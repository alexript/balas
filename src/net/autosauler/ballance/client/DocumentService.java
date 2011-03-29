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
 * The Interface DocumentService.
 * 
 * @author alexript
 */
@RemoteServiceRelativePath("document")
public interface DocumentService extends RemoteService {

	/**
	 * Activate.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 */
	public void activate(String docname, Long number);

	/**
	 * Creates the.
	 * 
	 * @param docname
	 *            the docname
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean create(String docname, HashMap<String, Object> map);

	/**
	 * Gets the.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @return the hash map
	 */
	public HashMap<String, Object> get(String docname, Long number);

	/**
	 * Gets the all.
	 * 
	 * @param docname
	 *            the docname
	 * @return the all
	 */
	public Set<Long> getAll(String docname);

	/**
	 * Unactivate.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 */
	public void unactivate(String docname, Long number);

	/**
	 * Update.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean update(String docname, Long number,
			HashMap<String, Object> map);
}
