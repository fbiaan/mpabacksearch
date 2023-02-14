package com.distribuidora.searchengine.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="internosMPA")
public class Denunciagrilla {
	@Id
	private Integer id;
	private Integer lugar;
	private String interno;
	private String detalle;
	
	public Denunciagrilla() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLugar() {
		return lugar;
	}

	public void setLugar(Integer lugar) {
		this.lugar = lugar;
	}

	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	
	
}