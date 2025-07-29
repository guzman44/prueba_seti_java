/**
 * 
 */
package com.co.nequi.seti.franquicia.domain.gateway;

import com.co.nequi.seti.franquicia.domain.model.Franquicia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public interface FranquiciaRepository {

	Mono<Franquicia> save(Franquicia Franquicia);

	Mono<Franquicia> findById(Long id);

	Flux<Franquicia> findAll();

	Mono<Franquicia> update(Long id, Franquicia Franquicia);

	Mono<Void> delete(Long id);


}
