package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia;

import org.springframework.stereotype.Component;

import com.co.nequi.seti.franquicia.domain.gateway.FranquiciaRepository;
import com.co.nequi.seti.franquicia.domain.model.Franquicia;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.entity.FranquiciaEntity;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.mapper.FranquiciaMapper;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.repository.FranquiciaR2DBCRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Component
public class FranquiciaRepositoryAdapter implements FranquiciaRepository {

	public FranquiciaRepositoryAdapter(FranquiciaR2DBCRepository repository) {
		this.repository = repository;
	}

	private final FranquiciaR2DBCRepository repository;

	@Override
	public Mono<Franquicia> findById(Long id) {
		return repository.findById(id).map(FranquiciaMapper::toDomain);
	}

	@Override
	public Flux<Franquicia> findAll() {
		return repository.findAll().map(FranquiciaMapper::toDomain);
	}

	@Override
	public Mono<Franquicia> save(Franquicia Franquicia) {
		FranquiciaEntity entity = FranquiciaMapper.toEntity(Franquicia);
		return repository.save(entity).map(FranquiciaMapper::toDomain);
	}

	@Override
	public Mono<Franquicia> update(Long id, Franquicia Franquicia) {
		return repository.findById(id).flatMap(existing -> {
			existing.setNombre(Franquicia.getNombre());
			return repository.save(existing);
		}).map(FranquiciaMapper::toDomain);
	}

	@Override
	public Mono<Void> delete(Long id) {
		return repository.deleteById(id);
	}

}
