package com.distribuidora.searchengine.dto;

public class DenunciaDato {
	private Integer id;
	private Integer nroprev;
	private String fechahecho;
	private String localidad;
	private String dato;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNroprev() {
		return nroprev;
	}
	public void setNroprev(Integer nroprev) {
		this.nroprev = nroprev;
	}
	public String getFechahecho() {
		return fechahecho;
	}
	public void setFechahecho(String fechahecho) {
		this.fechahecho = fechahecho;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getDato() {
		return dato;
	}
	public void setDato(String dato) {
		this.dato = dato;
	}
	public DenunciaDato(Integer id, Integer nroprev, String fechahecho, String localidad, String dato) {
	
		this.id = id;
		this.nroprev = nroprev;
		this.fechahecho = fechahecho;
		this.localidad = localidad;
		this.dato = dato;
	}
	
	public DenunciaDato() {
		super();
	}
	
}
