package com.alianza.clientadmin.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Clase DTO de cliente, en la que se establecen los atributos de mapeo a o
 * desde JSON, y los validadores utilizados en SpringBoot para los campos de
 * entrada requeridos
 * 
 * @author Daniel Alejandro
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "sharedKey", "businessId", "email", "phone", "addedDate", "lastModifiedDate" })
public class ClientDTO {

	@NotBlank(message = "Shared Key is required")
	@JsonProperty("sharedKey")
	private String sharedKey;

	@NotBlank(message = "Business Id is required")
	@JsonProperty("businessId")
	private String businessId;

	@NotBlank(message = "E-Mail is required")
	@JsonProperty("email")
	private String email;

	@NotBlank(message = "Phone Number is required")
	@JsonProperty("phone")
	private String phone;

	@JsonProperty("addedDate")
	private Date addedDate;

	@JsonProperty("lastModifiedDate")
	private Date lastModifiedDate;

	/**
	 * Constructor de la clase
	 */
	public ClientDTO() {
		super();
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param addedDate
	 * @param phone
	 * @param email
	 * @param lastModifiedDate
	 * @param businessId
	 * @param sharedKey
	 */
	public ClientDTO(String sharedKey, String businessId, String email, String phone, Date addedDate,
			Date lastModifiedDate) {
		super();
		this.sharedKey = sharedKey;
		this.businessId = businessId;
		this.email = email;
		this.phone = phone;
		this.addedDate = addedDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	@JsonProperty("sharedKey")
	public String getSharedKey() {
		return sharedKey;
	}

	@JsonProperty("sharedKey")
	public void setSharedKey(String sharedKey) {
		this.sharedKey = sharedKey;
	}

	@JsonProperty("businessId")
	public String getBusinessId() {
		return businessId;
	}

	@JsonProperty("businessId")
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}

	@JsonProperty("phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonProperty("addedDate")
	public Date getAddedDate() {
		return addedDate;
	}

	@JsonProperty("addedDate")
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	@JsonProperty("lastModifiedDate")
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	@JsonProperty("lastModifiedDate")
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}