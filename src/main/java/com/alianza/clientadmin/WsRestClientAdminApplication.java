package com.alianza.clientadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.alianza.clientadmin.configuration.ConfigProperties;

/**
 * Clase principal utilizada por SpringBoot para inicializar el microservicio
 * 
 * @author Daniel Alejandro
 *
 */
@Configuration
@EnableConfigurationProperties(ConfigProperties.class)
@EnableCaching
@SpringBootApplication
public class WsRestClientAdminApplication {

	/**
	 * Método principal que ejecuta el microservicio dentro del contexto de
	 * SpringBoot
	 * 
	 * @param args Argumentos enviados al microservicio en la consola
	 */
	public static void main(String[] args) {
		SpringApplication.run(WsRestClientAdminApplication.class, args);
	}

}
