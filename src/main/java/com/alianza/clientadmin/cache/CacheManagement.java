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
	 * 
	 */
	private List<ClientEntity> lstClientEntity;

	/**
	 * @return
	 */
	@Cacheable("lstClientEntity")
	public List<ClientEntity> getLstClientEntity() {
		return this.lstClientEntity;
	}

	/**
	 * 
	 */
	@CacheEvict(value = "lstClientEntity", allEntries = true)
	private void clearLstClientEntity() {
		Cache cache = cacheManager.getCache("lstClientEntity");
		if (cache != null) {
			cache.clear();
		}
	}

	/**
	 * 
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		this.lstClientEntity = new ArrayList<>();
		clearLstClientEntity();
	}

}
