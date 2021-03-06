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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface DatabaseServiceAsync.
 */
public interface DatabaseServiceAsync {

	/**
	 * Drop database.
	 * 
	 * @param callback
	 *            the callback
	 */
	void dropDatabase(AsyncCallback<Boolean> callback);

	/**
	 * Dump database.
	 * 
	 * @param filename
	 *            the filename
	 * @param callback
	 *            the callback
	 */
	void dumpDatabase(String filename, AsyncCallback<Void> callback);

	/**
	 * Gets the settings.
	 * 
	 * @param callback
	 *            the callback
	 */
	void getSettings(AsyncCallback<HashMap<String, String>> callback);

	/**
	 * Restore database.
	 * 
	 * @param filename
	 *            the filename
	 * @param callback
	 *            the callback
	 */
	void restoreDatabase(String filename, AsyncCallback<Void> callback);

	/**
	 * Sets the settings.
	 * 
	 * @param newvalues
	 *            the newvalues
	 * @param callback
	 *            the callback
	 */
	void setSettings(HashMap<String, String> newvalues,
			AsyncCallback<Void> callback);

}
