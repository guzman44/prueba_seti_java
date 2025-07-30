/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.application.usecases.franquicia.FranquiciaUseCase;
import com.co.nequi.seti.franquicia.domain.model.Franquicia;

import reactor.core.publisher.Mono;



/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Component
public class FranquiciaHandler {

	public FranquiciaHandler(FranquiciaUseCase useCase) {
		this.useCase = useCase;
	}

	private final FranquiciaUseCase useCase;

	
	public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Franquicia.class)
                .flatMap(useCase::saveFranquicia)
                .flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(useCase.getAllFranquicias(), Franquicia.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return useCase.getFranquiciaById(id)
                .flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(Franquicia.class)
                .flatMap(f -> useCase.updateFranquicia(id, f))
                .flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return useCase.deleteFranquicia(id).then(ServerResponse.noContent().build());
    }
	
	
}
