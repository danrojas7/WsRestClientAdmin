package com.alianza.clientadmin.constants;

/**
 * @author Daniel Alejandro
 *
 */
public class Constants {

	public static final String RESPUESTA_EXITOSA_GENERICA = "Transacción exitosa";
	public static final String RESPUESTA_NO_EXITOSA_GENERICA = "Transacción NO exitosa, causado por: %s";

	public static final String ERROR_GENERICO = "Error en API de administración de cliente";
	public static final String ERROR_ACCESO_DB = "Error de acceso a la base de datos: %s";
	public static final String ERROR_CONEXION_DB = "Error de conexión a la base de datos: %s";
	public static final String ERROR_ESCRITURA_DB = "Error en la escritura de la base de datos: %s";
	public static final String ERROR_GENERICO_DB = "Error con la base de datos: %s";
	public static final String ERROR_EXCEPCION_GENERAL = "Error general: %s";

	public static final String ERROR_CLIENTE_NO_EXISTE = "El cliente con sharedKey \"%s\" no existe";
	public static final String ERROR_CLIENTE_EXISTE = "El cliente con sharedKey \"%s\" ya existe";

	public Constants() {
		super();
	}
}
