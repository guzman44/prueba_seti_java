/**
 * 
 */
package com.co.nequi.seti.franquicia;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.application.usecases.sucursal.SucursalUseCase;
import com.co.nequi.seti.franquicia.domain.model.Sucursal;
import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.SucursalHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class SucursalHandlerTest {

	private SucursalUseCase useCase;
	private SucursalHandler handler;

	@BeforeEach
	void setup() {
		useCase = mock(SucursalUseCase.class);
		handler = new SucursalHandler(useCase);
	}

	@Test
	void testCreate() {
		Sucursal sucursal = new Sucursal(1L, "Sucursal 1", 1L);
		ServerRequest request = MockServerRequest.builder().body(Mono.just(sucursal));

		when(useCase.saveSucursal(any())).thenReturn(Mono.just(sucursal));

		Mono<ServerResponse> responseMono = handler.create(request);

		StepVerifier.create(responseMono).expectNextMatches(res -> res.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase, times(1)).saveSucursal(any());
	}

	@Test
	void testFindAll() {
		Sucursal s = new Sucursal(1L, "Sucursal A", 1L);
		when(useCase.getAllSucursales()).thenReturn(Flux.just(s));

		ServerRequest request = MockServerRequest.builder().build();
		Mono<ServerResponse> response = handler.findAll(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdFound() {
		Sucursal sucursal = new Sucursal(2L, "Sucursal B", 2L);
		when(useCase.getSucursalById(2L)).thenReturn(Mono.just(sucursal));

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "2").build();
		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdNotFound() {
		when(useCase.getSucursalById(99L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "99").build();
		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is4xxClientError()).verifyComplete();
	}

	@Test
	void testUpdate() {
		Sucursal s = new Sucursal(3L, "Sucursal Actualizada", 1L);
		ServerRequest request = MockServerRequest.builder().pathVariable("id", "3").body(Mono.just(s));
		
		when(useCase.updateSucursal(eq(3L), any())).thenReturn(Mono.just(s));

		Mono<ServerResponse> response = handler.update(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase).updateSucursal(eq(3L), any());
	}

	@Test
	void testDelete() {
		when(useCase.deleteSucursal(4L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "4").build();
		Mono<ServerResponse> response = handler.delete(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase).deleteSucursal(4L);
	}
}
