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

/**
 * @author alexript
 * 
 */
public class Table implements Serializable {
	private static final long serialVersionUID = 6058370916975359439L;
	private String name;
	private Name names = new Name();

	public Table() {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the names
	 */
	public Name getNames() {
		return names;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setName(String locale, String name) {
		names.setName(locale, name);
	}

	/**
	 * @param names
	 *            the names to set
	 */
	public void setNames(Name names) {
		this.names = names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tablename: " + name + "\n");
		sb.append("Names: \n");
		sb.append(names.toString());
		sb.append("\n");
		return sb.toString();
	}
}
