package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal;

import org.springframework.stereotype.Component;

import com.co.nequi.seti.franquicia.domain.gateway.SucursalRepository;
import com.co.nequi.seti.franquicia.domain.model.Sucursal;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.entity.SucursalEntity;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.mapper.SucursalMapper;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.repository.SucursalR2DBCRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component

public class SucursalRepositoryAdapter implements SucursalRepository {

	public SucursalRepositoryAdapter(SucursalR2DBCRepository repository) {
		this.repository = repository;
	}

	private final SucursalR2DBCRepository repository;

	@Override
	public Mono<Sucursal> save(Sucursal Sucursal) {
		SucursalEntity entity = SucursalMapper.toEntity(Sucursal);
		return repository.save(entity).map(SucursalMapper::toDomain);
	}

	
	@Override
	public Mono<Sucursal> findById(Long id) {
		return repository.findById(id).map(SucursalMapper::toDomain);
	}

	@Override
	public Flux<Sucursal> findByIdFranquicia(Long IdFranquicia) {
		return repository.findByIdFranquicia(IdFranquicia).map(SucursalMapper::toDomain);
	}

	
	@Override
	public Mono<Sucursal> update(Long id, Sucursal Sucursal) {
		return repository.findById(id).flatMap(existing -> {
			existing.setNombre(Sucursal.getNombre());
			return repository.save(existing);
		}).map(SucursalMapper::toDomain);
	}

	@Override
	public Mono<Void> delete(Long id) {
		return repository.deleteById(id);
	}
}