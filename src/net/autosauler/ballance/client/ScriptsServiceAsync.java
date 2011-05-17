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
 * The Interface ScriptsServiceAsync.
 * 
 * @author alexript
 */
public interface ScriptsServiceAsync {

	/**
	 * Eval script on server.
	 * 
	 * @param domain
	 *            the domain
	 * @param scriptname
	 *            the scriptname
	 * @param params
	 *            the params
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.ScriptsService#eval(java.lang.String,
	 *      java.lang.String, java.util.HashMap)
	 */
	void eval(String scriptname, String evalstring,
			HashMap<String, String> params, HashMap<String, Integer> types,
			AsyncCallback<HashMap<String, String>> callback);

	/**
	 * Gets the script.
	 * 
	 * @param scriptname
	 *            the scriptname
	 * @param callback
	 *            the callback
	 * @return the script
	 */
	void getScript(String scriptname, AsyncCallback<String> callback);

	/**
	 * Save script.
	 * 
	 * @param scriptname
	 *            the scriptname
	 * @param script
	 *            the script
	 * @param callback
	 *            the callback
	 */
	void saveScript(String scriptname, String script,
			AsyncCallback<Boolean> callback);

}
