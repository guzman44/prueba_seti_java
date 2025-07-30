/**
 * 
 */
package com.co.nequi.seti.franquicia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.application.usecases.franquicia.FranquiciaUseCase;
import com.co.nequi.seti.franquicia.domain.model.Franquicia;
import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.FranquiciaHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class FranquiciaHandlerTest {

    private FranquiciaUseCase useCase;
    private FranquiciaHandler handler;

    @BeforeEach
    void setUp() {
        useCase = mock(FranquiciaUseCase.class);
        handler = new FranquiciaHandler(useCase);
    }

    @Test
    void testCreate() {
        Franquicia franquicia = new Franquicia(1L, "Franquicia 1");
        ServerRequest request = MockServerRequest.builder().body(Mono.just(franquicia));

        when(useCase.saveFranquicia(any())).thenReturn(Mono.just(franquicia));

        Mono<ServerResponse> response = handler.create(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase, times(1)).saveFranquicia(any());
    }

    @Test
    void testFindAll() {
        Franquicia f = new Franquicia(1L, "Franquicia A");
        when(useCase.getAllFranquicias()).thenReturn(Flux.just(f));

        ServerRequest request = MockServerRequest.builder().build();
        Mono<ServerResponse> response = handler.findAll(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase, times(1)).getAllFranquicias();
    }

    @Test
    void testFindByIdFound() {
        Franquicia f = new Franquicia(1L, "Franquicia B");
        when(useCase.getFranquiciaById(1L)).thenReturn(Mono.just(f));

        ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();
        Mono<ServerResponse> response = handler.findById(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase).getFranquiciaById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(useCase.getFranquiciaById(99L)).thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder().pathVariable("id", "99").build();
        Mono<ServerResponse> response = handler.findById(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is4xxClientError())
                .verifyComplete();

        verify(useCase).getFranquiciaById(99L);
    }

    @Test
    void testUpdate() {
        Franquicia f = new Franquicia(1L, "Franquicia Actualizada");
        ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").body(Mono.just(f));

        when(useCase.updateFranquicia(eq(1L), any())).thenReturn(Mono.just(f));

        Mono<ServerResponse> response = handler.update(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase).updateFranquicia(eq(1L), any());
    }

    @Test
    void testDelete() {
        when(useCase.deleteFranquicia(1L)).thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();
        Mono<ServerResponse> response = handler.delete(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase).deleteFranquicia(1L);
    }
}
