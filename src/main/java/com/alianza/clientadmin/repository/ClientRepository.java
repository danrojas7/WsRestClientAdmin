package com.alianza.clientadmin.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.alianza.clientadmin.entity.ClientEntity;

@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, ObjectId> {

	public Optional<ClientEntity> findBySharedKey(String sharedKey);
}
