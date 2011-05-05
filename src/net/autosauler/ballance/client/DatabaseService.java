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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface DatabaseService.
 */
@RemoteServiceRelativePath("database")
public interface DatabaseService extends RemoteService {

	/**
	 * Drop database.
	 * 
	 * @return true, if successful
	 */
	public boolean dropDatabase();

	/**
	 * Dump database.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void dumpDatabase(String filename);

	/**
	 * Gets the settings.
	 * 
	 * @return the settings
	 */
	public HashMap<String, String> getSettings();

	/**
	 * Restore database.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void restoreDatabase(String filename);

	/**
	 * Sets the settings.
	 * 
	 * @param newvalues
	 *            the newvalues
	 */
	public void setSettings(HashMap<String, String> newvalues);
}
