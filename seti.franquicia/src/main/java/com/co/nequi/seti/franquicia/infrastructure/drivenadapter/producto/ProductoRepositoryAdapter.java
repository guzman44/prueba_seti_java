package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import com.co.nequi.seti.franquicia.domain.gateway.ProductoRepository;
import com.co.nequi.seti.franquicia.domain.model.Producto;
import com.co.nequi.seti.franquicia.domain.model.ProductoStockPorSucursal;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.entity.ProductoEntity;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.mapper.ProductoMapper;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.repository.ProductoR2DBCRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Component
public class ProductoRepositoryAdapter implements ProductoRepository {
	
	public ProductoRepositoryAdapter(ProductoR2DBCRepository repository, DatabaseClient databaseClient) {
		this.repository = repository;
		this.databaseClient = databaseClient;
	}

	private final ProductoR2DBCRepository repository;

    private final DatabaseClient databaseClient;
	
	@Override
	public Mono<Producto> findById(Long id) {
		return repository.findById(id).map(ProductoMapper::toDomain);
	}

	@Override
	public Flux<Producto> findAll() {
		return repository.findAll().map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		ProductoEntity entity = ProductoMapper.toEntity(producto);
		return repository.save(entity).map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Producto> update(Long id, Producto producto) {
		return repository.findById(id).flatMap(existing -> {
			existing.setNombre(producto.getNombre());
			return repository.save(existing);
		}).map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Producto> updateStock(Long id, Producto producto) {
		return repository.findById(id).flatMap(existing -> {
			existing.setStock(producto.getStock());
			return repository.save(existing);
		}).map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Void> delete(Long id) {
		return repository.deleteById(id);
	}

	@Override
	public Flux<ProductoStockPorSucursal> obtenerProductoMayorStockPorSucursalDeFranquicia(Long idFranquicia) {
		String sql = ""
				+ "SELECT p.id AS idProducto, p.nombre AS nombreProducto, p.stock, s.id AS idSucursal, s.nombre AS nombreSucursal "
				+ "FROM producto p "
				+ "JOIN sucursal s ON p.sucursal_id = s.id "
				+ "JOIN ( "
				+ "SELECT sucursal_id, MAX(stock) AS max_stock "
				+ "FROM producto "
				+ "GROUP BY sucursal_id "
				+ ") max_p ON p.sucursal_id = max_p.sucursal_id AND p.stock = max_p.max_stock "
				+ "WHERE s.franquicia_id = :idFranquicia" ;

	        return databaseClient.sql(sql)
	                .bind("idFranquicia", idFranquicia)
	                .map(row -> ProductoMapper.toProductoStock(row))
	                .all();
	}

}
