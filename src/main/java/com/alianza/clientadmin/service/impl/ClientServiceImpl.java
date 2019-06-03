package com.alianza.clientadmin.service.impl;

import static com.alianza.clientadmin.constants.Constants.CLIENT_DOES_NOT_EXIST_ERROR;
import static com.alianza.clientadmin.constants.Constants.CLIENT_EXIST_ERROR;
import static com.alianza.clientadmin.constants.Constants.GENERIC_ERROR_ACCESS_DB;
import static com.alianza.clientadmin.constants.Constants.GENERIC_ERROR_CONNECTION_DB;
import static com.alianza.clientadmin.constants.Constants.GENERIC_ERROR_DB;
import static com.alianza.clientadmin.constants.Constants.GENERIC_ERROR_GENERATING_FILE;
import static com.alianza.clientadmin.constants.Constants.GENERIC_ERROR_WRITING_DB;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alianza.clientadmin.cache.ProxyCache;
import com.alianza.clientadmin.component.GenCsvComponent;
import com.alianza.clientadmin.component.GenExcelComponent;
import com.alianza.clientadmin.configuration.ConfigProperties;
import com.alianza.clientadmin.entity.ClientEntity;
import com.alianza.clientadmin.repository.ClientRepository;
import com.alianza.clientadmin.service.ClientService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
	private final ConfigProperties configProperties;

	/**
	 * 
	 */
	private final ProxyCache proxyCache;

	/**
	 * 
	 */
	private final ClientRepository clientRepository;

	/**
	 * 
	 */
	private final GenExcelComponent genExcelComponent;

	/**
	 * 
	 */
	private final GenCsvComponent genCsvComponent;

	/**
	* 
	*/
	private final MongoTemplate mongoTemplate;

	/**
	 * @param configProperties
	 * @param proxyCache
	 * @param clientRepository
	 * @param genExcelComponent
	 * @param genCsvComponent
	 * @param mongoTemplate
	 */
	@Autowired
	public ClientServiceImpl(ConfigProperties configProperties, ProxyCache proxyCache,
			ClientRepository clientRepository, GenExcelComponent genExcelComponent, GenCsvComponent genCsvComponent,
			MongoTemplate mongoTemplate) {
		super();
		this.configProperties = configProperties;
		this.proxyCache = proxyCache;
		this.clientRepository = clientRepository;
		this.genExcelComponent = genExcelComponent;
		this.genCsvComponent = genCsvComponent;
		this.mongoTemplate = mongoTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alianza.clientadmin.service.ClientService#addClient(com.alianza.
	 * clientadmin.entity.ClientEntity)
	 */
	@Override
	public ClientEntity addClient(final ClientEntity client) {
		Optional<ClientEntity> oClientPersisted = null;
		ClientEntity clientPersisted = null;
		try {
			if (configProperties.isEnableCachingSave()) {
				clientPersisted = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(client.getSharedKey())).findAny().orElse(null);
				if (clientPersisted != null) {
					throw new IllegalArgumentException(String.format(CLIENT_EXIST_ERROR, client.getSharedKey()));
				} else {
					client.setAddedDate(new Date());
					client.setLastModifiedDate(null);
					proxyCache.getLstClientEntity().add(client);
					clientPersisted = client;
				}
			} else {
				oClientPersisted = clientRepository.findBySharedKey(client.getSharedKey());
				if (oClientPersisted.isPresent()) {
					throw new IllegalArgumentException(String.format(CLIENT_EXIST_ERROR, client.getSharedKey()));
				} else {
					client.setAddedDate(new Date());
					client.setLastModifiedDate(null);
					clientPersisted = clientRepository.save(client);
				}
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
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
			if (configProperties.isEnableCachingSave()) {
				clientPersisted = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(sharedKey)).findAny().orElse(null);
				if (clientPersisted != null) {
					client.setLastModifiedDate(new Date());
					BeanUtils.copyProperties(client, clientPersisted);
				} else {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			} else {
				oClientPersisted = clientRepository.findBySharedKey(sharedKey);
				if (oClientPersisted.isPresent()) {
					clientPersisted = oClientPersisted.get();
					client.setId(clientPersisted.getId());
					client.setLastModifiedDate(new Date());
					clientPersisted = clientRepository.save(client);
				} else {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}
		return clientPersisted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alianza.clientadmin.service.ClientService#getAllClients()
	 */
	@Override
	public List<ClientEntity> getAllClients() {
		List<ClientEntity> lstClientEntity = null;
		try {
			if (configProperties.isEnableCachingSave()) {
				lstClientEntity = proxyCache.getLstClientEntity();
			} else {
				lstClientEntity = clientRepository.findAll();
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
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
			if (configProperties.isEnableCachingSave()) {
				client = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(sharedKey)).findAny().orElse(null);
				if (client == null) {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			} else {
				oClient = clientRepository.findBySharedKey(sharedKey);
				if (oClient.isPresent()) {
					client = oClient.get();
				} else {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#getExcelExportClientList(java.
	 * lang.String)
	 */
	@Override
	public byte[] getExportFileClientList(String fileFormat) throws IOException {
		byte[] arrayFile = null;
		List<ClientEntity> lstClientEntity = null;
		List<LinkedHashMap<String, Object>> lstFileContents = null;
		ObjectMapper mapper = null;
		Map<String, String> columnFileTitle;
		try {
			if (configProperties.isEnableCachingSave()) {
				lstClientEntity = proxyCache.getLstClientEntity();
			} else {
				lstClientEntity = clientRepository.findAll();
			}
			if (lstClientEntity.isEmpty()) {
				lstClientEntity.add(new ClientEntity(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
						StringUtils.EMPTY, null, null));
			}
			mapper = new ObjectMapper();
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.setDateFormat(new SimpleDateFormat(configProperties.getDefaultDateFormat()));
			lstFileContents = mapper.readValue(mapper.writeValueAsString(lstClientEntity),
					new TypeReference<List<LinkedHashMap<String, Object>>>() {
					});

			columnFileTitle = mapper.readValue(configProperties.getDefaultHeadersExportFile(),
					new TypeReference<LinkedHashMap<String, Object>>() {
					});

			if (fileFormat.equalsIgnoreCase("xls") || fileFormat.equalsIgnoreCase("xlsx")) {
				arrayFile = genExcelComponent.generarByteArrayArchivoExcel(null, new Integer(0), new Integer(0),
						lstFileContents, columnFileTitle, true, String.format(".%s", fileFormat), "content", null,
						false, true);
			} else {
				arrayFile = genCsvComponent.generateCsvFile(lstFileContents, columnFileTitle, true,
						configProperties.getDefaultCsvSeparator(), configProperties.getDefaultCsvQuoteChar(),
						configProperties.getDefaultCsvEscapeChar(), configProperties.getDefaultCsvLineEnd());
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		} catch (JsonParseException e) {
			LOGGER.error(String.format(GENERIC_ERROR_GENERATING_FILE, e.getMessage()));
			throw e;
		} catch (JsonMappingException e) {
			LOGGER.error(String.format(GENERIC_ERROR_GENERATING_FILE, e.getMessage()));
			throw e;
		} catch (JsonProcessingException e) {
			LOGGER.error(String.format(GENERIC_ERROR_GENERATING_FILE, e.getMessage()));
			throw e;
		} catch (IOException e) {
			LOGGER.error(String.format(GENERIC_ERROR_GENERATING_FILE, e.getMessage()));
			throw e;
		}

		return arrayFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#searchClientsByCriteria(com.
	 * alianza.clientadmin.entity.ClientEntity)
	 */
	public List<ClientEntity> searchClientsByCriteria(ClientEntity qryClientEntity) {
		List<ClientEntity> lstClientEntity = null;
		Query query = null;

		try {
			if (configProperties.isEnableCachingSave()) {
				lstClientEntity = new ArrayList<>();
				lstClientEntity.addAll(proxyCache.getLstClientEntity());
				if (StringUtils.isNotEmpty(qryClientEntity.getSharedKey())) {
					lstClientEntity = lstClientEntity.stream().filter(
							p -> p.getSharedKey().toLowerCase().contains(qryClientEntity.getSharedKey().toLowerCase()))
							.collect(Collectors.toList());
				}
				if (StringUtils.isNotEmpty(qryClientEntity.getBusinessId())) {
					lstClientEntity = lstClientEntity.stream()
							.filter(p -> p.getBusinessId().toLowerCase()
									.contains(qryClientEntity.getBusinessId().toLowerCase()))
							.collect(Collectors.toList());
				}
				if (StringUtils.isNotEmpty(qryClientEntity.getEmail())) {
					lstClientEntity = lstClientEntity.stream()
							.filter(p -> p.getEmail().toLowerCase().contains(qryClientEntity.getEmail().toLowerCase()))
							.collect(Collectors.toList());
				}
				if (StringUtils.isNotEmpty(qryClientEntity.getPhone())) {
					lstClientEntity = lstClientEntity.stream()
							.filter(p -> p.getPhone().contains(qryClientEntity.getPhone()))
							.collect(Collectors.toList());
				}
				if (qryClientEntity.getAddedDate() != null && qryClientEntity.getLastModifiedDate() != null) {
					lstClientEntity = lstClientEntity.stream()
							.filter(p -> p.getAddedDate().after(qryClientEntity.getAddedDate())
									&& p.getAddedDate().before(qryClientEntity.getLastModifiedDate()))
							.collect(Collectors.toList());
				}
			} else {
				query = new Query();
				if (StringUtils.isNotEmpty(qryClientEntity.getSharedKey())) {
					query.addCriteria(
							Criteria.where("sharedKey")
									.regex(MongoRegexCreator.INSTANCE.toRegularExpression(
											qryClientEntity.getSharedKey(), MongoRegexCreator.MatchMode.CONTAINING),
											"i"));
				}
				if (StringUtils.isNotEmpty(qryClientEntity.getBusinessId())) {
					query.addCriteria(
							Criteria.where("businessId")
									.regex(MongoRegexCreator.INSTANCE.toRegularExpression(
											qryClientEntity.getBusinessId(), MongoRegexCreator.MatchMode.CONTAINING),
											"i"));
				}
				if (StringUtils.isNotEmpty(qryClientEntity.getEmail())) {
					query.addCriteria(Criteria.where("email").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
							qryClientEntity.getEmail(), MongoRegexCreator.MatchMode.CONTAINING), "i"));
				}
				if (StringUtils.isNotEmpty(qryClientEntity.getPhone())) {
					query.addCriteria(Criteria.where("phone").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
							qryClientEntity.getPhone(), MongoRegexCreator.MatchMode.CONTAINING), "i"));
				}
				if (qryClientEntity.getAddedDate() != null && qryClientEntity.getLastModifiedDate() != null) {
					query.addCriteria(Criteria.where("addedDate").gte(qryClientEntity.getAddedDate())
							.lte(qryClientEntity.getLastModifiedDate()));
				}
				lstClientEntity = mongoTemplate.find(query, ClientEntity.class);
			}
		} catch (DataAccessResourceFailureException e) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause()));
			throw e;
		} catch (MongoSocketOpenException e) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, ((MongoSocketOpenException) e).getCause()));
			throw e;
		} catch (MongoSocketWriteException e) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, ((MongoSocketWriteException) e).getCause()));
			throw e;
		} catch (UncategorizedMongoDbException e) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause()));
			throw e;
		}

		return lstClientEntity;
	}

}
