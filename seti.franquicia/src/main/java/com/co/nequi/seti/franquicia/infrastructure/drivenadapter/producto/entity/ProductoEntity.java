/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */

@Table("producto")
public class ProductoEntity {

	@Id
	private Long id;
	private String nombre;
	private Long stock;
	private Long sucursal_id;

	/**
	 * Metodo Constructor
	 * 
	 * @param id
	 * @param nombre
	 * @param stock
	 * @param sucursal_id
	 */
	public ProductoEntity(Long id, String nombre, Long stock, Long sucursal_id) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.stock = stock;
		this.sucursal_id = sucursal_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Long getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(Long sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

}
