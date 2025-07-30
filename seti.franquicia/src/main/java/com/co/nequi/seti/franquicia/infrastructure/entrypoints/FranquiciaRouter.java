package com.co.nequi.seti.franquicia.infrastructure.entrypoints;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.FranquiciaHandler;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Configuration
public class FranquiciaRouter {

	@Bean
	public RouterFunction<?> franquiciaRoutes(FranquiciaHandler handler) {
		return route(POST("/franquicias"), handler::create).andRoute(GET("/franquicias"), handler::findAll)
				.andRoute(GET("/franquicias/{id}"), handler::findById)
				.andRoute(PUT("/franquicias/{id}"), handler::update)
				.andRoute(DELETE("/franquicias/{id}"), handler::delete);
	}
}