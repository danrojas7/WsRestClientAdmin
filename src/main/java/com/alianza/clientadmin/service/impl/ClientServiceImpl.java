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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
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
import com.alianza.clientadmin.dto.ClientDTO;
import com.alianza.clientadmin.entity.ClientEntity;
import com.alianza.clientadmin.repository.ClientRepository;
import com.alianza.clientadmin.service.ClientService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoSocketWriteException;

/**
 * Clase servicio en la que se define la lógica de negocio del microservicio de
 * administración de clientes
 * 
 * @author Daniel Alejandro
 *
 */
@Service
public class ClientServiceImpl implements ClientService {

	/**
	 * Logger del Log4j inicializado para la clase actual
	 */
	private static final Logger LOGGER = LogManager.getLogger(ClientServiceImpl.class);

	/**
	 * Objeto instancia de la clase de mapeo de la configuración del microservicio
	 */
	private final ConfigProperties configProperties;

	/**
	 * Objeto instancia de la clase componente que sirve como fachada para acceder a
	 * los métodos de la caché de clientes de SpringBoot
	 */
	private final ProxyCache proxyCache;

	/**
	 * Objeto instancia de la clase repositorio en la que se define la lógica de
	 * persistencia y el CRUD del cliente en la base de datos de MongoDB
	 */
	private final ClientRepository clientRepository;

	/**
	 * Objeto instancia de la clase componente, utilizada para generar el archivo de
	 * hoja de cálculo de Microsoft Office Excel
	 */
	private final GenExcelComponent genExcelComponent;

	/**
	 * Objeto instancia de la clase componente, utilizada para generar el archivo de
	 * texto separado por comas
	 */
	private final GenCsvComponent genCsvComponent;

	/**
	 * Objeto de la clase MongoTemplate, utilizada para la ejecución de consultas
	 * hacia la colección de clientes, almacenada en la base de datos de MongoDB
	 */
	private final MongoTemplate mongoTemplate;

	/**
	 * Constructor de la clase en el que se inicializa las clases repositorios,
	 * componentes automáticamente acopladas por SpringBoot
	 * 
	 * @param configProperties  Objeto instancia de la clase de mapeo de la
	 *                          configuración del microservicio
	 * @param proxyCache        Objeto instancia de la clase componente que sirve
	 *                          como fachada para acceder a los métodos de la caché
	 *                          de clientes de SpringBoot
	 * @param clientRepository  Objeto instancia de la clase repositorio en la que
	 *                          se define la lógica de persistencia y el CRUD del
	 *                          cliente en la base de datos de MongoDB
	 * @param genExcelComponent Objeto instancia de la clase componente, utilizada
	 *                          para generar el archivo de hoja de cálculo de
	 *                          Microsoft Office Excel
	 * @param genCsvComponent   Objeto instancia de la clase componente, utilizada
	 *                          para generar el archivo de texto separado por comas
	 * @param mongoTemplate     Objeto de la clase MongoTemplate, utilizada para la
	 *                          ejecución de consultas hacia la colección de
	 *                          clientes, almacenada en la base de datos de MongoDB
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
	 * clientadmin.dto.ClientDTO)
	 */
	@Override
	public ClientDTO addClient(final ClientDTO clientDTO) {
		Optional<ClientEntity> oClientPersisted = null;
		ClientEntity clientEntity = null;
		try {
			if (configProperties.isEnableCachingSave()) {
				clientEntity = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(clientDTO.getSharedKey())).findAny()
						.orElse(null);
				if (clientEntity != null) {
					throw new IllegalArgumentException(String.format(CLIENT_EXIST_ERROR, clientDTO.getSharedKey()));
				} else {
					clientDTO.setAddedDate(new Date());
					clientDTO.setLastModifiedDate(null);
					clientEntity = new ClientEntity();
					BeanUtils.copyProperties(clientDTO, clientEntity);
					proxyCache.getLstClientEntity().add(clientEntity);
				}
			} else {
				oClientPersisted = clientRepository.findBySharedKey(clientDTO.getSharedKey());
				if (oClientPersisted.isPresent()) {
					throw new IllegalArgumentException(String.format(CLIENT_EXIST_ERROR, clientDTO.getSharedKey()));
				} else {
					clientDTO.setAddedDate(new Date());
					clientDTO.setLastModifiedDate(null);
					clientEntity = new ClientEntity();
					BeanUtils.copyProperties(clientDTO, clientEntity);
					clientEntity = clientRepository.save(clientEntity);
					BeanUtils.copyProperties(clientEntity, clientDTO);
				}
			}
		} catch (DataAccessResourceFailureException | MongoSocketOpenException | MongoSocketWriteException
				| UncategorizedMongoDbException e) {
			logsMongoDBExceptions(e);
			throw e;
		}
		return clientDTO;
	}

	/**
	 * Método usado para escribir en el log4j la traza de error con el detalle y el
	 * stacktrace, dependiendo del tipo de excepción de la base de datos de MongoDB,
	 * con una descripción más genérica que indica el error que se produjo
	 * 
	 * @param e Excepción a escribir en Log
	 */
	private void logsMongoDBExceptions(Exception e) {
		if (e instanceof DataAccessResourceFailureException) {
			LOGGER.error(String.format(GENERIC_ERROR_ACCESS_DB,
					((DataAccessResourceFailureException) e).getMostSpecificCause(), ExceptionUtils.getStackTrace(e)));
		} else if (e instanceof MongoSocketOpenException) {
			LOGGER.error(String.format(GENERIC_ERROR_CONNECTION_DB, e.getCause(), ExceptionUtils.getStackTrace(e)));
		} else if (e instanceof MongoSocketWriteException) {
			LOGGER.error(String.format(GENERIC_ERROR_WRITING_DB, e.getCause(), ExceptionUtils.getStackTrace(e)));
		} else if (e instanceof UncategorizedMongoDbException) {
			LOGGER.error(String.format(GENERIC_ERROR_DB, ((UncategorizedMongoDbException) e).getMostSpecificCause(),
					ExceptionUtils.getStackTrace(e)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#modifyClient(java.lang.String,
	 * com.alianza.clientadmin.dto.ClientDTO)
	 */
	@Override
	public ClientDTO modifyClient(String sharedKey, ClientDTO clientDTO) {
		Optional<ClientEntity> oClientPersisted = null;
		ClientEntity clientPersisted = null;
		Optional<ClientEntity> oSharedKeyToUpdate = null;
		ClientEntity sharedKeyToUpdate = null;
		ObjectId oId = null;
		try {
			if (configProperties.isEnableCachingSave()) {
				clientPersisted = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(sharedKey)).findAny().orElse(null);
				if (clientPersisted != null) {
					if (!sharedKey.equalsIgnoreCase(clientDTO.getSharedKey())) {
						sharedKeyToUpdate = proxyCache.getLstClientEntity().stream()
								.filter(p -> p.getSharedKey().equalsIgnoreCase(clientDTO.getSharedKey())).findAny()
								.orElse(null);
						if (sharedKeyToUpdate != null) {
							throw new IllegalArgumentException(
									String.format(CLIENT_EXIST_ERROR, clientDTO.getSharedKey()));
						}
					}
					clientDTO.setLastModifiedDate(new Date());
					BeanUtils.copyProperties(clientDTO, clientPersisted);
				} else {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			} else {
				oClientPersisted = clientRepository.findBySharedKey(sharedKey);
				if (oClientPersisted.isPresent()) {
					if (!sharedKey.equalsIgnoreCase(clientDTO.getSharedKey())) {
						oSharedKeyToUpdate = clientRepository.findBySharedKey(clientDTO.getSharedKey());
						if (oSharedKeyToUpdate.isPresent()) {
							throw new IllegalArgumentException(
									String.format(CLIENT_EXIST_ERROR, clientDTO.getSharedKey()));
						}
					}
					clientPersisted = oClientPersisted.get();
					oId = clientPersisted.getId();
					clientDTO.setLastModifiedDate(new Date());
					BeanUtils.copyProperties(clientDTO, clientPersisted);
					clientPersisted.setId(oId);
					clientRepository.save(clientPersisted);
				} else {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			}
		} catch (DataAccessResourceFailureException | MongoSocketOpenException | MongoSocketWriteException
				| UncategorizedMongoDbException e) {
			logsMongoDBExceptions(e);
			throw e;
		}
		return clientDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alianza.clientadmin.service.ClientService#getAllClients()
	 */
	@Override
	public List<ClientDTO> getAllClients() {
		List<ClientEntity> lstClientEntity = null;
		List<ClientDTO> lstClientDTO = null;
		ClientDTO auxClientDTO = null;
		try {
			if (configProperties.isEnableCachingSave()) {
				lstClientEntity = proxyCache.getLstClientEntity();
			} else {
				lstClientEntity = clientRepository.findAll();
			}
			lstClientDTO = new ArrayList<>(lstClientEntity.size());
			for (ClientEntity clientEntity : lstClientEntity) {
				auxClientDTO = new ClientDTO();
				BeanUtils.copyProperties(clientEntity, auxClientDTO);
				lstClientDTO.add(auxClientDTO);
			}
		} catch (DataAccessResourceFailureException | MongoSocketOpenException | MongoSocketWriteException
				| UncategorizedMongoDbException e) {
			logsMongoDBExceptions(e);
			throw e;
		}
		return lstClientDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#getClientBySharedKey(java.lang.
	 * String)
	 */
	@Override
	public ClientDTO getClientBySharedKey(String sharedKey) {
		Optional<ClientEntity> oClient = null;
		ClientEntity clientEntity = null;
		ClientDTO clientDTO = null;
		try {
			if (configProperties.isEnableCachingSave()) {
				clientEntity = proxyCache.getLstClientEntity().stream()
						.filter(p -> p.getSharedKey().equalsIgnoreCase(sharedKey)).findAny().orElse(null);
				if (clientEntity == null) {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
				clientDTO = new ClientDTO();
				BeanUtils.copyProperties(clientEntity, clientDTO);
			} else {
				oClient = clientRepository.findBySharedKey(sharedKey);
				if (oClient.isPresent()) {
					clientEntity = oClient.get();
					clientDTO = new ClientDTO();
					BeanUtils.copyProperties(clientEntity, clientDTO);
				} else {
					throw new IllegalArgumentException(String.format(CLIENT_DOES_NOT_EXIST_ERROR, sharedKey));
				}
			}
		} catch (DataAccessResourceFailureException | MongoSocketOpenException | MongoSocketWriteException
				| UncategorizedMongoDbException e) {
			logsMongoDBExceptions(e);
			throw e;
		}
		return clientDTO;
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
				arrayFile = genExcelComponent.generarByteArrayArchivoExcel(null, 0, 0, lstFileContents, columnFileTitle,
						true, String.format(".%s", fileFormat), "content", null, false, true);
			} else {
				arrayFile = genCsvComponent.generateCsvFile(lstFileContents, columnFileTitle, true,
						configProperties.getDefaultCsvSeparator(), configProperties.getDefaultCsvQuoteChar(),
						configProperties.getDefaultCsvEscapeChar(), configProperties.getDefaultCsvLineEnd());
			}
		} catch (DataAccessResourceFailureException | MongoSocketOpenException | MongoSocketWriteException
				| UncategorizedMongoDbException e) {
			logsMongoDBExceptions(e);
			throw e;
		} catch (IOException e) {
			LOGGER.error(String.format(GENERIC_ERROR_GENERATING_FILE, e.getMessage(), ExceptionUtils.getStackTrace(e)));
			throw e;
		}
		return arrayFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alianza.clientadmin.service.ClientService#searchClientsByCriteria(com.
	 * alianza.clientadmin.dto.ClientDTO)
	 */
	@Override
	public List<ClientDTO> searchClientsByCriteria(ClientDTO qryClientDTO) {
		List<ClientEntity> lstClientEntity = null;
		List<ClientDTO> lstClientDTO = null;
		ClientDTO auxClientDTO = null;

		try {
			if (configProperties.isEnableCachingSave()) {
				lstClientEntity = filterCollectionByCriteria(qryClientDTO);
			} else {
				lstClientEntity = filterMongoDBCollectionByCriteria(qryClientDTO);
			}
			lstClientDTO = new ArrayList<>(lstClientEntity.size());
			for (ClientEntity clientEntity : lstClientEntity) {
				auxClientDTO = new ClientDTO();
				BeanUtils.copyProperties(clientEntity, auxClientDTO);
				lstClientDTO.add(auxClientDTO);
			}
		} catch (DataAccessResourceFailureException | MongoSocketOpenException | MongoSocketWriteException
				| UncategorizedMongoDbException e) {
			logsMongoDBExceptions(e);
			throw e;
		}

		return lstClientDTO;
	}

	/**
	 * Método auxiliar para filtrar de la colección almacenada en la caché de
	 * clientes implementada con SpringBoot, de acuerdo a los atributos enviados
	 * 
	 * @param qryClientDTO Objeto instancia de la clase DTO de clientes con los
	 *                     filtros a utilizar
	 * @return Listado de clientes almacenados en caché que coincidan con los datos
	 *         enviados
	 */
	private List<ClientEntity> filterCollectionByCriteria(ClientDTO qryClientDTO) {
		List<ClientEntity> lstClientEntity = null;

		lstClientEntity = new ArrayList<>();
		lstClientEntity.addAll(proxyCache.getLstClientEntity());
		if (StringUtils.isNotEmpty(qryClientDTO.getSharedKey())) {
			lstClientEntity = lstClientEntity.stream()
					.filter(p -> p.getSharedKey().toLowerCase().contains(qryClientDTO.getSharedKey().toLowerCase()))
					.collect(Collectors.toList());
		}
		if (StringUtils.isNotEmpty(qryClientDTO.getBusinessId())) {
			lstClientEntity = lstClientEntity.stream()
					.filter(p -> p.getBusinessId().toLowerCase().contains(qryClientDTO.getBusinessId().toLowerCase()))
					.collect(Collectors.toList());
		}
		if (StringUtils.isNotEmpty(qryClientDTO.getEmail())) {
			lstClientEntity = lstClientEntity.stream()
					.filter(p -> p.getEmail().toLowerCase().contains(qryClientDTO.getEmail().toLowerCase()))
					.collect(Collectors.toList());
		}
		if (StringUtils.isNotEmpty(qryClientDTO.getPhone())) {
			lstClientEntity = lstClientEntity.stream().filter(p -> p.getPhone().contains(qryClientDTO.getPhone()))
					.collect(Collectors.toList());
		}
		if (qryClientDTO.getAddedDate() != null && qryClientDTO.getLastModifiedDate() != null) {
			lstClientEntity = lstClientEntity.stream()
					.filter(p -> p.getAddedDate().after(qryClientDTO.getAddedDate())
							&& p.getAddedDate().before(qryClientDTO.getLastModifiedDate()))
					.collect(Collectors.toList());
		}

		return lstClientEntity;
	}

	/**
	 * Método auxiliar utilizado para construir los filtros, mediante los campos
	 * enviados, para realizar la consulta de clientes que coincidan almacenados en
	 * la base datos de MongoDB
	 * 
	 * @param qryClientDTO Objeto instancia de la clase DTO de clientes con los
	 *                     filtros a utilizar
	 * @return Listado de clientes almacenados en la base de datos que coincidan con
	 *         los datos enviados
	 */
	private List<ClientEntity> filterMongoDBCollectionByCriteria(ClientDTO qryClientDTO) {
		Query query = null;

		query = new Query();
		if (StringUtils.isNotEmpty(qryClientDTO.getSharedKey())) {
			query.addCriteria(Criteria.where("sharedKey").regex(MongoRegexCreator.INSTANCE
					.toRegularExpression(qryClientDTO.getSharedKey(), MongoRegexCreator.MatchMode.CONTAINING), "i"));
		}
		if (StringUtils.isNotEmpty(qryClientDTO.getBusinessId())) {
			query.addCriteria(Criteria.where("businessId").regex(MongoRegexCreator.INSTANCE
					.toRegularExpression(qryClientDTO.getBusinessId(), MongoRegexCreator.MatchMode.CONTAINING), "i"));
		}
		if (StringUtils.isNotEmpty(qryClientDTO.getEmail())) {
			query.addCriteria(Criteria.where("email").regex(MongoRegexCreator.INSTANCE
					.toRegularExpression(qryClientDTO.getEmail(), MongoRegexCreator.MatchMode.CONTAINING), "i"));
		}
		if (StringUtils.isNotEmpty(qryClientDTO.getPhone())) {
			query.addCriteria(Criteria.where("phone").regex(MongoRegexCreator.INSTANCE
					.toRegularExpression(qryClientDTO.getPhone(), MongoRegexCreator.MatchMode.CONTAINING), "i"));
		}
		if (qryClientDTO.getAddedDate() != null && qryClientDTO.getLastModifiedDate() != null) {
			query.addCriteria(Criteria.where("addedDate").gte(qryClientDTO.getAddedDate())
					.lte(qryClientDTO.getLastModifiedDate()));
		}
		return mongoTemplate.find(query, ClientEntity.class);
	}

}
