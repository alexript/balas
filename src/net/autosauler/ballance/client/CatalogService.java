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
	 * Adds the record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean addRecord(String catalogname, HashMap<String, Object> map);

	/**
	 * Gets the all records.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @return the all records
	 */
	public Set<Long> getAllRecords(String catalogname);

	/**
	 * Gets the record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @return the record
	 */
	public HashMap<String, Object> getRecord(String catalogname, Long number);

	/**
	 * Gets the records for selection.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @return the records for selection
	 */
	public HashMap<String, Long> getRecordsForSelection(String catalogname);

	/**
	 * Gets the records for view.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @return the records for view
	 */
	public HashMap<Long, String> getRecordsForView(String catalogname);

	/**
	 * Restore record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @return true, if successful
	 */
	public boolean restoreRecord(String catalogname, Long number);

	/**
	 * Trash record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @return true, if successful
	 */
	public boolean trashRecord(String catalogname, Long number);

	/**
	 * Update record.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @param map
	 *            the map
	 * @return true, if successful
	 */
	public boolean updateRecord(String catalogname, Long number,
			HashMap<String, Object> map);
}
