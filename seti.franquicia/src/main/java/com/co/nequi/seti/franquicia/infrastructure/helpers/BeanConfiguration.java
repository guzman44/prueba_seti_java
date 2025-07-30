/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.co.nequi.seti.franquicia.application.usecases.franquicia.FranquiciaUseCase;
import com.co.nequi.seti.franquicia.domain.gateway.FranquiciaRepository;

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
}