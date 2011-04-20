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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.client.DocumentServiceAsync;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class DocumentTablePart.
 * 
 * @author alexript
 */
public class DocumentTablePart extends Composite {

	/** The title. */
	private final String title;

	/** The dataset. */
	private List<HashMap<String, Object>> dataset = null;

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

	/** The cell table. */
	@UiField(provided = true)
	private CellTable<HashMap<String, Object>> cellTable;

	private ListDataProvider<HashMap<String, Object>> dataProvider;

	/** The Constant KEY_PROVIDER. */
	private static final ProvidesKey<HashMap<String, Object>> KEY_PROVIDER = new ProvidesKey<HashMap<String, Object>>() {

		/**
		 * Gets the key.
		 * 
		 * @param item
		 *            the item
		 * @return the key
		 */
		@Override
		public Object getKey(HashMap<String, Object> map) {
			return map == null ? null : map.get("number");
		}
	};

	private static final SelectionModel<HashMap<String, Object>> selectionModel = new SingleSelectionModel<HashMap<String, Object>>(
			KEY_PROVIDER);

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
		// Window.alert("Add row into " + title);
		HashMap<String, Object> row = new HashMap<String, Object>();
		row.put("number", new Long(0L));
		dataset.add(row);
		dataProvider.refresh();
	}

	/**
	 * Clean table.
	 */
	public void cleanTable() {
		if (dataset != null) {
			dataset.clear();
		} else {
			dataset = new ArrayList<HashMap<String, Object>>();
		}
		if (dataProvider != null) {
			dataProvider.setList(dataset);
			dataProvider.refresh();
		}
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

		cellTable = new CellTable<HashMap<String, Object>>(KEY_PROVIDER);
		cellTable.setWidth("100%", true);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						HashMap<String, Object> map = ((SingleSelectionModel<HashMap<String, Object>>) selectionModel)
								.getSelectedObject();
						if (map != null) {
							Long recnum = (Long) map.get("number");
							// onselect
						} else {
							// edit.setEnabled(false);
							// del.setEnabled(false);

						}
					}
				});

		// Record number.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> recNumberColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				if (!map.containsKey("number")) {
					return "0";
				}
				Object o = map.get("number");
				if (o == null) {
					return "0";
				}
				return ((Long) o).toString();
			}
		};

		cellTable.addColumn(recNumberColumn, "N-");

		cellTable.setColumnWidth(recNumberColumn, 50, Unit.PX);

		initTableColumns(selectionModel);

		dataProvider = new ListDataProvider<HashMap<String, Object>>();
		dataProvider.addDataDisplay(cellTable);

		dataProvider.refresh(); // ?

		cellTable.setSelectionModel(selectionModel);

		ScrollPanel scroll = new ScrollPanel(cellTable);
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
		Set<HashMap<String, Object>> ds = new HashSet<HashMap<String, Object>>(
				dataset);
		return ds;
	}

	/**
	 * @param selectionModel
	 */
	private void initTableColumns(
			SelectionModel<HashMap<String, Object>> selectionModel) {
		// TODO Auto-generated method stub

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
		// Window.alert("Remove row from " + title);
		HashMap<String, Object> map = ((SingleSelectionModel<HashMap<String, Object>>) selectionModel)
				.getSelectedObject();
		if (map != null) {
			Long num = (Long) map.get("number");
			if (num.equals(0L)) {
				dataset.remove(map);
				dataProvider.setList(dataset);
				dataProvider.refresh();
			} else {
				new AlertDialog("You can't delete this record").show();
			}
		} else {
			new AlertDialog("You must select record before delete").show();
		}
	}

	/**
	 * Sets the data.
	 * 
	 * @param set
	 *            the set
	 */
	private void setData(Set<HashMap<String, Object>> set) {
		dataset = new ArrayList<HashMap<String, Object>>(set);
		dataProvider.setList(dataset);
		dataProvider.refresh();
	}
}
