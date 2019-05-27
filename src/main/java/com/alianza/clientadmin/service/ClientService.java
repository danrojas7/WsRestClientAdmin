package com.alianza.clientadmin.service;

import java.io.IOException;
import java.util.List;

import com.alianza.clientadmin.entity.ClientEntity;

/**
 * @author Daniel Alejandro
 *
 */
public interface ClientService {

	/**
	 * @param client
	 * @return
	 */
	public ClientEntity addClient(final ClientEntity client);

	/**
	 * @param sharedKey
	 * @param client
	 * @return
	 */
	public ClientEntity modifyClient(String sharedKey, ClientEntity client);

	/**
	 * @return
	 */
	public List<ClientEntity> getAllClients();

	/**
	 * @param sharedKey
	 * @return
	 */
	public ClientEntity getClientBySharedKey(String sharedKey);

	/**
	 * @param fileFormat
	 * @return
	 * @throws IOException
	 */
	public byte[] getExportFileClientList(String fileFormat) throws IOException;

}
