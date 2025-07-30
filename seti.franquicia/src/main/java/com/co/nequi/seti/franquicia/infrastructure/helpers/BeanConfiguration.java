/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.co.nequi.seti.franquicia.application.usecases.franquicia.FranquiciaUseCase;
import com.co.nequi.seti.franquicia.application.usecases.producto.ProductoUseCase;
import com.co.nequi.seti.franquicia.application.usecases.sucursal.SucursalUseCase;
import com.co.nequi.seti.franquicia.domain.gateway.FranquiciaRepository;
import com.co.nequi.seti.franquicia.domain.gateway.ProductoRepository;
import com.co.nequi.seti.franquicia.domain.gateway.SucursalRepository;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Configuration
public class BeanConfiguration {

	@Bean
	public FranquiciaUseCase franquiciaUseCase(FranquiciaRepository repository) {
		return new FranquiciaUseCase(repository);
	}

	@Bean
	public ProductoUseCase productoUseCase(ProductoRepository repository) {
		return new ProductoUseCase(repository);
	}

	@Bean
	public SucursalUseCase sucursalUseCase(SucursalRepository repository) {
		return new SucursalUseCase(repository);
	}
}