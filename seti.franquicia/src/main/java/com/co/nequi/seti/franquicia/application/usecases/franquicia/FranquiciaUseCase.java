/**
 * 
 */
package com.co.nequi.seti.franquicia.application.usecases.franquicia;

import com.co.nequi.seti.franquicia.domain.gateway.FranquiciaRepository;
import com.co.nequi.seti.franquicia.domain.model.Franquicia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class FranquiciaUseCase {

	public FranquiciaUseCase(FranquiciaRepository repository) {
		this.repository = repository;
	}

	private final FranquiciaRepository repository;

	public Mono<Franquicia> saveFranquicia(Franquicia franquicia) {
		return repository.save(franquicia);
	}

	public Flux<Franquicia> getAllFranquicias() {
		return repository.findAll();
	}

	public Mono<Franquicia> getFranquiciaById(Long id) {
		return repository.findById(id);
	}

	public Mono<Franquicia> updateFranquicia(Long id, Franquicia franquicia) {
		return repository.update(id, franquicia);
	}

	public Mono<Void> deleteFranquicia(Long id) {
		return repository.delete(id);
	}

}
