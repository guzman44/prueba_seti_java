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

import com.co.nequi.seti.franquicia.application.usecases.producto.ProductoUseCase;
import com.co.nequi.seti.franquicia.domain.model.Producto;

import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Component
public class ProductoHandler {

	private static final Logger log = LoggerFactory.getLogger(ProductoHandler.class);

	public ProductoHandler(ProductoUseCase useCase) {
		this.useCase = useCase;
	}

	private final ProductoUseCase useCase;

	public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Producto.class)
            .doOnNext(p -> log.info("Creando producto: {}", p.getNombre()))
            .flatMap(useCase::saveProducto)
            .doOnNext(p -> log.info(" Producto guardado: {}", p.getNombre()))
            .doOnError(e -> log.error("Error al guardar producto", e))
            .doOnSuccess(p -> log.info("Creación de producto finalizada"))
            .flatMap(p -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(p));
    }

	public Mono<ServerResponse> obtenerProductoMayorStockPorSucursalDeFranquicia(ServerRequest request) {
		Long idFranquicia = Long.valueOf(request.pathVariable("idFranquicia"));
		log.info("Buscando productos con mayor stock por sucursal para franquicia {}", idFranquicia);

		return useCase.obtenerProductoMayorStockPorSucursalDeFranquicia(idFranquicia)
				.doOnNext(p -> log.info("Producto con mayor stock encontrado: {}", p.getNombreProducto()))
				.doOnError(e -> log.error("Error al obtener productos por stock", e)).collectList()
				.doOnSuccess(list -> log.info("Consulta finalizada con {} productos", list.size()))
				.flatMap(list -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(list));
	}

    public Mono<ServerResponse> findAll(ServerRequest request) {
        log.info("Solicitando listado de todos los productos");

        return useCase.getAllProductos()
            .doOnNext(p -> log.info("Producto encontrado: {}", p.getNombre()))
            .doOnError(e -> log.error("Error al obtener todos los productos", e))
            .collectList()
            .doOnSuccess(list -> log.info("Total productos encontrados: {}", list.size()))
            .flatMap(list -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(list));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("Buscando producto con ID: {}", id);

        return useCase.getProductoById(id)
            .doOnNext(p -> log.info("Producto encontrado: {}", p.getNombre()))
            .doOnError(e -> log.error("Error al obtener producto por ID {}", id, e))
            .flatMap(p -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(p))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("Actualizando producto con ID: {}", id);

        return request.bodyToMono(Producto.class)
            .flatMap(p -> useCase.updateProducto(id, p)
                .doOnNext(updated -> log.info("Producto actualizado: {}", updated.getNombre()))
                .doOnError(e -> log.error("Error al actualizar producto {}", id, e))
                .flatMap(updated -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updated)));
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("Actualizando stock del producto ID: {}", id);

        return request.bodyToMono(Producto.class)
            .flatMap(p -> useCase.updateProductoStock(id, p)
                .doOnNext(updated -> log.info("Stock actualizado para producto: {}", updated.getNombre()))
                .doOnError(e -> log.error("Error al actualizar stock producto {}", id, e))
                .flatMap(updated -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updated)));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("Solicitando eliminación del producto ID: {}", id);

        return useCase.deleteProducto(id)
            .doOnSuccess(unused -> log.info("Producto con ID {} eliminado", id))
            .doOnError(e -> log.error("Error al eliminar producto con ID {}", id, e))
            .then(ServerResponse.noContent().build());
    }

}
