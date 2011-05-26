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
 * The Class ReportFormField.
 * 
 * @author alexript
 */
public class ReportFormField implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -858750666049064792L;

	/** The name. */
	private String name;

	/** The descr. */
	private String descr;

	/** The defval. */
	private String defval;

	/** The type. */
	private int type;

	/**
	 * Instantiates a new report form field.
	 */
	public ReportFormField() {
		setName("");
		setDescr("");
		setDefval(DataTypes.DT_OBJECT, null);
		setType(DataTypes.DT_OBJECT);
	}

	/**
	 * Gets the defval.
	 * 
	 * @return the defval
	 */
	public Object getDefval() {
		return DataTypes.fromString(getType(), defval);
	}

	/**
	 * Gets the descr.
	 * 
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
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
	 * @param type
	 *            the type
	 * @param defval
	 *            the defval to set
	 */
	public void setDefval(int type, Object defval) {
		this.defval = DataTypes.toString(type, defval);
	}

	/**
	 * Sets the descr.
	 * 
	 * @param descr
	 *            the descr to set
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
