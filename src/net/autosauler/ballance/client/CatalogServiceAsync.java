/*******************************************************************************
 * Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.autosauler.ballance.client;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface CatalogServiceAsync.
 * 
 * @author alexript
 */
public interface CatalogServiceAsync {

	/**
	 * Adds the partner.
	 * 
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#addPartner(java.util.HashMap)
	 */
	void addPartner(HashMap<String, Object> map, AsyncCallback<Boolean> callback);

	/**
	 * Gets the all partners.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all partners
	 * @see net.autosauler.ballance.client.CatalogService#getAllPartners()
	 */
	void getAllPartners(AsyncCallback<Set<Long>> callback);

	/**
	 * Gets the partner.
	 * 
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @return the partner
	 * @see net.autosauler.ballance.client.CatalogService#getPartner(java.lang.Long)
	 */
	void getPartner(Long number, AsyncCallback<HashMap<String, Object>> callback);

	/**
	 * Restore partner.
	 * 
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#restorePartner(java.lang.Long)
	 */
	void restorePartner(Long number, AsyncCallback<Boolean> callback);

	/**
	 * Trash partner.
	 * 
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#trashPartner(java.lang.Long)
	 */
	void trashPartner(Long number, AsyncCallback<Boolean> callback);

	/**
	 * Update partner.
	 * 
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#updatePartner(java.lang.Long,
	 *      java.util.HashMap)
	 */
	void updatePartner(Long number, HashMap<String, Object> map,
			AsyncCallback<Boolean> callback);

}
