package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.co.nequi.seti.franquicia.domain.gateway.SucursalRepository;
import com.co.nequi.seti.franquicia.domain.model.Sucursal;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.entity.SucursalEntity;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.mapper.SucursalMapper;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.repository.SucursalR2DBCRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Component
public class SucursalRepositoryAdapter implements SucursalRepository {

	private static final Logger logger = LoggerFactory.getLogger(SucursalRepositoryAdapter.class);

	public SucursalRepositoryAdapter(SucursalR2DBCRepository repository) {
		this.repository = repository;
	}

	private final SucursalR2DBCRepository repository;

	@Override
	public Mono<Sucursal> save(Sucursal sucursal) {
		SucursalEntity entity = SucursalMapper.toEntity(sucursal);
		return repository.save(entity).doOnNext(saved -> logger.info("Sucursal guardada: {}", saved.getId()))
				.doOnError(e -> logger.error("Error al guardar sucursal", e)).map(SucursalMapper::toDomain);
	}

	@Override
	public Mono<Sucursal> findById(Long id) {
		return repository.findById(id).doOnNext(sucursal -> logger.info("Sucursal encontrada: {}", id))
				.doOnError(e -> logger.error("Error al buscar sucursal ID: {}", id, e)).map(SucursalMapper::toDomain);
	}

	@Override
	public Flux<Sucursal> findByIdFranquicia(Long idFranquicia) {
		return repository.findByIdFranquicia(idFranquicia)
				.doOnNext(sucursal -> logger.info("Sucursal por franquicia ID {}: {}", idFranquicia, sucursal.getId()))
				.doOnComplete(() -> logger.info("Consulta por franquicia completa"))
				.doOnError(e -> logger.error("Error al buscar sucursales por franquicia", e))
				.map(SucursalMapper::toDomain);
	}

	@Override
	public Mono<Sucursal> update(Long id, Sucursal sucursal) {
		return repository.findById(id).flatMap(existing -> {
			existing.setNombre(sucursal.getNombre());
			existing.setIdFranquicia(sucursal.getIdFranquicia());
			return repository.save(existing);
		}).doOnNext(updated -> logger.info("Sucursal actualizada: {}", updated.getId()))
				.doOnError(e -> logger.error("Error al actualizar sucursal ID: {}", id, e))
				.map(SucursalMapper::toDomain);
	}

	@Override
	public Mono<Void> delete(Long id) {
		return repository.deleteById(id).doOnSuccess(v -> logger.info("Sucursal eliminada: {}", id))
				.doOnError(e -> logger.error("Error al eliminar sucursal ID: {}", id, e));
	}

	@Override
	public Flux<Sucursal> findAll() {
		return repository.findAll().doOnNext(sucursal -> logger.info("Sucursal listada: {}", sucursal.getId()))
				.doOnComplete(() -> logger.info("Listado completo de sucursales"))
				.doOnError(e -> logger.error("Error al listar sucursales", e)).map(SucursalMapper::toDomain);
	}
}