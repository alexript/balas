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
 * The Interface ScriptsService.
 * 
 * @author alexript
 */
@RemoteServiceRelativePath("scripts")
public interface ScriptsService extends RemoteService {

	/**
	 * Eval script on server.
	 * 
	 * @param domain
	 *            the domain
	 * @param scriptname
	 *            the scriptname
	 * @param params
	 *            the params
	 * @return the hash map
	 */
	public HashMap<String, String> eval(String scriptname, String evalstring,
			HashMap<String, String> params, HashMap<String, Integer> types);

	/**
	 * @param scriptname
	 * @param evalstring
	 * @param changedfield
	 * @param params
	 * @param types
	 * @return
	 */
	public HashMap<String, String> evalOnChange(String scriptname,
			String evalstring, String changedfield,
			HashMap<String, String> params, HashMap<String, Integer> types);

	/**
	 * @param scriptname
	 * @param evalstring
	 * @param tablename
	 * @param changedfield
	 * @param params
	 * @param types
	 * @return
	 */
	public HashMap<String, String> evalOnChangeTable(String scriptname,
			String evalstring, String tablename, String changedfield,
			HashMap<String, String> params, HashMap<String, Integer> types);

	/**
	 * Gets the script.
	 * 
	 * @param scriptname
	 *            the scriptname
	 * @return the script
	 */
	public String getScript(String scriptname);

	/**
	 * Save script.
	 * 
	 * @param scriptname
	 *            the scriptname
	 * @param script
	 *            the script
	 * @return the boolean
	 */
	public Boolean saveScript(String scriptname, String script);
}
