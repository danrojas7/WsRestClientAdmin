package com.alianza.clientadmin.constants;

/**
 * @author Daniel Alejandro
 *
 */
public class Constants {

	public static final String RESPUESTA_EXITOSA_GENERICA = "Success Transaction";
	public static final String RESPUESTA_NO_EXITOSA_GENERICA = "Transaction Unsuccess, caused by: %s";

	public static final String ERROR_GENERICO = "Error in API of Client Management";
	public static final String ERROR_ACCESO_DB = "Database access error: %s";
	public static final String ERROR_CONEXION_DB = "Database conection error: %s";
	public static final String ERROR_ESCRITURA_DB = "Database write error: %s";
	public static final String ERROR_GENERICO_DB = "Database error: %s";
	public static final String ERROR_EXCEPCION_GENERAL = "General Error : %s";

	public static final String ERROR_CLIENTE_NO_EXISTE = "Client by sharedKey \"%s\" doesn't exists";
	public static final String ERROR_CLIENTE_EXISTE = "Client by sharedKey \"%s\" exists";

	public Constants() {
		super();
	}
}
