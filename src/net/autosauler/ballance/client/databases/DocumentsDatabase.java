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

package net.autosauler.ballance.client.databases;

import java.util.HashMap;

import com.google.gwt.view.client.HasData;

/**
 * The Class DocumentsDatabase.
 * 
 * @author alexript
 */
public class DocumentsDatabase {

	/**
	 * Gets the.
	 * 
	 * @param documentname
	 *            the documentname
	 * @return the documents database
	 */
	public static DocumentsDatabase get(String documentname) {
		return new DocumentsDatabase(documentname);
	}

	/** The data provider. */
	private final DocumentsDataProvider dataProvider;

	/**
	 * Instantiates a new documents database.
	 * 
	 * @param documentname
	 *            the documentname
	 */
	public DocumentsDatabase(String documentname) {
		dataProvider = new DocumentsDataProvider(documentname);
	}

	/**
	 * Adds the data display.
	 * 
	 * @param display
	 *            the display
	 */
	public void addDataDisplay(HasData<HashMap<String, Object>> display) {
		dataProvider.addDataDisplay(display);
	}

	/**
	 * Gets the data provider.
	 * 
	 * @return the data provider
	 */
	public DocumentsDataProvider getDataProvider() {
		return dataProvider;
	}

}
