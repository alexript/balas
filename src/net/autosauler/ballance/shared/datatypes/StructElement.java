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

/**
 * The Class StructElement.
 * 
 * @author alexript
 */
public class StructElement {

	/** The type. */
	private final int type;

	/** The defvalue. */
	private final Object defvalue;

	/**
	 * Instantiates a new struct element.
	 * 
	 * @param type
	 *            the type
	 * @param defvalue
	 *            the defvalue
	 */
	public StructElement(int type, Object defvalue) {
		this.type = type;
		this.defvalue = defvalue;
	}

	/**
	 * Gets the def val.
	 * 
	 * @return the def val
	 */
	public Object getDefVal() {
		return defvalue;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

}
