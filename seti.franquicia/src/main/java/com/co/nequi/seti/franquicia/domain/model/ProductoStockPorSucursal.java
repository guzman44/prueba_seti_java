/**
 * 
 */
package com.co.nequi.seti.franquicia.domain.model;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class ProductoStockPorSucursal {

	private Long idProducto;
	private String nombreProducto;
	private Integer stock;
	private Long idSucursal;
	private String nombreSucursal;

	public ProductoStockPorSucursal(Long idProducto, String nombreProducto, Integer stock, Long idSucursal,
			String nombreSucursal) {
		super();
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.stock = stock;
		this.idSucursal = idSucursal;
		this.nombreSucursal = nombreSucursal;
	}

	public ProductoStockPorSucursal() {
		super();
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

}
