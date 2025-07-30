/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.producto.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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

	@Column("sucursal_id")
	private Long idSucursal;

	/**
	 * Metodo Constructor
	 * 
	 * @param id
	 * @param nombre
	 * @param stock
	 * @param idSucursal
	 */
	public ProductoEntity(Long id, String nombre, Long stock, Long idSucursal) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.stock = stock;
		this.idSucursal = idSucursal;
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

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

}
