
package com.alianza.clientadmin.entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Clase Entity con los atributos de la colección en MongoDB, a almacenar dicha
 * información en la base de datos
 * 
 * @author Daniel Alejandro
 *
 */
@Document(collection = "alianza_cp_clientes")
public class ClientEntity {

	@Id
	private ObjectId id;
	private String sharedKey;
	private String businessId;
	private String email;
	private String phone;
	private Date addedDate;
	private Date lastModifiedDate;

	/**
	 * Constructor de la clase
	 *
	 */
	public ClientEntity() {
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

	public String getSharedKey() {
		return sharedKey;
	}

	public void setSharedKey(String sharedKey) {
		this.sharedKey = sharedKey;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}