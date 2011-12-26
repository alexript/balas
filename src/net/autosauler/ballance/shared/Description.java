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
import java.util.Iterator;
import java.util.List;

/**
 * The Class Description.
 * 
 * @author alexript
 */
public class Description implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4249956404428572162L;

	private Name name;

	/** The fields. */
	private List<Field> fields = new ArrayList<Field>();

	/** The tables. */
	private List<Table> tables = new ArrayList<Table>();

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
	 * Adds the table.
	 * 
	 * @param table
	 *            the table
	 */
	public void addTable(Table table) {
		tables.add(table);
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public Name getName() {
		return name;
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
	 * Gets the tables.
	 * 
	 * @return the tables
	 */
	public List<Table> getTables() {
		return tables;
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
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(Name name) {
		this.name = name;
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

	/**
	 * Sets the tables.
	 * 
	 * @param tables
	 *            the new tables
	 */
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Role=" + role + "\n");
		sb.append("Names: \n");
		sb.append(name.toString());
		sb.append("\n");
		sb.append("Fields:\n");
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field f = i.next();
			sb.append(f.toString());
		}

		sb.append("Tables:\n");
		Iterator<Table> j = tables.iterator();
		while (j.hasNext()) {
			Table f = j.next();
			sb.append(f.toString());
		}

		return sb.toString();
	}
}
