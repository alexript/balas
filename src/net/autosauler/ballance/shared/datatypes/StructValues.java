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
import java.util.Iterator;
import java.util.Set;

/**
 * The Class StructValues.
 * 
 * @author alexript
 */
public class StructValues {

	/** The values. */
	private HashMap<String, Object> values;

	/**
	 * Instantiates a new struct values.
	 * 
	 * @param struct
	 *            the struct
	 */
	public StructValues(Structure struct) {
		fillDefaults(struct, null);
	}

	/**
	 * Instantiates a new struct values.
	 * 
	 * @param struct
	 *            the struct
	 * @param xmldump
	 *            the xmldump
	 */
	public StructValues(Structure struct, String xmldump) {
		fillDefaults(struct, xmldump);
	}

	/**
	 * Fill defaults.
	 * 
	 * @param struct
	 *            the struct
	 * @param xmldump
	 *            the xmldump
	 */
	private void fillDefaults(Structure struct, String xmldump) {
		values = new HashMap<String, Object>();

		Set<String> names = struct.getNames();

		if (xmldump == null) {
			Iterator<String> i = names.iterator();
			while (i.hasNext()) {
				String name = i.next();
				Object val = struct.getDefValue(name);
				values.put(name, val);
			}
		} else {
			fromString(names, xmldump);
		}
	}

	/**
	 * From string.
	 * 
	 * @param names
	 *            the names
	 * @param xml
	 *            the xml
	 */
	private void fromString(Set<String> names, String xml) {
		// TODO: parse xml
	}

	/**
	 * Gets the.
	 * 
	 * @param name
	 *            the name
	 * @return the object
	 */
	public Object get(String name) {
		if (values.containsKey(name)) {
			return values.get(name);
		}
		return null;
	}

	/**
	 * Sets the.
	 * 
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 */
	public void set(String name, Object val) {
		if (values.containsKey(name)) {
			values.put(name, val);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<String> names = values.keySet();
		if (names.size() > 0) {

			sb.append("<record>");
			Iterator<String> i = names.iterator();
			while (i.hasNext()) {
				String name = i.next();
				Object val = values.get(name);
				sb.append(" <field name=\"");
				sb.append(name.trim().toLowerCase());
				sb.append("\" value=\"");
				sb.append(val.toString());
				sb.append("\"/>\n");
			}

			sb.append("</record>");
		}
		return sb.toString();
	}
}
