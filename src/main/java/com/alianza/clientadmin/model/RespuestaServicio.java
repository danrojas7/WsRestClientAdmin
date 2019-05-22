package com.alianza.clientadmin.model;

/**
 * @author Daniel Alejandro
 *
 */
public class RespuestaServicio {

	private int estado;
	private String descripcion;
	private Object informacion;

	public RespuestaServicio() {
		super();
	}

	public final int getEstado() {
		return estado;
	}

	public final void setEstado(int estado) {
		this.estado = estado;
	}

	public final String getDescripcion() {
		return descripcion;
	}

	public final void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public final Object getInformacion() {
		return informacion;
	}

	public final void setInformacion(Object informacion) {
		this.informacion = informacion;
	}

}
