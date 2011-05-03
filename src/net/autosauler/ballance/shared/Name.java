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

package net.autosauler.ballance.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class Name.
 * 
 * @author alexript
 */
public class Name implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4497185194138699286L;
	/** The i18n. */
	private Map<String, String> i18n = new HashMap<String, String>();

	/**
	 * Instantiates a new name.
	 */
	public Name() {
	}

	/**
	 * Gets the name.
	 * 
	 * @param locale
	 *            the locale
	 * @return the name
	 */
	public String getName(String locale) {
		return i18n.get(locale);
	}

	/**
	 * Sets the name.
	 * 
	 * @param locale
	 *            the locale
	 * @param name
	 *            the name
	 */
	public void setName(String locale, String name) {
		i18n.put(locale, name);
	}

	/**
	 * Sets the names.
	 * 
	 * @param names
	 *            the names
	 */
	public void setNames(HashMap<String, String> names) {
		i18n = names;
	}
}
