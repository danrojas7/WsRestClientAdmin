package com.alianza.clientadmin.service;

import java.io.IOException;
import java.util.List;

import com.alianza.clientadmin.dto.ClientDTO;

/**
 * @author Daniel Alejandro
 *
 */
public interface ClientService {

	/**
	 * Método POST en el que se inserta a un nuevo cliente en base de datos o en
	 * caché, dependiendo de la configuración del microservicio
	 * 
	 * @param clientDTO Objeto instancia de la clase DTO del cliente con la
	 *                  información del mismo a persistir
	 * @return Instancia del cliente ya persistido en caché o en base de datos según
	 *         configuración del microservicio
	 */
	public ClientDTO addClient(final ClientDTO clientDTO);

	/**
	 * Método utilizado para actualizar la información de un cliente existente
	 * 
	 * @param sharedKey llave del cliente a actualizar en base de datos o en caché
	 *                  según la configuración del microservicio
	 * @param clientDTO DTO de la clase cliente con la nueva información a
	 *                  actualizar del mismo
	 * @return Objeto instancia de la clase DTO de cliente ya persistido en base de
	 *         datos o en la caché según la configuración del microservicio
	 */
	public ClientDTO modifyClient(String sharedKey, ClientDTO clientDTO);

	/**
	 * Método utilizado para consultar todos los clientes almacenados en caché o en
	 * la base de datos
	 * 
	 * @return Listado de clientes con la información de los clientes persistidos en
	 *         caché o en base de datos según la configuración del microservicio
	 */
	public List<ClientDTO> getAllClients();

	/**
	 * Método para obtener un cliente dependiendo del shared key enviado como
	 * parámetro
	 * 
	 * @param sharedKey Shared key del cliente a retornar
	 * @return Cliente coincidente con el shared key enviado, y que se encuentre
	 *         persistido en base de datos o en caché
	 */
	public ClientDTO getClientBySharedKey(String sharedKey);

	/**
	 * Método utilizado para exportar el contenido de todos los clientes almacenados
	 * en la base de datos o en la caché, dependiendo de la configuración del
	 * microservicio, a un archivo de hoja de cálculo de Microsoft Office Excel 2003
	 * o 2007, o a un CSV, dependiendo de la extensión del tipo de archivo requerido
	 * y enviada como parámetro
	 * 
	 * @param fileFormat Formato del archivo a generar (.xls para un archivo de hoja
	 *                   de cáculo de Microsoft Office Excel 2003, .xlsx para un
	 *                   archivo de hoja de cáculo de Microsoft Office Excel 2007,
	 *                   .csv para un archivo de texto separado por comas)
	 * @return ResponseEntity con la data del archivo generado dentro de un
	 *         InputStream, para que se active la descarga mediante un modal de
	 *         descarga al cliente
	 * @throws IOException Si ocurre una excepción inesperada al momento de generar
	 *                     el archivo requerido
	 */
	public byte[] getExportFileClientList(String fileFormat) throws IOException;

	/**
	 * Método utilizado para buscar por uno o varios criterios los clientes con
	 * coinciden con dichos criterios, los criterios null o no enviados se omiten
	 * como filtros de la clase
	 * 
	 * @param qryClientDTO Objeto instancia de la clase DTO de cliente con los
	 *                     atributos a enviar como filtros, para retornar los
	 *                     clientes que coinciden dentro de la base de datos o
	 *                     almacenados en caché
	 * @return Listado de clientes que coinciden con los criterios de entrada
	 *         enviados
	 */
	public List<ClientDTO> searchClientsByCriteria(ClientDTO qryClientDTO);

}
