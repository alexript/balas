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

package net.autosauler.ballance.client.gui;

/**
 * The Interface ITableFieldChangeHandler.
 * 
 * @author alexript
 */
public interface ITableFieldChangeHandler {

	/**
	 * Handle field change.
	 * 
	 * @param tag
	 *            the tag
	 * @param values
	 *            the values
	 */
	public void handleFieldChange(String tag, Object newvalue);
}
