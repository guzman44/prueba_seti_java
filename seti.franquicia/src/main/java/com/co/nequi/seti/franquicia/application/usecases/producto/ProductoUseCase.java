/**
 * 
 */
package com.co.nequi.seti.franquicia.application.usecases.producto;

import com.co.nequi.seti.franquicia.domain.gateway.ProductoRepository;
import com.co.nequi.seti.franquicia.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class ProductoUseCase {

	public ProductoUseCase(ProductoRepository repository) {
		super();
		this.repository = repository;
	}

	private final ProductoRepository repository;

	public Mono<Producto> saveProducto(Producto producto) {
		return repository.save(producto);
	}

	public Flux<Producto> getAllProductos() {
		return repository.findAll();
	}

	public Mono<Producto> getProductoById(Long id) {
		return repository.findById(id);
	}

	public Mono<Producto> updateProducto(Long id, Producto producto) {
		return repository.update(id, producto);
	}


	public Mono<Producto> updateProductoStock(Long id, Producto producto) {
		return repository.updateStock(id, producto);
	}

	public Mono<Void> deleteProducto(Long id) {
		return repository.delete(id);
	}

}
