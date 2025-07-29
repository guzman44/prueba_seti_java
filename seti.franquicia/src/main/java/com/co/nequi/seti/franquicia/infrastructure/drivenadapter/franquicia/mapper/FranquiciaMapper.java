/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.mapper;

import com.co.nequi.seti.franquicia.domain.model.Franquicia;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.entity.FranquiciaEntity;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class FranquiciaMapper {

	private FranquiciaMapper() {
	}

	public static Franquicia toDomain(FranquiciaEntity entity) {
		return new Franquicia(entity.getId(), entity.getNombre());
	}

	public static FranquiciaEntity toEntity(Franquicia domain) {
		return new FranquiciaEntity(domain.getId(), domain.getNombre());
	}

}
