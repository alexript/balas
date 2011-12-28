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
	public boolean create(String docname, HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts);

	/**
	 * Creates the and activate.
	 * 
	 * @param docname
	 *            the docname
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean createAndActivate(String docname,
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts);

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
	 * Gets the.
	 * 
	 * @param docname
	 *            the docname
	 * @param numbers
	 *            the numbers
	 * @return the sets the
	 */
	public Set<HashMap<String, Object>> get(String docname, Set<Long> numbers);

	/**
	 * Gets the all.
	 * 
	 * @param docname
	 *            the docname
	 * @return the all
	 */
	public Set<Long> getAll(String docname, boolean withunactive);

	/**
	 * Gets the table.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param tablename
	 *            the tablename
	 * @return the table
	 */
	public Set<HashMap<String, Object>> getTable(String docname, Long number,
			String tablename);

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
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts);

	/**
	 * Update and activate.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean updateAndActivate(String docname, Long number,
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts);

}
