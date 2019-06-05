package com.alianza.clientadmin.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.alianza.clientadmin.entity.ClientEntity;

/**
 * Clase repository en la que se administra mediante CRUD los registros de
 * clientes en la base de datos de MongoDB, entre otros métodos definidos
 * explícitamente
 * 
 * @author Daniel Alejandro
 *
 */
@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, ObjectId> {

	/**
	 * Método para obtener un cliente dependiendo del shared key enviado como
	 * parámetro
	 * 
	 * @param sharedKey Shared key del cliente a retornar
	 * @return Cliente coincidente con el shared key enviado, y que se encuentre
	 *         persistido en base de datos
	 */
	public Optional<ClientEntity> findBySharedKey(String sharedKey);

}
