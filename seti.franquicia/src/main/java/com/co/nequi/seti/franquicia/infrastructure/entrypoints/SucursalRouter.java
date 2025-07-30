package com.co.nequi.seti.franquicia.infrastructure.entrypoints;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.SucursalHandler;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Configuration
public class SucursalRouter {

	@Bean
	public RouterFunction<ServerResponse> routeSucursal(SucursalHandler handler) {
		return RouterFunctions.route()
				.nest(path("/api/sucursal"),
						builder -> builder
								.POST("/sucursales", handler::create)
								.GET("/sucursales/{id}", handler::findById)
								.GET("/sucursales", handler::findAll)
								.PUT("/sucursales/{id}", handler::update)
								.DELETE("/sucursales/{id}", handler::delete)

				).build();
	}
}