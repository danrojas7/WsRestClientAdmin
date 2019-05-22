package com.alianza.clientadmin.configuration;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Clase de configuración de la librería Swagger para realizar documentación y
 * pruebas del microservicio
 * 
 * @author Daniel Alejandro
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	/**
	 * Método que inicializa el contexto de inicialización de Swagger, con la
	 * respectiva ruta de paquetes del proyecto principal
	 * 
	 * @return Docket con la configuración inicializada del Swagger
	 */
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.alianza.clientadmin")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	/**
	 * Método que realiza la inicialización del ApiInfo del Swagger con la
	 * descripción del Api
	 * 
	 * @return ApiInfo con la descripción del API
	 */
	private ApiInfo apiInfo() {
		return new ApiInfo("Microservicio para la administración de clientes",
				"Microservicio de API Rest en el que se definen los métodos CRUD para la administración de clientes, como parte de la prueba técnica para Alianza",
				"1.0.0", StringUtils.EMPTY, null, StringUtils.EMPTY, StringUtils.EMPTY, Collections.emptyList());
	}
}
