/**
 * 
 */
package com.co.nequi.seti.franquicia.infrastructure.drivenadapter.franquicia.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
@Table("franquicia")
public class FranquiciaEntity {

	@Id
	private Long id;
	private String nombre;

	/**
	 * Metodo Constructor
	 * 
	 * @param id
	 * @param nombre
	 */
	public FranquiciaEntity(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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

}
