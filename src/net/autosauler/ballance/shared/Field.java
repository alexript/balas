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
	private int type = DataTypes.DT_OBJECT;

	/** The defval. */
	private String defval;

	/** The name. */
	private Name name;

	/** The helper. */
	private String helper;

	/** The helpertype. */
	private String helpertype;

	/** The columnwidth. */
	private int columnwidth;

	/** The visible. */
	private boolean visible = true;

	/**
	 * Instantiates a new field.
	 */
	public Field() {

	}

	/**
	 * Instantiates a new field.
	 * 
	 * @param type
	 *            the type
	 */
	public Field(int type) {
		fieldname = "";
		this.type = type;
		defval = null;
		name = new Name();
		helper = "";
		helpertype = "";
		setColumnwidth(10);
		visible = true;
	}

	/**
	 * Gets the columnwidth.
	 * 
	 * @return the columnwidth
	 */
	public int getColumnwidth() {
		return columnwidth;
	}

	/**
	 * Gets the defval.
	 * 
	 * @return the defval
	 */
	public Object getDefval() {
		return DataTypes.fromString(type, defval);
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
	 * Gets the helpertype.
	 * 
	 * @return the helpertype
	 */
	public String getHelpertype() {
		return helpertype;
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
	 * Checks if is visible.
	 * 
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the columnwidth.
	 * 
	 * @param columnwidth
	 *            the columnwidth to set
	 */
	public void setColumnwidth(int columnwidth) {
		this.columnwidth = columnwidth;
	}

	/**
	 * Sets the defval.
	 * 
	 * @param defval
	 *            the defval to set
	 */
	public void setDefval(Object defval) {
		this.defval = DataTypes.toString(type, defval);
	}

	/**
	 * Sets the defval as string.
	 * 
	 * @param defval
	 *            the new defval as string
	 */
	public void setDefvalAsString(String defval) {
		this.defval = defval;
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
	 * Sets the helpertype.
	 * 
	 * @param helpertype
	 *            the helpertype to set
	 */
	public void setHelpertype(String helpertype) {
		this.helpertype = helpertype;
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

	/**
	 * Sets the visible.
	 * 
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Fieldname: " + fieldname + "\n");
		sb.append("type: " + type + "\n");
		sb.append("defval: " + defval + "\n");
		sb.append("helper: " + helper + "\n");
		sb.append("helpertype: " + helpertype + "\n");
		sb.append("Names: \n");
		sb.append(name.toString());
		sb.append("\n");
		return sb.toString();
	}

}
