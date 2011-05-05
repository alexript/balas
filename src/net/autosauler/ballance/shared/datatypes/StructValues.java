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

	/** The struct. */
	private Structure struct;

	/**
	 * Instantiates a new struct values.
	 * 
	 * @param struct
	 *            the struct
	 */
	public StructValues(Structure struct) {

		fillDefaults(struct);
	}

	/**
	 * Fill defaults.
	 * 
	 * @param struct
	 *            the struct
	 * @param xmldump
	 *            the xmldump
	 */
	private void fillDefaults(Structure struct) {
		this.struct = struct;
		values = new HashMap<String, Object>();

		Set<String> names = struct.getNames();

		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			Object val = struct.getDefValue(name);
			values.put(name, val);
		}
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
	 * Gets the object.
	 * 
	 * @param name
	 *            the name
	 * @return the object
	 */
	public Object getObject(String name) {
		if (struct.containKey(name) && values.containsKey(name)) {
			return struct.toMapping(name, get(name));
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

	/**
	 * Sets the object.
	 * 
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 */
	public void setObject(String name, Object val) {
		if (struct.containKey(name)) {
			set(name, struct.fromMapping(name, val));
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

			sb.append("<record>\n");
			Iterator<String> i = names.iterator();
			while (i.hasNext()) {
				String name = i.next();
				Object val = values.get(name);
				sb.append(" <field name=\"");
				sb.append(name.trim().toLowerCase());
				sb.append("\" value=\"");
				if (val != null) {
					sb.append(DataTypes.toString(struct.getType(name), val));
				}
				sb.append("\"/>\n");
			}

			sb.append("</record>\n");
		}
		return sb.toString();
	}
}
