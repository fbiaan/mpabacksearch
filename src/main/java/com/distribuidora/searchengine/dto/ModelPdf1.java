package com.distribuidora.searchengine.dto;

public class ModelPdf1 {
	private String nombreArchivo;
	private String pathArchivo;
	private String respuesta;
	private String numeroPaginas;
	private String lugarEncontrado;
	
	
	
	public ModelPdf1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getPathArchivo() {
		return pathArchivo;
	}
	public void setPathArchivo(String pathArchivo) {
		this.pathArchivo = pathArchivo;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public String getNumeroPaginas() {
		return numeroPaginas;
	}
	public void setNumeroPaginas(String numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}
	public String getLugarEncontrado() {
		return lugarEncontrado;
	}
	public void setLugarEncontrado(String lugarEncontrado) {
		this.lugarEncontrado = lugarEncontrado;
	}
	
	
	
}
