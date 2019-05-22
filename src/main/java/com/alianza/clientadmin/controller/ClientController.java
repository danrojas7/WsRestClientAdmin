package com.alianza.clientadmin.controller;

import static com.alianza.clientadmin.constants.Constants.RESPUESTA_EXITOSA_GENERICA;
import static com.alianza.clientadmin.constants.Constants.RESPUESTA_NO_EXITOSA_GENERICA;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

			respuesta.setEstado(0);
			respuesta.setDescripcion(RESPUESTA_EXITOSA_GENERICA);
			respuesta.setInformacion(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setEstado(0);
			respuesta.setDescripcion(String.format(RESPUESTA_NO_EXITOSA_GENERICA, e.getMessage()));
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

			respuesta.setEstado(0);
			respuesta.setDescripcion(RESPUESTA_EXITOSA_GENERICA);
			respuesta.setInformacion(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setEstado(0);
			respuesta.setDescripcion(String.format(RESPUESTA_NO_EXITOSA_GENERICA, e.getMessage()));
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

			respuesta.setEstado(0);
			respuesta.setDescripcion(RESPUESTA_EXITOSA_GENERICA);
			respuesta.setInformacion(lstClientsInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setEstado(0);
			respuesta.setDescripcion(String.format(RESPUESTA_NO_EXITOSA_GENERICA, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @param sharedKey
	 * @return
	 */
	@CrossOrigin
	@GetMapping("getBySharedKey/{sharedKey}")
	public ResponseEntity<RespuestaServicio> getClientBySharedKey(@Valid @PathVariable("sharedKey") String sharedKey) {
		RespuestaServicio respuesta = null;
		ClientEntity clientInserted = null;

		respuesta = new RespuestaServicio();
		try {
			clientInserted = clientService.getClientBySharedKey(sharedKey);

			respuesta.setEstado(0);
			respuesta.setDescripcion(RESPUESTA_EXITOSA_GENERICA);
			respuesta.setInformacion(clientInserted);
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} catch (Exception e) {
			respuesta.setEstado(0);
			respuesta.setDescripcion(String.format(RESPUESTA_NO_EXITOSA_GENERICA, e.getMessage()));
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
