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
	 * @param client
	 * @return
	 */
	public ClientDTO addClient(final ClientDTO clientDTO);

	/**
	 * @param sharedKey
	 * @param client
	 * @return
	 */
	public ClientDTO modifyClient(String sharedKey, ClientDTO clientDTO);

	/**
	 * @return
	 */
	public List<ClientDTO> getAllClients();

	/**
	 * @param sharedKey
	 * @return
	 */
	public ClientDTO getClientBySharedKey(String sharedKey);

	/**
	 * @param fileFormat
	 * @return
	 * @throws IOException
	 */
	public byte[] getExportFileClientList(String fileFormat) throws IOException;

	/**
	 * @param qryClientDTO
	 * @return
	 */
	public List<ClientDTO> searchClientsByCriteria(ClientDTO qryClientDTO);

}
