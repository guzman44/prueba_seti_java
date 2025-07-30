package com.co.nequi.seti.franquicia.infrastructure.entrypoints;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.domain.model.Sucursal;
import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.SucursalHandler;

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
public class SucursalRouter {

	@Bean
	@RouterOperations({
        @RouterOperation(
            path = "/api/sucursal/sucursales",
            method = RequestMethod.POST,
            beanClass = SucursalHandler.class,
            beanMethod = "create",
            operation = @Operation(
                operationId = "crearSucursal",
                summary = "Crear una nueva sucursal",
                tags = { "Sucursal" },
                requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Sucursal.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Sucursal creada")
                }
            )
        ),
        @RouterOperation(
            path = "/api/sucursal/sucursales/{id}",
            method = RequestMethod.GET,
            beanClass = SucursalHandler.class,
            beanMethod = "findById",
            operation = @Operation(
                operationId = "obtenerSucursalPorId",
                summary = "Obtener sucursal por ID",
                tags = { "Sucursal" },
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", required = true)
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
                }
            )
        ),
        @RouterOperation(
            path = "/api/sucursal/sucursales",
            method = RequestMethod.GET,
            beanClass = SucursalHandler.class,
            beanMethod = "findAll",
            operation = @Operation(
                operationId = "listarSucursales",
                summary = "Listar todas las sucursales",
                tags = { "Sucursal" },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Listado exitoso")
                }
            )
        ),
        @RouterOperation(
            path = "/api/sucursal/sucursales/{id}",
            method = RequestMethod.PUT,
            beanClass = SucursalHandler.class,
            beanMethod = "update",
            operation = @Operation(
                operationId = "actualizarSucursal",
                summary = "Actualizar sucursal",
                tags = { "Sucursal" },
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", required = true)
                },
                requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Sucursal.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Sucursal actualizada")
                }
            )
        ),
        @RouterOperation(
            path = "/api/sucursal/sucursales/{id}",
            method = RequestMethod.DELETE,
            beanClass = SucursalHandler.class,
            beanMethod = "delete",
            operation = @Operation(
                operationId = "eliminarSucursal",
                summary = "Eliminar sucursal por ID",
                tags = { "Sucursal" },
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", required = true)
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Sucursal eliminada")
                }
            )
        )
    })
	public RouterFunction<ServerResponse> routeSucursal(SucursalHandler handler) {
		return RouterFunctions.route()
				.nest(path("/api/sucursal"),
						builder -> builder
								.POST("/sucursales", handler::create)
								.GET("/sucursales/{id}", handler::findById)
								.GET("/sucursales", handler::findAll)
								.GET("/sucursales", handler::findAll)
								.PUT("/sucursales/{id}", handler::update)
								.DELETE("/sucursales/{id}", handler::delete)

				).build();
	}
}