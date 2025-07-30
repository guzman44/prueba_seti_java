/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler;

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

	public ProductoHandler(ProductoUseCase useCase) {
		this.useCase = useCase;
	}

	private final ProductoUseCase useCase;

	
	public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Producto.class)
                .flatMap(useCase::saveProducto)
                .flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(useCase.getAllProductos(), Producto.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return useCase.getProductoById(id)
                .flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(Producto.class)
                .flatMap(f -> useCase.updateProducto(id, f))
                .flatMap(f -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(f));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return useCase.deleteProducto(id).then(ServerResponse.noContent().build());
    }
	
	
}
