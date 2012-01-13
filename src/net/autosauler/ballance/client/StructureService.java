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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface StructureService.
 * 
 * @author alexript
 */
@RemoteServiceRelativePath("structure")
public interface StructureService extends RemoteService {

	public String get(String name);

	/**
	 * Gets the all.
	 * 
	 * @return the all
	 */
	public HashMap<String, Description> getAll();

	/**
	 * Gets the dummy.
	 * 
	 * @return the dummy
	 */
	public Dummy getDummy();

	public String getHelp(String locale, String name);

	public List<String> getHelpNames();

	/**
	 * Gets the structure description.
	 * 
	 * @param name
	 *            the name
	 * @return the structure description
	 */
	public Description getStructureDescription(String name);

	public void save(String name, String text);

	public void saveHelp(String name, HashMap<String, String> texts);
}
