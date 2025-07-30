/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(SucursalHandler.class);

	public SucursalHandler(SucursalUseCase useCase) {
		this.useCase = useCase;
	}

	private final SucursalUseCase useCase;

	public Mono<ServerResponse> create(ServerRequest request) {
		return request.bodyToMono(Sucursal.class).doOnNext(s -> log.info("Creando sucursal: {}", s.getNombre()))
				.flatMap(useCase::saveSucursal).doOnNext(s -> log.info("Sucursal guardada: {}", s.getNombre()))
				.doOnError(e -> log.error("Error al guardar sucursal", e))
				.doOnSuccess(s -> log.info("Creación finalizada"))
				.flatMap(s -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s));
	}

	public Mono<ServerResponse> findAll(ServerRequest request) {
		log.info("Solicitando todas las sucursales");

		return useCase.getAllSucursales().doOnNext(s -> log.info("Sucursal encontrada: {}", s.getNombre()))
				.doOnError(e -> log.error("Error al obtener sucursales", e)).collectList()
				.doOnSuccess(list -> log.info("Total sucursales encontradas: {}", list.size()))
				.flatMap(list -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(list));
	}

	public Mono<ServerResponse> findById(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		log.info("Buscando sucursal con ID: {}", id);

		return useCase.getSucursalById(id).doOnNext(s -> log.info("Sucursal encontrada: {}", s.getNombre()))
				.doOnError(e -> log.error("Error al buscar sucursal con ID {}", id, e))
				.flatMap(s -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> update(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		log.info("Actualizando sucursal con ID: {}", id);

		return request.bodyToMono(Sucursal.class).flatMap(s -> useCase.updateSucursal(id, s)
				.doOnNext(updated -> log.info("Sucursal actualizada: {}", updated.getNombre()))
				.doOnError(e -> log.error("Error al actualizar sucursal con ID {}", id, e))
				.flatMap(updated -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updated)));
	}

	public Mono<ServerResponse> delete(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		log.info("Eliminando sucursal con ID: {}", id);

		return useCase.deleteSucursal(id).doOnSuccess(v -> log.info("Sucursal eliminada con ID: {}", id))
				.doOnError(e -> log.error("Error al eliminar sucursal con ID {}", id, e))
				.then(ServerResponse.noContent().build());
	}

}
