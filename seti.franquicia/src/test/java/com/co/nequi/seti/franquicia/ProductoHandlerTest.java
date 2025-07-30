package com.co.nequi.seti.franquicia;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.co.nequi.seti.franquicia.application.usecases.producto.ProductoUseCase;
import com.co.nequi.seti.franquicia.domain.model.Producto;
import com.co.nequi.seti.franquicia.domain.model.ProductoStockPorSucursal;
import com.co.nequi.seti.franquicia.infrastructure.entrypoints.handler.ProductoHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class ProductoHandlerTest {

	private ProductoUseCase useCase;
	private ProductoHandler handler;

	@BeforeEach
	void setup() {
		useCase = mock(ProductoUseCase.class);
		handler = new ProductoHandler(useCase);
	}

	@Test
	void testCreate() {
		Producto producto = new Producto(1L, "Producto 1", 10L, 1L);
		ServerRequest request = MockServerRequest.builder()
				.body(Mono.just(producto));

		when(useCase.saveProducto(any())).thenReturn(Mono.just(producto));

		Mono<ServerResponse> responseMono = handler.create(request);

		StepVerifier.create(responseMono).expectNextMatches(res -> res.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase, times(1)).saveProducto(any());
	}

	@Test
	void testFindAll() {
		Producto p = new Producto(1L, "Producto A", 5L, 1L);
		when(useCase.getAllProductos()).thenReturn(Flux.just(p));

		ServerRequest request = MockServerRequest.builder().build();
		Mono<ServerResponse> response = handler.findAll(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdFound() {
		Producto producto = new Producto(1L, "Producto B", 10L, 1L);
		when(useCase.getProductoById(1L)).thenReturn(Mono.just(producto));

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();

		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdNotFound() {
		when(useCase.getProductoById(99L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "99").build();

		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is4xxClientError()).verifyComplete();
	}

	@Test
	void testUpdate() {
		Producto updated = new Producto(1L, "Producto Updated", 5L, 1L);

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1")
				.body(Mono.just(updated));

		when(useCase.updateProducto(eq(1L), any())).thenReturn(Mono.just(updated));

		Mono<ServerResponse> response = handler.update(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testUpdateStock() {
		Producto updated = new Producto(1L, "Producto Updated", 20L, 1L);

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1")
				.body(Mono.just(updated));

		when(useCase.updateProductoStock(eq(1L), any())).thenReturn(Mono.just(updated));

		Mono<ServerResponse> response = handler.updateStock(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testDelete() {
		when(useCase.deleteProducto(1L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();

		Mono<ServerResponse> response = handler.delete(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testObtenerProductoMayorStockPorSucursalDeFranquicia() {
		ProductoStockPorSucursal stock = new ProductoStockPorSucursal(1L, "Producto", 100, 1L, "Sucursal A");
		when(useCase.obtenerProductoMayorStockPorSucursalDeFranquicia(1L)).thenReturn(Flux.just(stock));

		ServerRequest request = MockServerRequest.builder().pathVariable("idFranquicia", "1").build();

		Mono<ServerResponse> response = handler.obtenerProductoMayorStockPorSucursalDeFranquicia(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}
}