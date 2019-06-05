package com.alianza.clientadmin.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alianza.clientadmin.entity.ClientEntity;

/**
 * Clase componente que sirve de fachada para invocar los métodos de la clase
 * administradora de la caché
 * 
 * @author Daniel Alejandro
 *
 */
@Component
public class ProxyCache {

	/**
	 * Objeto instancia de la clase de la administradora de la cache de clientes
	 */
	private final CacheManagement cacheManagement;

	/**
	 * Método constructor de la clase en la que se inicializan los componentes a
	 * utilizar, inyectados automáticamente por SpringBoot
	 * 
	 * @param cacheManagement Objeto instancia de la clase administradora de la
	 *                        caché de clientes
	 */
	@Autowired
	public ProxyCache(CacheManagement cacheManagement) {
		super();
		this.cacheManagement = cacheManagement;
	}

	/**
	 * Método que invoca el método de la clase controladora de la caché para obtener
	 * los clientes almacenados en la misma
	 * 
	 * @return Listado de clientes almacenados en la caché de SpringBoot
	 */
	public synchronized List<ClientEntity> getLstClientEntity() {
		return cacheManagement.getLstClientEntity();
	}

	/**
	 * Método que invoca el método init de la clase controladora de la caché, para
	 * inicializar la lista de clientes y la caché de SpringBoot
	 */
	public synchronized void init() {
		cacheManagement.init();
	}

}
