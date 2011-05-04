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
	 * Instantiates a new structure.
	 * 
	 * @param xmldescription
	 *            the xmldescription
	 */
	public Structure(String xmldescription) {
		struct = new HashMap<String, StructElement>();
		fromString(xmldescription);
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
	 * Contain key.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	public boolean containKey(String name) {
		return struct.containsKey(name);
	}

	/**
	 * From mapping.
	 * 
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 * @return the object
	 */
	public Object fromMapping(String name, Object val) {
		Object obj = null;
		int type = getType(name);
		obj = DataTypes.fromMapping(type, val);
		return obj;
	}

	/**
	 * From string.
	 * 
	 * @param xml
	 *            the xml
	 */
	private void fromString(String xml) {
		// TODO: xml parse
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

	/**
	 * To mapping.
	 * 
	 * @param name
	 *            the name
	 * @param object
	 *            the object
	 * @return the object
	 */
	public Object toMapping(String name, Object object) {
		Object obj = null;
		if (object != null) {
			int type = getType(name);
			obj = DataTypes.toMapping(type, object);
		}
		return obj;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		Set<String> names = getNames();
		if (names.size() > 0) {
			sb.append("<fields>\n");
			Iterator<String> i = names.iterator();
			while (i.hasNext()) {
				String name = i.next();
				StructElement element = struct.get(name);
				sb.append(" <field name=\"");
				sb.append(name.trim().toLowerCase());
				sb.append("\" type=\"");
				sb.append(element.getType());
				sb.append("\" default=\"");
				sb.append(toMapping(name, element.getDefVal()).toString());
				sb.append("\"/>\n");
			}
			sb.append("</fields>\n");
		}
		return sb.toString();
	}
}
