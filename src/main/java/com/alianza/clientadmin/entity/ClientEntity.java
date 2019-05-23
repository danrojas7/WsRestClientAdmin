
package com.alianza.clientadmin.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Daniel Alejandro
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "sharedKey", "businessId", "email", "phone", "addedDate", "lastModifiedDate" })
@Document(collection = "alianza_cp_clientes")
public class ClientEntity {

	@Id
	private ObjectId id;

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
	 * No args constructor for use in serialization
	 *
	 */
	public ClientEntity() {
		super();
	}

	/**
	 *
	 * @param addedDate
	 * @param phone
	 * @param email
	 * @param lastModifiedDate
	 * @param businessId
	 * @param sharedKey
	 */
	public ClientEntity(String sharedKey, String businessId, String email, String phone, Date addedDate,
			Date lastModifiedDate) {
		super();
		this.sharedKey = sharedKey;
		this.businessId = businessId;
		this.email = email;
		this.phone = phone;
		this.addedDate = addedDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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