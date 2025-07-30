package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(ProductoRepositoryAdapter.class);

	public ProductoRepositoryAdapter(ProductoR2DBCRepository repository, DatabaseClient databaseClient) {
		this.repository = repository;
		this.databaseClient = databaseClient;
	}

	private final ProductoR2DBCRepository repository;

	private final DatabaseClient databaseClient;

	@Override
	public Mono<Producto> findById(Long id) {
		return repository.findById(id).doOnNext(p -> logger.info("Producto encontrado: {}", id))
				.doOnError(e -> logger.error("Error al buscar producto ID: {}", id, e)).map(ProductoMapper::toDomain);
	}

	@Override
	public Flux<Producto> findAll() {
		return repository.findAll().doOnNext(p -> logger.info("Producto listado: {}", p.getId()))
				.doOnComplete(() -> logger.info("Listado de productos completo"))
				.doOnError(e -> logger.error("Error al listar productos", e)).map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		ProductoEntity entity = ProductoMapper.toEntity(producto);
		return repository.save(entity).doOnNext(p -> logger.info("Producto guardado: {}", p.getId()))
				.doOnError(e -> logger.error("Error al guardar producto", e)).map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Producto> update(Long id, Producto producto) {
		return repository.findById(id).flatMap(existing -> {
			existing.setNombre(producto.getNombre());
			return repository.save(existing);
		}).doOnNext(p -> logger.info("Producto actualizado: {}", p.getId()))
				.doOnError(e -> logger.error("Error al actualizar producto ID: {}", id, e))
				.map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Producto> updateStock(Long id, Producto producto) {
		return repository.findById(id).flatMap(existing -> {
			existing.setStock(producto.getStock());
			return repository.save(existing);
		}).doOnNext(p -> logger.info("Stock actualizado: {} unidades para producto ID: {}", p.getStock(), p.getId()))
				.doOnError(e -> logger.error("Error al actualizar stock producto ID: {}", id, e))
				.map(ProductoMapper::toDomain);
	}

	@Override
	public Mono<Void> delete(Long id) {
		return repository.deleteById(id).doOnSuccess(v -> logger.info("Producto eliminado: {}", id))
				.doOnError(e -> logger.error("Error al eliminar producto ID: {}", id, e));
	}

	@Override
	public Flux<ProductoStockPorSucursal> obtenerProductoMayorStockPorSucursalDeFranquicia(Long idFranquicia) {
		String sql = ""
				+ "SELECT p.id AS idProducto, p.nombre AS nombreProducto, p.stock, s.id AS idSucursal, s.nombre AS nombreSucursal "
				+ "FROM producto p " + "JOIN sucursal s ON p.sucursal_id = s.id JOIN ( "
				+ "SELECT sucursal_id, MAX(stock) AS max_stock " + "FROM producto  GROUP BY sucursal_id "
				+ ") max_p ON p.sucursal_id = max_p.sucursal_id AND p.stock = max_p.max_stock"
				+ "WHERE s.franquicia_id = :idFranquicia";

		return databaseClient.sql(sql).bind("idFranquicia", idFranquicia).map(ProductoMapper::toProductoStock).all()
				.doOnNext(p -> logger.info("Producto con mayor stock por sucursal: {}", p))
				.doOnComplete(() -> logger.info("Consulta productos con mayor stock terminada"))
				.doOnError(e -> logger.error("Error en consulta de mayor stock por sucursal", e));
	}

}
