/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.mapper;

import com.co.nequi.seti.franquicia.domain.model.Producto;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.entity.ProductoEntity;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class ProductoMapper {

	private ProductoMapper() {
	}

	public static Producto toDomain(ProductoEntity entity) {
		return new Producto(entity.getId(), entity.getNombre(), entity.getStock(), entity.getSucursal_id());
	}

	public static ProductoEntity toEntity(Producto domain) {
		return new ProductoEntity(domain.getId(), domain.getNombre(), domain.getStock(), domain.getIdSucursal());
	}

}
