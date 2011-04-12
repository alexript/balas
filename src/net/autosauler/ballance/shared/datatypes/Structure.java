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

package net.autosauler.ballance.shared.datatypes;

import java.util.HashMap;
import java.util.Set;

/**
 * The Class Structure.
 * 
 * @author alexript
 */
public class Structure {

	/** The struct. */
	private final HashMap<String, StructElement> struct;

	/**
	 * Instantiates a new structure.
	 */
	public Structure() {
		struct = new HashMap<String, StructElement>();
	}

	/**
	 * Adds the.
	 * 
	 * @param name
	 *            the name
	 * @param datatype
	 *            the datatype
	 * @param defvalue
	 *            the defvalue
	 */
	public void add(String name, int datatype, Object defvalue) {
		StructElement element = new StructElement(datatype, defvalue);
		struct.put(name, element);
	}

	/**
	 * Gets the def value.
	 * 
	 * @param name
	 *            the name
	 * @return the def value
	 */
	public Object getDefValue(String name) {
		if (struct.containsKey(name)) {
			StructElement element = struct.get(name);
			Object val = element.getDefVal();
			return val;
		} else {
			return null;
		}
	}

	/**
	 * Gets the names.
	 * 
	 * @return the names
	 */
	public Set<String> getNames() {
		return struct.keySet();
	}

	/**
	 * Gets the type.
	 * 
	 * @param name
	 *            the name
	 * @return the type
	 */
	public int getType(String name) {
		if (struct.containsKey(name)) {
			StructElement element = struct.get(name);
			int type = element.getType();
			return type;
		} else {
			return DataTypes.DT_OBJECT;
		}
	}
}
