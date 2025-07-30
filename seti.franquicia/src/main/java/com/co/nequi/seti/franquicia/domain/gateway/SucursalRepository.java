/**
 * 
 */
package com.co.nequi.seti.franquicia.domain.gateway;

import com.co.nequi.seti.franquicia.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public interface SucursalRepository {

	Mono<Sucursal> save(Sucursal Sucursal);

	Flux<Sucursal> findByIdFranquicia(Long IdFranquicia);

	Mono<Sucursal> findById(Long id);

	Flux<Sucursal> findAll();

	Mono<Sucursal> update(Long id, Sucursal Sucursal);

	Mono<Void> delete(Long id);

}
