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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface CatalogService.
 * 
 * @author alexript
 */
@RemoteServiceRelativePath("catalog")
public interface CatalogService extends RemoteService {

	/**
	 * Adds the partner.
	 * 
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean addPartner(HashMap<String, Object> map);

	/**
	 * Gets the all partners.
	 * 
	 * @return the all partners
	 */
	public Set<Long> getAllPartners();

	/**
	 * Gets the partner.
	 * 
	 * @param number
	 *            the number
	 * @return the partner
	 */
	public HashMap<String, Object> getPartner(Long number);

	/**
	 * Restore partner.
	 * 
	 * @param number
	 *            the number
	 * @return true, if successful
	 */
	public boolean restorePartner(Long number);

	/**
	 * Trash partner.
	 * 
	 * @param number
	 *            the number
	 * @return true, if successful
	 */
	public boolean trashPartner(Long number);

	/**
	 * Update partner.
	 * 
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean updatePartner(Long number, HashMap<String, Object> map);
}
