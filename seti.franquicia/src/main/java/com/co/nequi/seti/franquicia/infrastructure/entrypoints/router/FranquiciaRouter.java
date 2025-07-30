package com.co.nequi.seti.franquicia.infrastructure.entrypoints.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.domain.model.Franquicia;
import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.FranquiciaHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Configuration
public class FranquiciaRouter {

	@Bean
	@RouterOperations({
	    @RouterOperation(
	        path = "/api/franquicia/franquicias",
	        method = RequestMethod.POST,
	        beanClass = FranquiciaHandler.class,
	        beanMethod = "create",
	        operation = @Operation(
	            operationId = "crearFranquicia",
	            summary = "Crear una nueva franquicia",
	            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Franquicia.class))),
	            responses = {
	                @ApiResponse(responseCode = "200", description = "Franquicia creada exitosamente")
	            }
	        )
	    ),
	    @RouterOperation(
	        path = "/api/franquicia/franquicias/{id}",
	        method = RequestMethod.GET,
	        beanClass = FranquiciaHandler.class,
	        beanMethod = "findById",
	        operation = @Operation(
	            operationId = "obtenerFranquiciaPorId",
	            summary = "Obtener franquicia por ID",
	            parameters = {
	                @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia")
	            },
	            responses = {
	                @ApiResponse(responseCode = "200", description = "Franquicia encontrada", content = @Content(schema = @Schema(implementation = Franquicia.class))),
	                @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
	            }
	        )
	    ),
	    @RouterOperation(
	        path = "/api/franquicia/franquicias",
	        method = RequestMethod.GET,
	        beanClass = FranquiciaHandler.class,
	        beanMethod = "findAll",
	        operation = @Operation(
	            operationId = "listarFranquicias",
	            summary = "Listar todas las franquicias",
	            responses = {
	                @ApiResponse(responseCode = "200", description = "Lista de franquicias", content = @Content(schema = @Schema(implementation = Franquicia.class)))
	            }
	        )
	    ),
	    @RouterOperation(
	        path = "/api/franquicia/franquicias/{id}",
	        method = RequestMethod.PUT,
	        beanClass = FranquiciaHandler.class,
	        beanMethod = "update",
	        operation = @Operation(
	            operationId = "actualizarFranquicia",
	            summary = "Actualizar franquicia",
	            parameters = {
	                @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia")
	            },
	            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Franquicia.class))),
	            responses = {
	                @ApiResponse(responseCode = "200", description = "Franquicia actualizada exitosamente")
	            }
	        )
	    ),
	    @RouterOperation(
	        path = "/api/franquicia/franquicias/{id}",
	        method = RequestMethod.DELETE,
	        beanClass = FranquiciaHandler.class,
	        beanMethod = "delete",
	        operation = @Operation(
	            operationId = "eliminarFranquicia",
	            summary = "Eliminar franquicia por ID",
	            parameters = {
	                @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia")
	            },
	            responses = {
	                @ApiResponse(responseCode = "204", description = "Franquicia eliminada exitosamente")
	            }
	        )
	    )
	})
	public RouterFunction<ServerResponse> routeFranquicia(FranquiciaHandler handler) {
		return RouterFunctions.route()
				.nest(path("/api/franquicia"),
						builder -> builder
								.POST("/franquicias", handler::create)
								.GET("/franquicias/{id}", handler::findById)
								.GET("/franquicias", handler::findAll)
								.PUT("/franquicias/{id}", handler::update)
								.DELETE("/franquicias/{id}", handler::delete)

				).build();
	}
}