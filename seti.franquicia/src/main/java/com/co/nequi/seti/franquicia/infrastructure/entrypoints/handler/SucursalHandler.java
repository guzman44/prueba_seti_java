/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.application.usecases.sucursal.SucursalUseCase;
import com.co.nequi.seti.franquicia.domain.model.Sucursal;

import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Component
public class SucursalHandler {

	public SucursalHandler(SucursalUseCase useCase) {
		this.useCase = useCase;
	}

	private final SucursalUseCase useCase;

	public Mono<ServerResponse> create(ServerRequest request) {
		return request.bodyToMono(Sucursal.class).flatMap(useCase::saveSucursal)
				.flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
	}

	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(useCase.getAllSucursales(),
				Sucursal.class);
	}

	public Mono<ServerResponse> findById(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		return useCase.getSucursalById(id)
				.flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> update(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		return request.bodyToMono(Sucursal.class).flatMap(f -> useCase.updateSucursal(id, f))
				.flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
	}

	public Mono<ServerResponse> delete(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		return useCase.deleteSucursal(id).then(ServerResponse.noContent().build());
	}

}
