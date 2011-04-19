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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The Class DocumentTableParts.
 * 
 * @author alexript
 */
public class DocumentTableParts extends Composite {

	/** The mainpane. */
	private final DecoratedTabPanel mainpane;

	/** The parts. */
	private final HashMap<String, DocumentTablePart> parts;

	/**
	 * Instantiates a new document table parts.
	 */
	public DocumentTableParts() {
		parts = new HashMap<String, DocumentTablePart>();
		mainpane = new DecoratedTabPanel();
		mainpane.setWidth("750px");
		mainpane.setHeight("250px");

		initWidget(mainpane);
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
		mainpane.add(part.constructPane(), getTabHeaderString(part.getTitle()),
				true);
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
	 * Gets the tab header string.
	 * 
	 * @param text
	 *            the text
	 * @return the tab header string
	 */
	private String getTabHeaderString(String text) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(2);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		HTML headerText = new HTML(text);
		hPanel.add(headerText);

		// Return the HTML string for the panel
		return hPanel.getElement().getString();
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

	/**
	 * Select tab.
	 * 
	 * @param index
	 *            the index
	 */
	public void selectTab(int index) {
		mainpane.selectTab(index);
	}
}
