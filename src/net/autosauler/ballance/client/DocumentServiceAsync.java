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
 * The Interface DocumentServiceAsync.
 * 
 * @author alexript
 */
public interface DocumentServiceAsync {

	/**
	 * Activate.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.DocumentService#activate(java.lang.String,
	 *      java.lang.Long)
	 */
	void activate(String docname, Long number, AsyncCallback<Void> callback);

	/**
	 * Creates the.
	 * 
	 * @param docname
	 *            the docname
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 */
	void create(String docname, HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts,
			AsyncCallback<Boolean> callback);

	/**
	 * Creates the and activate.
	 * 
	 * @param docname
	 *            the docname
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 */
	void createAndActivate(String docname, HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts,
			AsyncCallback<Boolean> callback);

	/**
	 * Gets the.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.DocumentService#get(java.lang.String,
	 *      java.lang.Long)
	 */
	void get(String docname, Long number,
			AsyncCallback<HashMap<String, Object>> callback);

	/**
	 * Gets the.
	 * 
	 * @param docname
	 *            the docname
	 * @param numbers
	 *            the numbers
	 * @param callback
	 *            the callback
	 */
	void get(String docname, Set<Long> numbers,
			AsyncCallback<Set<HashMap<String, Object>>> callback);

	/**
	 * Gets the all.
	 * 
	 * @param docname
	 *            the docname
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.DocumentService#getAll(java.lang.String)
	 */
	void getAll(String docname, AsyncCallback<Set<Long>> callback);

	/**
	 * Gets the table.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param tablename
	 *            the tablename
	 * @param callback
	 *            the callback
	 */
	void getTable(String docname, Long number, String tablename,
			AsyncCallback<Set<HashMap<String, Object>>> callback);

	/**
	 * Unactivate.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.DocumentService#unactivate(java.lang.String,
	 *      java.lang.Long)
	 */
	void unactivate(String docname, Long number, AsyncCallback<Void> callback);

	/**
	 * Update.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 */
	void update(String docname, Long number, HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts,
			AsyncCallback<Boolean> callback);

	/**
	 * Update and activate.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 */
	void updateAndActivate(String docname, Long number,
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts,
			AsyncCallback<Boolean> callback);

	/**
	 * Update table.
	 * 
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 * @param tablename
	 *            the tablename
	 * @param set
	 *            the set
	 * @param callback
	 *            the callback
	 */
	void updateTable(String docname, Long number, String tablename,
			Set<HashMap<String, Object>> set, AsyncCallback<Void> callback);

}
