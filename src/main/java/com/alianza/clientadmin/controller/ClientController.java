package com.alianza.clientadmin.controller;

import static com.alianza.clientadmin.constants.Constants.GENERIC_SUCCESS_RESPONSE;
import static com.alianza.clientadmin.constants.Constants.GENERIC_UNSUCCESS_REPONSE;

import java.io.ByteArrayInputStream;
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

import com.alianza.clientadmin.entity.ClientEntity;
import com.alianza.clientadmin.model.RespuestaServicio;
import com.alianza.clientadmin.service.ClientService;

/**
 * @author Daniel Alejandro
 *
 */
@RestController
@RequestMapping("/client")
public class ClientController {

	/**
	 * 
	 */
	@Autowired
	private ClientService clientService;

	/**
	 * @param client
	 * @return
	 */
	@CrossOrigin
	@PostMapping
	public ResponseEntity<RespuestaServicio> addClient(@Valid @RequestBody ClientEntity client) {
		RespuestaServicio respuesta = null;
		ClientEntity clientInserted = null;

		respuesta = new RespuestaServicio();
		try {
			clientInserted = clientService.addClient(client);

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
	 * @param sharedKey
	 * @param client
	 * @return
	 */
	@CrossOrigin
	@PostMapping("/{sharedKey}")
	public ResponseEntity<RespuestaServicio> modifyClient(@Valid @PathVariable("sharedKey") String sharedKey,
			@Valid @RequestBody ClientEntity client) {
		RespuestaServicio respuesta = null;
		ClientEntity clientInserted = null;

		respuesta = new RespuestaServicio();
		try {
			clientInserted = clientService.modifyClient(sharedKey, client);

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setInformation(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @return
	 */
	@CrossOrigin
	@GetMapping
	public ResponseEntity<RespuestaServicio> getAllClients() {
		RespuestaServicio respuesta = null;
		List<ClientEntity> lstClientsInserted = null;

		respuesta = new RespuestaServicio();
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
	 * @param sharedKey
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/getBySharedKey/{sharedKey}")
	public ResponseEntity<RespuestaServicio> getClientBySharedKey(@Valid @PathVariable("sharedKey") String sharedKey) {
		RespuestaServicio respuesta = null;
		ClientEntity clientInserted = null;

		respuesta = new RespuestaServicio();
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
	 * @param fileFormat
	 * @return
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
				throw new Exception("Unsupported format file");
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
	 * @param qryClientEntity
	 * @return
	 */
	@CrossOrigin
	@PostMapping("/searchClientsByCriteria")
	public ResponseEntity<RespuestaServicio> searchClientsByCriteria(@RequestBody ClientEntity qryClientEntity) {
		RespuestaServicio respuesta = null;
		List<ClientEntity> lstClientEntity = null;

		respuesta = new RespuestaServicio();
		try {
			lstClientEntity = clientService.searchClientsByCriteria(qryClientEntity);

			respuesta.setStatus(0);
			respuesta.setDescription(GENERIC_SUCCESS_RESPONSE);
			respuesta.setInformation(lstClientEntity);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setStatus(1);
			respuesta.setDescription(String.format(GENERIC_UNSUCCESS_REPONSE, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
