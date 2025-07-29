/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.entity.FranquiciaEntity;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Repository
public interface FranquiciaR2DBCRepository extends ReactiveCrudRepository<FranquiciaEntity, Long> {

}
