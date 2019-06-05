package com.alianza.clientadmin.constants;

/**
 * Clase de constantes definidas para el microservicio
 * 
 * @author Daniel Alejandro
 *
 */
public class Constants {

	public static final String GENERIC_SUCCESS_RESPONSE = "Success Transaction";
	public static final String GENERIC_UNSUCCESS_REPONSE = "Transaction Unsuccess, caused by: %s";

	public static final String GENERIC_ERROR = "Error in API of Client Management";
	public static final String GENERIC_ERROR_ACCESS_DB = "Database access error: %s, stacktrace:\n%s";
	public static final String GENERIC_ERROR_CONNECTION_DB = "Database conection error: %s, stacktrace:\n%s";
	public static final String GENERIC_ERROR_WRITING_DB = "Database write error: %s, stacktrace:\n%s";
	public static final String GENERIC_ERROR_DB = "Database error: %s, stacktrace:\n%s";
	public static final String GENERAL_EXCEPTION_ERROR = "General Error : %s, stacktrace:\n%s";
	public static final String GENERIC_ERROR_GENERATING_FILE = "Error while creating file to download: %s, stacktrace:\n%s";

	public static final String CLIENT_DOES_NOT_EXIST_ERROR = "Client by sharedKey \"%s\" doesn't exists";
	public static final String CLIENT_EXIST_ERROR = "Client by sharedKey \"%s\" exists";

	/**
	 * Constructor de la clase
	 */
	private Constants() {
		super();
	}
}
