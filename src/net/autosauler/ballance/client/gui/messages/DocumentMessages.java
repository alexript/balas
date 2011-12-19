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
package net.autosauler.ballance.client.gui.messages;

import com.google.gwt.i18n.client.Messages;

/**
 * The Interface DocumentMessages.
 * 
 * @author alexript
 */
public interface DocumentMessages extends Messages {

	/**
	 * Btn activate.
	 * 
	 * @return the string
	 */
	public String btnActivate();

	/**
	 * Btn cancel.
	 * 
	 * @return the string
	 */
	public String btnCancel();

	/**
	 * Btn save.
	 * 
	 * @return the string
	 */
	public String btnSave();

	/**
	 * Btn save and activate.
	 * 
	 * @return the string
	 */
	public String btnSaveAndActivate();

	/**
	 * Btn un activate.
	 * 
	 * @return the string
	 */
	public String btnUnActivate();

	/**
	 * Col author.
	 * 
	 * @return the string
	 */
	public String colAuthor();

	/**
	 * Col create date.
	 * 
	 * @return the string
	 */
	public String colCreateDate();

	/**
	 * Col number.
	 * 
	 * @return the string
	 */
	public String colNumber();

	public String labelDocumentsList();

	/**
	 * Menu create.
	 * 
	 * @return the string
	 */
	public String menuCreate();

	/**
	 * Menu reload.
	 * 
	 * @return the string
	 */
	public String menuReload();

	/**
	 * Menu script.
	 * 
	 * @return the string
	 */
	public String menuScript();

	/**
	 * Msg create error.
	 * 
	 * @return the string
	 */
	public String msgCreateError();

	/**
	 * Msg update error.
	 * 
	 * @return the string
	 */
	public String msgUpdateError();

	public String titleEditor();
}
