/**
 * 
 */
package com.co.nequi.seti.franquicia.domain.model;

import java.util.List;

/**
 * @author MarkoPortatil
 * @version 1.0
 * @since 29 Julio 2025
 */
public class Sucursal {

	private Long id;
	private String nombre;
	private String direccion;
	private Long idFranquicia;
	private List<Producto> productos;

	/**
	 * Metodo Constructor
	 * 
	 * @param id
	 * @param nombre
	 * @param direccion
	 * @param idFranquicia
	 */
	public Sucursal(Long id, String nombre, String direccion, Long idFranquicia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.idFranquicia = idFranquicia;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Long getIdFranquicia() {
		return idFranquicia;
	}

	public void setIdFranquicia(Long idFranquicia) {
		this.idFranquicia = idFranquicia;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

}