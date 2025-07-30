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

import com.co.nequi.seti.franquicia.domain.model.Producto;
import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.ProductoHandler;

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
public class ProductoRouter {

	@Bean
	@RouterOperations({
        @RouterOperation(
            path = "/api/producto/productos",
            method = RequestMethod.POST,
            beanClass = ProductoHandler.class,
            beanMethod = "create",
            operation = @Operation(
                operationId = "crearProducto",
                summary = "Crear un nuevo producto",
                tags = { "Producto" },
                requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Producto.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Producto creado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
                }
            )
        ),
        @RouterOperation(
            path = "/api/producto/productos/{id}",
            method = RequestMethod.GET,
            beanClass = ProductoHandler.class,
            beanMethod = "findById",
            operation = @Operation(
                operationId = "obtenerProductoPorId",
                summary = "Obtener producto por ID",
                tags = { "Producto" },
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", required = true, description = "ID del producto")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
                }
            )
        ),
        @RouterOperation(
            path = "/api/producto/productos",
            method = RequestMethod.GET,
            beanClass = ProductoHandler.class,
            beanMethod = "findAll",
            operation = @Operation(
                operationId = "listarProductos",
                summary = "Listar todos los productos",
                tags = { "Producto" },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido")
                }
            )
        ),
        @RouterOperation(
            path = "/api/producto/productos/{id}",
            method = RequestMethod.PUT,
            beanClass = ProductoHandler.class,
            beanMethod = "update",
            operation = @Operation(
                operationId = "actualizarProducto",
                summary = "Actualizar producto por ID",
                tags = { "Producto" },
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", required = true, description = "ID del producto")
                },
                requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Producto.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
                }
            )
        ),
        @RouterOperation(
            path = "/api/producto/productos/{id}",
            method = RequestMethod.DELETE,
            beanClass = ProductoHandler.class,
            beanMethod = "delete",
            operation = @Operation(
                operationId = "eliminarProducto",
                summary = "Eliminar producto por ID",
                tags = { "Producto" },
                parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", required = true, description = "ID del producto")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Producto eliminado")
                }
            )
        ),
        @RouterOperation(
                path = "/api/producto/maxStockByFranquicia/{idFranquicia}",
                method = RequestMethod.GET,
                beanClass = ProductoHandler.class,
                beanMethod = "obtenerProductoMayorStockPorSucursalDeFranquicia",
                operation = @Operation(
                    operationId = "getMaxStockByFranquicia",
                    summary = "Producto con mayor stock por sucursal para una franquicia",
                    parameters = {
                        @Parameter(in = ParameterIn.PATH, name = "idFranquicia", required = true, description = "ID de la franquicia")
                    },
                    responses = {
                        @ApiResponse(responseCode = "200", description = "Listado de productos con mayor stock por sucursal"),
                        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
                    }
                )
            ),
            @RouterOperation(
                path = "/api/producto/{id}/stock",
                method = RequestMethod.PUT,
                beanClass = ProductoHandler.class,
                beanMethod = "updateStock",
                operation = @Operation(
                    operationId = "updateStock",
                    summary = "Actualizar el stock de un producto por ID",
                    parameters = {
                        @Parameter(in = ParameterIn.PATH, name = "id", required = true, description = "ID del producto")
                    },
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Producto con el nuevo stock",
                        required = true
                    ),
                    responses = {
                        @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente"),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
                    }
                )
            )
    })
	public RouterFunction<ServerResponse> routeProducto(ProductoHandler handler) {
		return RouterFunctions.route()
				.nest(path("/api/producto"),
						builder -> builder
								.POST("/productos", handler::create)
								.GET("/productos/{id}", handler::findById)
								.GET("/productos", handler::findAll)
								.GET("/maxStockByFranquicia/{idFranquicia}", handler::obtenerProductoMayorStockPorSucursalDeFranquicia)
								.PUT("/productos/{id}", handler::update)
								.PUT("/productos/{id}/stock", handler::updateStock)
								.DELETE("/productos/{id}", handler::delete)

				).build();
	}
}