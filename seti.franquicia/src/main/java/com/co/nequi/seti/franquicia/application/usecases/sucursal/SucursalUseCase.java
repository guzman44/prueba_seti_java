/**
 * 
 */
package com.co.nequi.seti.franquicia.application.usecases.sucursal;

import com.co.nequi.seti.franquicia.domain.gateway.SucursalRepository;
import com.co.nequi.seti.franquicia.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class SucursalUseCase {

	public SucursalUseCase(SucursalRepository repository) {
		super();
		this.repository = repository;
	}

	private final SucursalRepository repository;

	public Mono<Sucursal> saveSucursal(Sucursal sucursal) {
		return repository.save(sucursal);
	}

	public Flux<Sucursal> getAllSucursales() {
		return repository.findAll();
	}

	public Mono<Sucursal> getSucursalById(Long id) {
		return repository.findById(id);
	}

	public Mono<Sucursal> updateSucursal(Long id, Sucursal sucursal) {
		return repository.update(id, sucursal);
	}

	public Mono<Void> deleteSucursal(Long id) {
		return repository.delete(id);
	}
}
