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

	private static final Logger log = LoggerFactory.getLogger(FranquiciaHandler.class);

	public FranquiciaHandler(FranquiciaUseCase useCase) {
		this.useCase = useCase;
	}

	private final FranquiciaUseCase useCase;

	public Mono<ServerResponse> create(ServerRequest request) {
		return request.bodyToMono(Franquicia.class).doOnNext(f -> log.info("Creando franquicia: {}", f.getNombre()))
				.flatMap(useCase::saveFranquicia).doOnNext(f -> log.info("Franquicia guardada: {}", f.getNombre()))
				.doOnError(e -> log.error("Error al guardar franquicia", e))
				.doOnSuccess(f -> log.info("Creación de franquicia finalizada"))
				.flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
	}

	public Mono<ServerResponse> findAll(ServerRequest request) {
		log.info("Solicitando listado de todas las franquicias");

		return useCase.getAllFranquicias().doOnNext(f -> log.info("Franquicia encontrada: {}", f.getNombre()))
				.doOnError(e -> log.error("Error al obtener franquicias", e)).collectList()
				.doOnSuccess(list -> log.info("Total franquicias encontradas: {}", list.size()))
				.flatMap(list -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(list));
	}

	public Mono<ServerResponse> findById(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		log.info("Buscando franquicia con ID: {}", id);

		return useCase.getFranquiciaById(id).doOnNext(f -> log.info("Franquicia encontrada: {}", f.getNombre()))
				.doOnError(e -> log.error("Error al buscar franquicia con ID {}", id, e))
				.flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> update(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		log.info("Actualizando franquicia con ID: {}", id);

		return request.bodyToMono(Franquicia.class).flatMap(f -> useCase.updateFranquicia(id, f)
				.doOnNext(updated -> log.info("Franquicia actualizada: {}", updated.getNombre()))
				.doOnError(e -> log.error("Error al actualizar franquicia con ID {}", id, e))
				.flatMap(updated -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updated)));
	}

	public Mono<ServerResponse> delete(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		log.info("Eliminando franquicia con ID: {}", id);

		return useCase.deleteFranquicia(id).doOnSuccess(unused -> log.info("Franquicia con ID {} eliminada", id))
				.doOnError(e -> log.error("Error al eliminar franquicia con ID {}", id, e))
				.then(ServerResponse.noContent().build());
	}

}
