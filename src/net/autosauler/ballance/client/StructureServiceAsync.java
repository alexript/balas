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
import java.util.List;

import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Dummy;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface StructureServiceAsync.
 * 
 * @author alexript
 */
public interface StructureServiceAsync {

	void get(String name, AsyncCallback<String> callback);

	/**
	 * Gets the all.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all
	 */
	void getAll(AsyncCallback<HashMap<String, Description>> callback);

	/**
	 * Gets the dummy.
	 * 
	 * @param callback
	 *            the callback
	 * @return the dummy
	 */
	void getDummy(AsyncCallback<Dummy> callback);

	void getHelp(String locale, String name, AsyncCallback<String> callback);

	void getHelpNames(AsyncCallback<List<String>> callback);

	/**
	 * Gets the structure description.
	 * 
	 * @param name
	 *            the name
	 * @param callback
	 *            the callback
	 * @return the structure description
	 */
	void getStructureDescription(String name,
			AsyncCallback<Description> callback);

	void save(String name, String text, AsyncCallback<Void> callback);

	void saveHelp(String name, HashMap<String, String> texts,
			AsyncCallback<Void> callback);

}
