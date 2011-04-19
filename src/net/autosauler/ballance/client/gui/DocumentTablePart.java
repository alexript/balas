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
import java.util.HashSet;
import java.util.Set;

import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.client.DocumentServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class DocumentTablePart.
 * 
 * @author alexript
 */
public class DocumentTablePart extends Composite {

	/** The title. */
	private final String title;

	/** The dataset. */
	private Set<HashMap<String, Object>> dataset = null;

	/** The service. */
	private static DocumentServiceAsync service = GWT
			.create(DocumentService.class);

	/** The Constant images. */
	private static final TablePartImages images = GWT
			.create(TablePartImages.class);

	/** The btn plus. */
	private Image btnPlus;

	/** The btn minus. */
	private Image btnMinus;

	/**
	 * Instantiates a new document table part.
	 * 
	 * @param title
	 *            the title
	 */
	public DocumentTablePart(String title) {
		this.title = title;
		cleanTable();
	}

	/**
	 * Adds the row.
	 */
	private void addRow() {
		Window.alert("Add row into " + title);
		// TODO: add row into table
	}

	/**
	 * Clean table.
	 */
	public void cleanTable() {
		if (dataset != null) {
			dataset.clear();
		} else {
			dataset = new HashSet<HashMap<String, Object>>();
		}
		// TODO: remove rows

	}

	/**
	 * Construct pane.
	 * 
	 * @return the vertical panel
	 */
	public VerticalPanel constructPane() {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("750px");
		panel.setHeight("220px");

		HorizontalPanel tools = new HorizontalPanel();
		tools.setHeight("20px");
		tools.setSpacing(3);

		btnPlus = new Image(images.Plus());
		btnPlus.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addRow();
			}
		});

		btnMinus = new Image(images.Minus());
		btnMinus.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeRow();

			}
		});

		tools.add(btnPlus);
		tools.add(btnMinus);
		panel.add(tools);

		ScrollPanel scroll = new ScrollPanel(new Label(title)); // TODO: build
																// table
		scroll.setSize("100%", "200px");

		panel.add(scroll);

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.UIObject#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	public Set<HashMap<String, Object>> getValues() {
		return dataset;
	}

	/**
	 * Load data.
	 * 
	 * @param documentname
	 *            the documentname
	 * @param number
	 *            the number
	 * @param tablename
	 *            the tablename
	 */
	public void loadData(String documentname, Long number, String tablename) {
		MainPanel.setCommInfo(true);
		service.getTable(documentname, number, tablename,
				new AsyncCallback<Set<HashMap<String, Object>>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						cleanTable();
						new AlertDialog(caught.getMessage()).show();

					}

					@Override
					public void onSuccess(Set<HashMap<String, Object>> result) {
						MainPanel.setCommInfo(false);
						setData(result);

					}
				});

	}

	/**
	 * Removes the row.
	 */
	private void removeRow() {
		Window.alert("Remove row from " + title);
		// TODO: remove row from table
	}

	/**
	 * Sets the data.
	 * 
	 * @param set
	 *            the set
	 */
	private void setData(Set<HashMap<String, Object>> set) {
		dataset = set;
		// TODO: fill table
	}
}
