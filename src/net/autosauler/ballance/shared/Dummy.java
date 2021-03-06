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
 * The Class Dummy. GWT rules!!! http://nofate.name/oshibki-serializacii-v-gwt/
 * 
 * @author alexript
 */
public class Dummy implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -547562347176212918L;

	/** The name. */
	public Name name;

	/** The field. */
	public Field field;

	/** The table. */
	public Table table;

	/**
	 * Instantiates a new dummy.
	 */
	public Dummy() {
	};
}
