/**
 * 
 */
package com.co.nequi.seti.franquicia.domain.gateway;

import com.co.nequi.seti.franquicia.domain.model.Producto;
import com.co.nequi.seti.franquicia.domain.model.ProductoStockPorSucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public interface ProductoRepository {

	Mono<Producto> save(Producto producto);

	Mono<Producto> findById(Long id);

	Flux<Producto> findAll();

	Mono<Producto> update(Long id, Producto producto);

	Mono<Producto> updateStock(Long id, Producto producto);
	
	Mono<Void> delete(Long id);
		
    Flux<ProductoStockPorSucursal> obtenerProductoMayorStockPorSucursalDeFranquicia(Long idFranquicia);

}
