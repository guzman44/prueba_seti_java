/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.entity.ProductoEntity;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Repository
public interface ProductoR2DBCRepository extends ReactiveCrudRepository<ProductoEntity, Long> {

}
