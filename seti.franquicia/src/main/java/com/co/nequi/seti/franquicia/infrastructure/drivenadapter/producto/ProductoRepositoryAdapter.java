package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto;

import org.springframework.stereotype.Component;

import com.co.nequi.seti.franquicia.domain.gateway.ProductoRepository;
import com.co.nequi.seti.franquicia.domain.model.Producto;
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

	public ProductoRepositoryAdapter(ProductoR2DBCRepository repository) {
		this.repository = repository;
	}

	private final ProductoR2DBCRepository repository;

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

}
