package com.alianza.clientadmin.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daniel Alejandro
 *
 */
@Configuration
@ConfigurationProperties(prefix = "com.alianza.clientadmin.parametro")
public class ConfigProperties {

	private boolean habilitarGuardadoCache;

	public ConfigProperties() {
		super();
	}

	public boolean isHabilitarGuardadoCache() {
		return habilitarGuardadoCache;
	}

	public void setHabilitarGuardadoCache(boolean habilitarGuardadoCache) {
		this.habilitarGuardadoCache = habilitarGuardadoCache;
	}

}
