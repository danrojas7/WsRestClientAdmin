package com.alianza.clientadmin.model;

/**
 * Clase de respuesta estándar del servicio
 * 
 * @author Daniel Alejandro
 *
 */
public class ResponseService {

	private int status;
	private String description;
	private Object information;

	/**
	 * Constructor de la clase
	 */
	public ResponseService() {
		super();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getInformation() {
		return information;
	}

	public void setInformation(Object information) {
		this.information = information;
	}

}
