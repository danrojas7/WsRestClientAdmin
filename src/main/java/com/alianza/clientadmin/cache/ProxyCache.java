package com.alianza.clientadmin.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alianza.clientadmin.entity.ClientEntity;
import com.alianza.clientadmin.cache.CacheManagement;

/**
 * @author Daniel Alejandro
 *
 */
@Component
public class ProxyCache {

	/**
	 * 
	 */
	@Autowired
	private CacheManagement cacheManagement;

	/**
	 * @return
	 */
	public synchronized List<ClientEntity> getLstClientEntity() {
		return cacheManagement.getLstClientEntity();
	}

	/**
	 * 
	 */
	public synchronized void init() {
		cacheManagement.init();
	}

}
