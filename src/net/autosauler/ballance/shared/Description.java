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
import java.util.ArrayList;
import java.util.List;


/**
 * The Class Description.
 * 
 * @author alexript
 */
public class Description implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4249956404428572162L;

	/** The fields. */
	private List<Field> fields = new ArrayList<Field>();

	/** The role. */
	private int role;

	/**
	 * Instantiates a new description.
	 */
	public Description() {

	}

	/**
	 * Adds the.
	 * 
	 * @param field
	 *            the field
	 */
	public void add(Field field) {
		fields.add(field);
	}

	/**
	 * Gets the.
	 * 
	 * @return the list
	 */
	public List<Field> get() {
		return fields;
	}

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public int getRole() {
		return role;
	}

	/**
	 * Sets the.
	 * 
	 * @param fields
	 *            the fields
	 */
	public void set(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the role to set
	 */
	public void setRole(int role) {
		this.role = role;
	}
}
