/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.mapper;

import com.co.nequi.seti.franquicia.domain.model.Sucursal;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.sucursal.entity.SucursalEntity;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class SucursalMapper {

	private SucursalMapper() {
	}

	public static Sucursal toDomain(SucursalEntity entity) {
		return new Sucursal(entity.getId(), entity.getNombre(), entity.getIdFranquicia());
	}

	public static SucursalEntity toEntity(Sucursal domain) {
		return new SucursalEntity(domain.getId(), domain.getNombre(),domain.getIdFranquicia());
	}

}
