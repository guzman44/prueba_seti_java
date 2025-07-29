/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.entity.SucursalEntity;

import reactor.core.publisher.Flux;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Repository
public interface SucursalR2DBCRepository extends ReactiveCrudRepository<SucursalEntity, Long> {
	Flux<SucursalEntity> findByIdFranquicia(Long idFranquicia);
}
