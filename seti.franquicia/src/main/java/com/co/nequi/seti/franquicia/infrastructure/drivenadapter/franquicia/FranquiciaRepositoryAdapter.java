package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.co.nequi.seti.franquicia.domain.gateway.FranquiciaRepository;
import com.co.nequi.seti.franquicia.domain.model.Franquicia;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.entity.FranquiciaEntity;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.mapper.FranquiciaMapper;
import com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.repository.FranquiciaR2DBCRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Component
public class FranquiciaRepositoryAdapter implements FranquiciaRepository {

    private static final Logger logger = LoggerFactory.getLogger(FranquiciaRepositoryAdapter.class);

    
	public FranquiciaRepositoryAdapter(FranquiciaR2DBCRepository repository) {
		this.repository = repository;
	}

	private final FranquiciaR2DBCRepository repository;

	@Override
    public Mono<Franquicia> findById(Long id) {
        return repository.findById(id)
            .doOnNext(entity -> logger.info("Franquicia encontrada con ID: {}", id))
            .doOnError(e -> logger.error("Error buscando franquicia con ID: {}", id, e))
            .map(FranquiciaMapper::toDomain);
    }

    @Override
    public Flux<Franquicia> findAll() {
        return repository.findAll()
            .doOnNext(entity -> logger.info("Franquicia listada: {}", entity.getId()))
            .doOnComplete(() -> logger.info("Listado completo de franquicias"))
            .doOnError(e -> logger.error("Error listando franquicias", e))
            .map(FranquiciaMapper::toDomain);
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        FranquiciaEntity entity = FranquiciaMapper.toEntity(franquicia);
        return repository.save(entity)
            .doOnNext(saved -> logger.info("Franquicia guardada con ID: {}", saved.getId()))
            .doOnError(e -> logger.error("Error guardando franquicia", e))
            .map(FranquiciaMapper::toDomain);
    }

    @Override
    public Mono<Franquicia> update(Long id, Franquicia franquicia) {
        return repository.findById(id)
            .flatMap(existing -> {
                existing.setNombre(franquicia.getNombre());
                return repository.save(existing);
            })
            .doOnNext(updated -> logger.info("Franquicia actualizada con ID: {}", updated.getId()))
            .doOnError(e -> logger.error("Error actualizando franquicia con ID: {}", id, e))
            .map(FranquiciaMapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id)
            .doOnSuccess(v -> logger.info("Franquicia eliminada con ID: {}", id))
            .doOnError(e -> logger.error("Error eliminando franquicia con ID: {}", id, e));
    }

}
