package com.alianza.clientadmin.controller;

import static com.alianza.clientadmin.constants.Constants.GENERIC_SUCCESS_RESPONSE;
import static com.alianza.clientadmin.constants.Constants.GENERIC_UNSUCCESS_REPONSE;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alianza.clientadmin.component.GenExcelComponent;
import com.alianza.clientadmin.dto.ClientDTO;
import com.alianza.clientadmin.model.ResponseService;
import com.alianza.clientadmin.service.ClientService;

/**
 * Clase controladora en la que se exponen todas las operaciones del API rest
 * desarrollada
 * 
 * @author Daniel Alejandro
 *
 */
@RestController
@RequestMapping("/client")
public class ClientController {

	/**
	 * Objeto instancia de la clase servicio, en el que se invoca los métodos de
	 * negocio del microservicio
	 */
	private final ClientService clientService;

	/**
	 * Objeto instancia de la clase componente, utilizada para generar el archivo de
	 * hoja de cálculo de Microsoft Office Excel
	 */
	private final GenExcelComponent genExcelComponent;

	/**
	 * Constructor de la clase en el que se inicializan los objetos o componentes
	 * que acoplará SpringBoot al momento de inicializar la clase
	 * 
	 * @param clientService     Objeto instancia de la clase servicio, en el que se
	 *                          invoca los métodos de negocio del microservicio
	 * @param genExcelComponent Objeto instancia de la clase componente, utilizada
	 *                          para generar el archivo de hoja de cálculo de
	 *                          Microsoft Office Excel
	 */
	@Autowired
	public ClientController(ClientService clientService, GenExcelComponent genExcelComponent) {
		super();
		this.clientService = clientService;
		this.genExcelComponent = genExcelComponent;
	}

	/**
	 * Método POST en el que se inserta a un nuevo cliente
	 * 
	 * @param client Objeto instancia de la clase DTO del cliente con la información
	 *               del mismo a persistir
	 * @return Instancia del cliente ya persistido en caché o en base de datos según
	 *         configuración del microservicio
	 */
	@CrossOrigin
	@PostMapping
	public ResponseEntity<ResponseService> addClient(@Valid @RequestBody ClientDTO clientDTO) {
		ResponseService respuesta = null;
		ClientDTO clientInserted = null;

		respuesta = new ResponseService();
		try {
			clientInserted = clientService.addClient(clientDTO);

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setDescription(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método utilizado para actualizar la información de un cliente existente
	 * 
	 * @param sharedKey llave del cliente a actualizar en base de datos o en caché
	 *                  según la configuración del microservicio
	 * @param client    DTO de la clase cliente con la nueva información a
	 *                  actualizar del mismo
	 * @return Objeto instancia de la clase DTO de cliente ya persistido en base de
	 *         datos o en la caché según la configuración del microservicio
	 */
	@CrossOrigin
	@PostMapping("/{sharedKey}")
	public ResponseEntity<ResponseService> modifyClient(@Valid @PathVariable("sharedKey") String sharedKey,
			@Valid @RequestBody ClientDTO clientDTO) {
		ResponseService respuesta = null;
		ClientDTO clientInserted = null;

		respuesta = new ResponseService();
		try {
			clientInserted = clientService.modifyClient(sharedKey, clientDTO);

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setDescription(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método utilizado para consultar todos los clientes almacenados en caché o en
	 * la base de datos
	 * 
	 * @return Listado de clientes con la información de los clientes persistidos en
	 *         caché o en base de datos según la configuración del microservicio
	 */
	@CrossOrigin
	@GetMapping
	public ResponseEntity<ResponseService> getAllClients() {
		ResponseService respuesta = null;
		List<ClientDTO> lstClientsInserted = null;

		respuesta = new ResponseService();
		try {
			lstClientsInserted = clientService.getAllClients();

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(lstClientsInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setDescription(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método para obtener un cliente dependiendo del shared key enviado como
	 * parámetro
	 * 
	 * @param sharedKey Shared key del cliente a retornar
	 * @return Cliente coincidente con el shared key enviado, y que se encuentre
	 *         persistido en base de datos o en caché
	 */
	@CrossOrigin
	@GetMapping("/getBySharedKey/{sharedKey}")
	public ResponseEntity<ResponseService> getClientBySharedKey(@Valid @PathVariable("sharedKey") String sharedKey) {
		ResponseService respuesta = null;
		ClientDTO clientInserted = null;

		respuesta = new ResponseService();
		try {
			clientInserted = clientService.getClientBySharedKey(sharedKey);

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setDescription(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método utilizado para exportar el contenido de todos los clientes almacenados
	 * en la base de datos o en la caché, dependiendo de la configuración del
	 * microservicio, a un archivo de hoja de cálculo de Microsoft Office Excel 2003
	 * o 2007, o a un CSV, dependiendo de la extensión del tipo de archivo requerido
	 * y enviada como parámetro
	 * 
	 * @param fileFormat Formato del archivo a generar (.xls para un archivo de hoja
	 *                   de cáculo de Microsoft Office Excel 2003, .xlsx para un
	 *                   archivo de hoja de cáculo de Microsoft Office Excel 2007,
	 *                   .csv para un archivo de texto separado por comas)
	 * @return ResponseEntity con la data del archivo generado dentro de un
	 *         InputStream, para que se active la descarga mediante un modal de
	 *         descarga al cliente
	 */
	@CrossOrigin
	@GetMapping("/getFile/{fileFormat}")
	public ResponseEntity<InputStreamResource> getFile(@Valid @PathVariable("fileFormat") String fileFormat) {
		byte[] baFile = null;
		long contentLength = 0;
		String mediaType = null;
		try {
			if (fileFormat.equalsIgnoreCase("xls")) {
				mediaType = "application/vnd.ms-excel";
			} else if (fileFormat.equalsIgnoreCase("xlsx")) {
				mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			} else if (fileFormat.equalsIgnoreCase("csv")) {
				mediaType = "text/csv";
			} else {
				throw new IllegalArgumentException("Unsupported format file");
			}
			baFile = clientService.getExportFileClientList(fileFormat);
			contentLength = baFile.length;
			return ResponseEntity.ok().contentLength(contentLength).contentType(MediaType.parseMediaType(mediaType))
					.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=export.%s", fileFormat))
					.body(new InputStreamResource(new ByteArrayInputStream(baFile)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Método que genera un archivo de excel a partir de un archivo JSON
	 * 
	 * @param fileFormat      Fomato del archivo de excel a generar
	 * @param lstFileContents Lista de mapas con la información del JSON a generar
	 *                        el archivo
	 * @return ResponseEntity con la data del archivo generado dentro de un
	 *         InputStream, para que se active la descarga mediante un modal de
	 *         descarga al cliente
	 */
	@CrossOrigin
	@PostMapping("/getFileFromJson/{fileFormat}")
	public ResponseEntity<InputStreamResource> getFileFromJson(@Valid @PathVariable("fileFormat") String fileFormat,
			@Valid @RequestBody List<LinkedHashMap<String, Object>> lstFileContents) {
		byte[] baFile = null;
		long contentLength = 0;
		String mediaType = null;

		try {
			if (fileFormat.equalsIgnoreCase("xls")) {
				mediaType = "application/vnd.ms-excel";
			} else if (fileFormat.equalsIgnoreCase("xlsx")) {
				mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			} else {
				throw new IllegalArgumentException("Unsupported format file");
			}

			baFile = genExcelComponent.generarByteArrayArchivoExcel(null, 0, 0, lstFileContents, null, true,
					String.format(".%s", fileFormat), "content", null, false, true);

			contentLength = baFile.length;
			return ResponseEntity.ok().contentLength(contentLength).contentType(MediaType.parseMediaType(mediaType))
					.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=export.%s", fileFormat))
					.body(new InputStreamResource(new ByteArrayInputStream(baFile)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Método utilizado para buscar por uno o varios criterios los clientes con
	 * coinciden con dichos criterios, los criterios null o no enviados se omiten
	 * como filtros de la clase
	 * 
	 * @param qryClientDTO Objeto instancia de la clase DTO de cliente con los
	 *                     atributos a enviar como filtros, para retornar los
	 *                     clientes que coinciden dentro de la base de datos o
	 *                     almacenados en caché
	 * @return Listado de clientes que coinciden con los criterios de entrada
	 *         enviados
	 */
	@CrossOrigin
	@PostMapping("/searchClientsByCriteria")
	public ResponseEntity<ResponseService> searchClientsByCriteria(@RequestBody ClientDTO qryClientDTO) {
		ResponseService respuesta = null;
		List<ClientDTO> lstClientDTO = null;

		respuesta = new ResponseService();
		try {
			lstClientDTO = clientService.searchClientsByCriteria(qryClientDTO);

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(lstClientDTO);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setDescription(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
