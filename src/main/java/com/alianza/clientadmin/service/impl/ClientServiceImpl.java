package com.alianza.clientadmin.service.impl;

import static com.alianza.clientadmin.constants.Constants.ERROR_ACCESO_DB;
import static com.alianza.clientadmin.constants.Constants.ERROR_CLIENTE_EXISTE;
import static com.alianza.clientadmin.constants.Constants.ERROR_CLIENTE_NO_EXISTE;
import static com.alianza.clientadmin.constants.Constants.ERROR_CONEXION_DB;
import static com.alianza.clientadmin.constants.Constants.ERROR_ESCRITURA_DB;
import static com.alianza.clientadmin.constants.Constants.ERROR_GENERICO_DB;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.stereotype.Service;

import com.alianza.clientadmin.cache.ProxyCache;
import com.alianza.clientadmin.configuration.ConfigProperties;
import com.alianza.clientadmin.entity.ClientEntity;
import com.alianza.clientadmin.repository.ClientRepository;
import com.alianza.clientadmin.service.ClientService;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoSocketWriteException;

/**
 * @author Daniel Alejandro
 *
 */
@Service
public class ClientServiceImpl implements ClientService {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(ClientServiceImpl.class);

	/**
	 * 
	 */
	@Autowired
	private ConfigProperties configProperties;

	/**
	 * 
	 */
	@Autowired
	private ProxyCache proxyCache;

	/**
	 * 
	 */
	@Autowired
	public ClientRepository clientRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alianza.clientadmin.service.ClientService#createClient(com.alianza.
	 * clientadmin.entity.ClientEntity)
	 */
	@Override
	public ClientEntity createClient(final ClientEntity client) {
		Optional<ClientEntity> oClientPersisted = null;
		ClientEntity clientPersisted = null;
		try {
			if (configProperties.isHabilitarGuardadoCache()) {
				clientPersisted = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(client.getSharedKey())).findAny().orElse(null);
				if (clientPersisted != null) {
					throw new IllegalArgumentException(String.format(ERROR_CLIENTE_EXISTE, client.getSharedKey()));
				} else {
					proxyCache.getLstClientEntity().add(client);
					clientPersisted = client;
				}
			} else {
				oClientPersisted = clientRepository.findBySharedKey(client.getSharedKey());
				if (oClientPersisted.isPresent()) {
					throw new IllegalArgumentException(String.format(ERROR_CLIENTE_EXISTE, client.getSharedKey()));
				} else {
					clientPersisted = clientRepository.save(client);
				}
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(
					String.format(ERROR_ACCESO_DB, ((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(ERROR_CONEXION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(ERROR_ESCRITURA_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(ERROR_GENERICO_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}
		return clientPersisted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#modifyClient(java.lang.String,
	 * com.alianza.clientadmin.entity.ClientEntity)
	 */
	@Override
	public ClientEntity modifyClient(String sharedKey, ClientEntity client) {
		Optional<ClientEntity> oClientPersisted = null;
		ClientEntity clientPersisted = null;
		try {
			if (configProperties.isHabilitarGuardadoCache()) {
				clientPersisted = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(sharedKey)).findAny().orElse(null);
				if (clientPersisted != null) {
					BeanUtils.copyProperties(client, clientPersisted);
				} else {
					throw new IllegalArgumentException(String.format(ERROR_CLIENTE_NO_EXISTE, sharedKey));
				}
			} else {
				oClientPersisted = clientRepository.findBySharedKey(sharedKey);
				if (oClientPersisted.isPresent()) {
					clientPersisted = oClientPersisted.get();
					client.setId(clientPersisted.getId());
					client = clientRepository.save(client);
				} else {
					throw new IllegalArgumentException(String.format(ERROR_CLIENTE_NO_EXISTE, sharedKey));
				}
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(
					String.format(ERROR_ACCESO_DB, ((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(ERROR_CONEXION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(ERROR_ESCRITURA_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(ERROR_GENERICO_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alianza.clientadmin.service.ClientService#queryAllClients()
	 */
	@Override
	public List<ClientEntity> queryAllClients() {
		List<ClientEntity> lstClientEntity = null;
		try {
			if (configProperties.isHabilitarGuardadoCache()) {
				lstClientEntity = proxyCache.getLstClientEntity();
			} else {
				lstClientEntity = clientRepository.findAll();
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(
					String.format(ERROR_ACCESO_DB, ((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(ERROR_CONEXION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(ERROR_ESCRITURA_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(ERROR_GENERICO_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}
		return lstClientEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#queryClientBySharedKey(java.
	 * lang.String)
	 */
	@Override
	public ClientEntity getClientBySharedKey(String sharedKey) {
		Optional<ClientEntity> oClient = null;
		ClientEntity client = null;
		try {
			if (configProperties.isHabilitarGuardadoCache()) {
				client = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(sharedKey)).findAny().orElse(null);
				if (client == null) {
					throw new IllegalArgumentException(String.format(ERROR_CLIENTE_NO_EXISTE, sharedKey));
				}
			} else {
				oClient = clientRepository.findBySharedKey(sharedKey);
				if (oClient.isPresent()) {
					client = oClient.get();
				} else {
					throw new IllegalArgumentException(String.format(ERROR_CLIENTE_NO_EXISTE, sharedKey));
				}
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(
					String.format(ERROR_ACCESO_DB, ((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(ERROR_CONEXION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(ERROR_ESCRITURA_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(ERROR_GENERICO_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alianza.clientadmin.service.ClientService#getExcelExportClientList()
	 */
	@Override
	public byte[] getExcelExportClientList() {
		// TODO Auto-generated method stub
		return null;
	}

}
