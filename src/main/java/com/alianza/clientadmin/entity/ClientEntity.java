
package com.alianza.clientadmin.entity;

import javax.validation.constraints.NotBlank;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
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
	private String addedDate;

	@JsonProperty("lastModifiedDate")
	private String lastModifiedDate;

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
	public ClientEntity(String sharedKey, String businessId, String email, String phone, String addedDate,
			String lastModifiedDate) {
		super();
		this.sharedKey = sharedKey;
		this.businessId = businessId;
		this.email = email;
		this.phone = phone;
		this.addedDate = addedDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	@JsonIgnore
	public ObjectId getId() {
		return id;
	}

	@JsonIgnore
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
	public String getAddedDate() {
		return addedDate;
	}

	@JsonProperty("addedDate")
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	@JsonProperty("lastModifiedDate")
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	@JsonProperty("lastModifiedDate")
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}