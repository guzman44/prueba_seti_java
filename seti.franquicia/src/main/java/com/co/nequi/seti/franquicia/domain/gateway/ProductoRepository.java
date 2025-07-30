/**
 * 
 */
package com.co.nequi.seti.franquicia.domain.gateway;

import com.co.nequi.seti.franquicia.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public interface ProductoRepository {

	Mono<Producto> save(Producto Franquicia);

	Mono<Producto> findById(Long id);

	Flux<Producto> findAll();

	Mono<Producto> update(Long id, Producto Franquicia);

	Mono<Void> delete(Long id);
}
