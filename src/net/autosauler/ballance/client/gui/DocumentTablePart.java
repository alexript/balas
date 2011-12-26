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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.client.model.DocumentTableModel;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The Class DocumentTablePart.
 * 
 * @author alexript
 */
public class DocumentTablePart implements ITableFieldChangeHandler {

	/** The title. */
	private final String title;

	/** The btn plus. */
	private Button btnPlus;

	/** The btn minus. */
	private Button btnMinus;

	/** The newnumber. */
	private Long newnumber = 0L;

	/** The datatypes. */
	private final HashMap<String, Integer> datatypes;

	/** The defaultvalues. */
	private final HashMap<String, Object> defaultvalues;

	private final String tablename;

	private final String docname;

	private EditorGrid<DocumentTableModel> grid;
	private ListStore<DocumentTableModel> store;

	/**
	 * Instantiates a new document table part.
	 * 
	 * @param title
	 *            the title
	 * @param tablename
	 */
	public DocumentTablePart(String title, String tablename, String docname) {
		this.title = title;
		this.tablename = tablename;
		this.docname = docname;
		defaultvalues = new HashMap<String, Object>();
		datatypes = new HashMap<String, Integer>();

		cleanTable();
	}

	/**
	 * Adds the row.
	 */
	private void addRow() {
		grid.getSelectionModel().deselectAll();

		DocumentTableModel row = new DocumentTableModel(newnumber,
				defaultvalues);
		store.add(row);
		grid.getSelectionModel().select(row, false);

		newnumber = newnumber - 1L;

	}

	/**
	 * Clean table.
	 */
	public void cleanTable() {
		if (store != null) {
			try {
				store.removeAll();
			} catch (java.lang.IllegalArgumentException e) {
				Log.error("Something strange:");
				Log.error(e.getMessage());
				Log.error(e.getStackTrace().toString());
			}
		}
		newnumber = 0L;
	}

	/**
	 * Construct pane.
	 * 
	 * @param tablepartname
	 *            the tablepartname
	 * @return the vertical panel
	 */
	public ContentPanel constructPane(String tablepartname) {
		ContentPanel panel = new ContentPanel(new BorderLayout());
		panel.setHeaderVisible(false);

		ToolBar toolBar = new ToolBar();

		btnPlus = new Button(M.table.btnAddrow());
		btnPlus.setIcon(AbstractImagePrototype.create(Images.table.Plus()));
		btnPlus.setToolTip(M.table.btnAddrow());
		btnPlus.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				addRow();
			}
		});

		btnMinus = new Button(M.table.btnDelrow());
		btnMinus.setIcon(AbstractImagePrototype.create(Images.table.Minus()));

		btnMinus.setToolTip(M.table.btnDelrow());
		btnMinus.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				removeRow();

			}
		});

		toolBar.add(btnPlus);
		toolBar.add(btnMinus);

		panel.setTopComponent(toolBar);

		// create table

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();

		UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
		if (role.isAdmin()) {
			column.setId("number");
			column.setHeader(M.table.colNumber());
			column.setWidth(50);
			column.setRowHeader(true);
			columns.add(column);
		}
		column = new ColumnConfig();

		Description structuredescription = StructureFactory
				.getDescription("table." + tablepartname);
		if (structuredescription != null) {
			List<Field> fields = structuredescription.get();
			Iterator<Field> i = fields.iterator();
			while (i.hasNext()) {
				Field f = i.next();
				String helper = f.getHelper();
				String helpertype = f.getHelpertype();

				CatalogPanel h = null;
				if (helpertype.equals("catalog") && (helper != null)
						&& !helper.isEmpty()) {
					h = new CatalogPanel("catalog." + helper);
				}

				String field = f.getFieldname();
				Object defval = f.getDefval();
				int type = f.getType();

				columns.add(DataTypeFactory.addEditableCell(f, h, this));

				defaultvalues.put(field, defval);
				datatypes.put(field, type);

			}
		}

		ColumnModel cm = new ColumnModel(columns);

		store = new ListStore<DocumentTableModel>();

		grid = new EditorGrid<DocumentTableModel>(store, cm);

		grid.setBorders(true);

		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// add table into tabitem

		panel.add(grid, new BorderLayoutData(LayoutRegion.CENTER));

		return panel;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	public Set<HashMap<String, Object>> getValues() {
		Set<HashMap<String, Object>> ds = new HashSet<HashMap<String, Object>>();

		List<DocumentTableModel> models = store.getModels();
		for (DocumentTableModel model : models) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (String name : model.getPropertyNames()) {
				if (datatypes.containsKey(name)) {
					map.put(name, DataTypes.toMapping(datatypes.get(name),

					model.get(name)));
				} else {
					map.put(name, model.get(name));
				}
			}
			ds.add(map);
		}
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IFieldChangeHandler#handleFieldChange
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void handleFieldChange(String tag, final Object newvalue) {
		// Window.alert("Call " + tag + " for new value " + newvalue);

		HashMap<String, String> sendmap = new HashMap<String, String>();
		final DocumentTableModel model = grid.getSelectionModel()
				.getSelectedItem();

		Set<String> names = datatypes.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();

			sendmap.put(name, DataTypes.toString(datatypes.get(name),

			model.get(name)));
		}

		sendmap.put(tag, DataTypes.toString(datatypes.get(tag),

		newvalue));

		MainPanel.setCommInfo(true);
		Services.scripts.evalOnChangeTable(docname, "OnChangeTable", tablename,
				tag, sendmap, datatypes,
				new AsyncCallback<HashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);

						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						MainPanel.setCommInfo(false);

						Set<String> names = datatypes.keySet();
						Iterator<String> i = names.iterator();
						while (i.hasNext()) {
							String name = i.next();
							if (result.containsKey(name)) {
								model.set(
										name,
										DataTypes.fromString(
												datatypes.get(name),
												result.get(name)));

							}
						}
						store.update(model);

					}
				});

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
	public void loadData(final String documentname, final Long number,
			final String tablename) {
		final List<DocumentTableModel> records = new ArrayList<DocumentTableModel>();
		cleanTable();

		MainPanel.setCommInfo(true);
		Services.documents.getTable(documentname, number, tablename,
				new AsyncCallback<Set<HashMap<String, Object>>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);

						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(Set<HashMap<String, Object>> result) {
						MainPanel.setCommInfo(false);

						for (HashMap<String, Object> document : result) {
							records.add(new DocumentTableModel(document,
									datatypes));
						}
						try {
							store.add(records);
						} catch (java.lang.IllegalArgumentException ex) {
							loadData(documentname, number, tablename);
						}

						newnumber = 0L;

					}
				});

	}

	/**
	 * Removes the row.
	 */
	private void removeRow() {
		// Window.alert("Remove row from " + title);

		DocumentTableModel record = grid.getSelectionModel().getSelectedItem();

		if (record != null) {
			Long num = (Long) record.get("number");
			if ((num == null) || (num.compareTo(1L) == -1)) {
				store.remove(record);

			} else {
				new AlertDialog(M.table.msgCantdelete()).show();
			}
		} else {
			new AlertDialog(M.table.msgMustselect()).show();
		}
	}

}
