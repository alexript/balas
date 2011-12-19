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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * The Class DocumentTableParts.
 * 
 * @author alexript
 */
public class DocumentTableParts extends TabPanel {

	/** The parts. */
	private final HashMap<String, DocumentTablePart> parts;

	/**
	 * Instantiates a new document table parts.
	 */
	public DocumentTableParts() {
		super();
		parts = new HashMap<String, DocumentTablePart>();
	}

	/**
	 * Adds the part.
	 * 
	 * @param name
	 *            the name
	 * @param part
	 *            the part
	 */
	public void addPart(String name, DocumentTablePart part) {
		parts.put(name, part);
		TabItem tabitem = new TabItem(part.getTitle());
		tabitem.setLayout(new FitLayout());
		tabitem.add(part.constructPane(name));
		add(tabitem);
	}

	/**
	 * Clean tables.
	 */
	public void cleanTables() {
		Set<String> names = parts.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			DocumentTablePart part = parts.get(name);
			part.cleanTable();
		}
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	public HashMap<String, Set<HashMap<String, Object>>> getValues() {
		HashMap<String, Set<HashMap<String, Object>>> map = new HashMap<String, Set<HashMap<String, Object>>>();

		Set<String> names = parts.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			DocumentTablePart part = parts.get(name);
			map.put(name, part.getValues());
		}

		return map;
	}

	/**
	 * Load data.
	 * 
	 * @param documentname
	 *            the documentname
	 * @param number
	 *            the number
	 */
	public void loadData(String documentname, Long number) {
		Set<String> names = parts.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			DocumentTablePart part = parts.get(name);
			part.loadData(documentname, number, name);
		}
	}

}
