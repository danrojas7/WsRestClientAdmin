package com.alianza.clientadmin.service;

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
	public ClientEntity createClient(ClientEntity client);

	/**
	 * @param sharedKey
	 * @param client
	 * @return
	 */
	public ClientEntity modifyClient(String sharedKey, ClientEntity client);

	/**
	 * @return
	 */
	public List<ClientEntity> queryAllClients();

	/**
	 * @param sharedKey
	 * @return
	 */
	public ClientEntity getClientBySharedKey(String sharedKey);

	/**
	 * @return
	 */
	public byte[] getExcelExportClientList();

}
