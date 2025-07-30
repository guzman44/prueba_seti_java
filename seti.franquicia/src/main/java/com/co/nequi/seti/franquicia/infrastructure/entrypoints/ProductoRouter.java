package com.co.nequi.seti.franquicia.infrastructure.entrypoints;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.ProductoHandler;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Configuration
public class ProductoRouter {

	@Bean
	public RouterFunction<ServerResponse> routeProducto(ProductoHandler handler) {
		return RouterFunctions.route()
				.nest(path("/api/producto"),
						builder -> builder
								.POST("/productos", handler::create)
								.GET("/productos/{id}", handler::findById)
								.GET("/productos", handler::findAll)
								.PUT("/productos/{id}", handler::update)
								.DELETE("/productos/{id}", handler::delete)

				).build();
	}
}