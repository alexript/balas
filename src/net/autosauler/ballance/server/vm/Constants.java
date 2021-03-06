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

package net.autosauler.ballance.server.vm;

import net.autosauler.ballance.server.model.GlobalSettings;

/**
 * The Class Constants.
 * 
 * @author alexript
 */
public class Constants {

	/** The settings. */
	private final GlobalSettings settings;

	/**
	 * Instantiates a new constants.
	 * 
	 * @param domain
	 *            the domain
	 */
	public Constants(String domain) {
		settings = new GlobalSettings(domain);
	}

	/**
	 * Gets the.
	 * 
	 * @param name
	 *            the name
	 * @param defval
	 *            the defval
	 * @return the int
	 */
	public int get(String name, Integer defval) {
		int i = settings.get(name, defval);
		settings.save();
		return i;
	}

	/**
	 * Gets the.
	 * 
	 * @param name
	 *            the name
	 * @param defval
	 *            the defval
	 * @return the string
	 */
	public String get(String name, String defval) {
		String s = settings.get(name, defval);
		settings.save();
		return s;
	}
}
