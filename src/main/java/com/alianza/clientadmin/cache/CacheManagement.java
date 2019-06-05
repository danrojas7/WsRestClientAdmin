package com.alianza.clientadmin.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.alianza.clientadmin.entity.ClientEntity;

/**
 * Clase utilizada para almacenar en la caché de SpringBoot la información de
 * los clientes ingresados, siempre cuando la aplicación se encuentre
 * configurada para almacenar en caché, para temas de demostración del
 * microservicio
 * 
 * @author Daniel Alejandro
 *
 */
@Component
public class CacheManagement {

	/**
	 * Clase controladora de la caché
	 */
	@Autowired
	private CacheManager cacheManager;

	/**
	 * Lista de clientes a cachear
	 */
	private List<ClientEntity> lstClientEntity;

	/**
	 * Método que retorna de la caché, la lista de clientes almacenada
	 * 
	 * @return Lista de clientes
	 */
	@Cacheable("lstClientEntity")
	public List<ClientEntity> getLstClientEntity() {
		return this.lstClientEntity;
	}

	/**
	 * Método que limpia de la caché, la lista de los clientes ya almacenados
	 */
	@CacheEvict(value = "lstClientEntity", allEntries = true)
	private void clearLstClientEntity() {
		Cache cache = cacheManager.getCache("lstClientEntity");
		if (cache != null) {
			cache.clear();
		}
	}

	/**
	 * Método que inicializa la lista de los clientes, para ello crea una nueva
	 * instancia de la lista de clientes, e invoca el método que limpia de la caché
	 * la lista existente
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		this.lstClientEntity = new ArrayList<>();
		clearLstClientEntity();
	}

}
