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
	 * Adds the record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#addRecord(java.util.HashMap)
	 */
	void addRecord(String catalogname, HashMap<String, Object> map,
			AsyncCallback<Boolean> callback);

	/**
	 * Gets the all records.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param callback
	 *            the callback
	 * @return the all records
	 * @see net.autosauler.ballance.client.CatalogService#getAllRecords()
	 */
	void getAllRecords(String catalogname, AsyncCallback<Set<Long>> callback);

	/**
	 * Gets the record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @return the record
	 * @see net.autosauler.ballance.client.CatalogService#getRecord(java.lang.Long)
	 */
	void getRecord(String catalogname, Long number,
			AsyncCallback<HashMap<String, Object>> callback);

	/**
	 * Gets the records for selection.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param callback
	 *            the callback
	 * @return the records for selection
	 */
	void getRecordsForSelection(String catalogname,
			AsyncCallback<HashMap<String, Long>> callback);

	/**
	 * Gets the records for view.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param callback
	 *            the callback
	 * @return the records for view
	 */
	void getRecordsForView(String catalogname,
			AsyncCallback<HashMap<Long, String>> callback);

	/**
	 * Restore record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#restoreRecord(java.lang.Long)
	 */
	void restoreRecord(String catalogname, Long number,
			AsyncCallback<Boolean> callback);

	/**
	 * Trash record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#trashRecord(java.lang.Long)
	 */
	void trashRecord(String catalogname, Long number,
			AsyncCallback<Boolean> callback);

	/**
	 * Update record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CatalogService#updateRecord(java.lang.Long,
	 *      java.util.HashMap)
	 */
	void updateRecord(String catalogname, Long number,
			HashMap<String, Object> map, AsyncCallback<Boolean> callback);

}
