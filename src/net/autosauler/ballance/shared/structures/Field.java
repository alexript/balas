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

package net.autosauler.ballance.shared.structures;

import java.io.Serializable;

import net.autosauler.ballance.shared.datatypes.DataTypes;

/**
 * The Class Field.
 * 
 * @author alexript
 */
public class Field implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4835685036511909581L;

	/** The fieldname. */
	private String fieldname;

	/** The type. */
	private int type;

	/** The defval. */
	private Object defval;

	/** The name. */
	private Name name;

	/** The helper. */
	private String helper;

	/**
	 * Instantiates a new field.
	 */
	public Field(int type) {
		fieldname = "";
		this.type = type;
		defval = null;
		name = new Name();
		helper = "";
	}

	/**
	 * Gets the defval.
	 * 
	 * @return the defval
	 */
	public Object getDefval() {
		return DataTypes.fromMapping(type, defval);
	}

	/**
	 * Gets the fieldname.
	 * 
	 * @return the fieldname
	 */
	public String getFieldname() {
		return fieldname;
	}

	/**
	 * Gets the helper.
	 * 
	 * @return the helper
	 */
	public String getHelper() {
		return helper;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public Name getName() {
		return name;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the defval.
	 * 
	 * @param defval
	 *            the defval to set
	 */
	public void setDefval(Object defval) {
		this.defval = DataTypes.toMapping(type, defval);
	}

	/**
	 * Sets the fieldname.
	 * 
	 * @param fieldname
	 *            the fieldname to set
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	/**
	 * Sets the helper.
	 * 
	 * @param helper
	 *            the helper to set
	 */
	public void setHelper(String helper) {
		this.helper = helper;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(Name name) {
		this.name = name;
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
		this.name.setName(locale, name);
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
