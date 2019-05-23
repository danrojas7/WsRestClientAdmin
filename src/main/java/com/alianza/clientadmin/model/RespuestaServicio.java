package com.alianza.clientadmin.model;

/**
 * @author Daniel Alejandro
 *
 */
public class RespuestaServicio {

	private int status;
	private String description;
	private Object information;

	public RespuestaServicio() {
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
