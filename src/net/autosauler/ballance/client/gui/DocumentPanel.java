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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.client.model.DocumentModel;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.Table;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The Class DocumentPanel.
 * 
 * @author alexript
 */
// TODO: Pager
public class DocumentPanel extends ContentPanel implements IPaneWithMenu,
		IReloadMsgReceiver, IFieldChangeHandler {

	/** The documentname. */
	private final String documentname;

	/** The editor. */
	private FieldSet editor;

	/** The btn save. */
	private Button btnSave;

	/** The btn save activate. */
	private Button btnSaveActivate;

	/** The btn activate. */
	private Button btnActivate;

	/** The btn cancel. */
	private Button btnCancel;

	/** The editformnumber. */
	private Long editformnumber;

	/** The editformisactive. */
	private Boolean editformisactive;

	/** The parts. */
	private DocumentTableParts parts;

	/** The fields. */
	private HashMap<String, HeaderField> fields;

	/** The structuredescription. */
	private final Description structuredescription;

	private final GridSelectionModel<DocumentModel> sm = new GridSelectionModel<DocumentModel>();
	private Grid<DocumentModel> grid;
	private ListStore<DocumentModel> store;
	private final FormData formData = new FormData("98%");

	/**
	 * Instantiates a new document panel.
	 * 
	 * @param docname
	 *            the docname
	 * @param image
	 *            the image
	 */
	public DocumentPanel(String docname) {
		super(new BorderLayout());
		setHeading(M.document.labelDocumentsList());
		documentname = docname;

		structuredescription = StructureFactory.getDescription(docname);
		createStructure();

		createListForm();

		if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())
				|| canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			createEditorForm();
		}
		reloadList();
		formstateEmpty();

	}

	/**
	 * Adds the field.
	 * 
	 * @param name
	 *            the name
	 * @param field
	 *            the field
	 * @param type
	 *            the type
	 * @param defval
	 *            the defval
	 * @param helper
	 *            the helper
	 */
	protected void addField(String name, String field, int type, Object defval,
			Object helper) {
		HeaderField hf = DataTypeFactory.addField(name, field, type, defval,
				helper);

		hf.setChangeHandler(field, this);
		// Log.error(field);
		fields.put(field, hf);
	}

	/**
	 * Can activate.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	public boolean canActivate(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Can create.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	public boolean canCreate(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Can edit.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	public boolean canEdit(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Clean edit form.
	 */
	private void cleanEditForm() {
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			fields.get(name).reset();
		}

		if (hasTablePart()) {
			parts.cleanTables();
		}
	}

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		TabPanel tabPanel = null;
		TabItem head = null;
		FormPanel headSet = null;

		editor = new FieldSet();
		editor.setBorders(true);

		editor.setHeading(M.document.titleEditor());

		headSet = new FormPanel();
		headSet.setLabelAlign(LabelAlign.RIGHT);
		headSet.setLabelWidth(150);
		headSet.setHeaderVisible(false);

		if (hasTablePart()) {
			headSet.setScrollMode(Scroll.ALWAYS);
			editor.setLayout(new FitLayout());
			tabPanel = new TabPanel();
			head = new TabItem(M.document.tableHead());
			head.setLayout(new FitLayout());

		} else {
			editor.setScrollMode(Scroll.ALWAYS);
		}

		List<Field> descrfields = structuredescription.get();
		Iterator<Field> i = descrfields.iterator();
		while (i.hasNext()) {
			Field field = i.next();
			String name = field.getFieldname();
			if (fields.containsKey(name)) {
				// Log.error("Field " + name);
				headSet.add(fields.get(name).getField(), formData);
			}

		}

		if (hasTablePart()) {
			head.add(headSet);
			tabPanel.add(head);
			parts = new DocumentTableParts(tabPanel);
			initTableParts(parts);
			tabPanel.setSelection(tabPanel.getItem(0));
			editor.add(tabPanel);
		} else {
			editor.add(headSet);
		}
		this.add(editor, new BorderLayoutData(LayoutRegion.CENTER));
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {
		sm.setSelectionMode(SelectionMode.SINGLE);

		sm.addSelectionChangedListener(new SelectionChangedListener<DocumentModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<DocumentModel> se) {
				cleanEditForm();
				DocumentModel record = se.getSelectedItem();

				if (record != null) {
					editformnumber = (Long) record.get("number");
					editformisactive = (Boolean) record.get("active");
					if (hasTablePart()) {
						loadTables();
					}
					openEditor(record);
				} else {
					// edit.setEnabled(false);
					// del.setEnabled(false);

				}

			}
		});

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("number");
		column.setHeader("Id");
		column.setWidth(50);
		column.setRowHeader(true);
		columns.add(column);

		column = new ColumnConfig();
		column.setId("active");
		column.setHeader("");
		column.setWidth(50);
		column.setRowHeader(true);
		GridCellRenderer<DocumentModel> gridActive = new GridCellRenderer<DocumentModel>() {

			@Override
			public Object render(DocumentModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<DocumentModel> store, Grid<DocumentModel> grid) {

				Boolean isactive = (Boolean) model.get("active");
				if (isactive) {
					return AbstractImagePrototype.create(Images.menu.Ok())
							.createImage();

				}
				return AbstractImagePrototype.create(Images.menu.Cancel())
						.createImage();

			}
		};
		column.setAlignment(HorizontalAlignment.CENTER);
		column.setRenderer(gridActive);
		columns.add(column);

		GridCellRenderer<DocumentModel> gridDate = new GridCellRenderer<DocumentModel>() {

			@Override
			public Object render(DocumentModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<DocumentModel> store, Grid<DocumentModel> grid) {
				Long longdate = (Long) model.get("createdate");
				if (longdate == null) {
					return "";
				}
				return DataTypeFactory.formatter.format(new Date(longdate));
			}
		};
		column = new ColumnConfig();
		column.setId("createdate");
		column.setHeader(M.document.colCreateDate());
		column.setWidth(100);
		column.setRowHeader(true);
		column.setRenderer(gridDate);
		columns.add(column);

		column = new ColumnConfig();
		column.setId("username");
		column.setHeader(M.document.colAuthor());
		column.setWidth(100);
		column.setRowHeader(true);
		columns.add(column);

		List<Field> fields = structuredescription.get();
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field f = i.next();
			if (f.isInlist()) {
				String helper = f.getHelper();
				String helpertype = f.getHelpertype();

				IFieldChangeHandler h = null;
				if (helpertype.equals("catalog") && (helper != null)
						&& !helper.isEmpty()) {
					h = new CatalogPanel("catalog." + helper);
				} else if (helpertype.equals("document") && (helper != null)
						&& !helper.isEmpty()) {
					h = new DocumentPanel("document." + helper);
				}

				columns.add(DataTypeFactory.addCell(f, h));

			}
		}

		ColumnModel cm = new ColumnModel(columns);

		store = new ListStore<DocumentModel>();

		grid = new Grid<DocumentModel>(store, cm);
		grid.setSelectionModel(sm);
		grid.setBorders(true);

		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,
				200, 100, 300);
		northData.setMargins(new Margins(5, 5, 5, 5));
		northData.setSplit(true);
		northData.setCollapsible(true);

		this.add(grid, northData);

		btnSave = new Button(M.document.btnSave());
		btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				HashMap<String, Object> map = getEditorValues();
				if (map == null) {
					map = new HashMap<String, Object>();
				}

				MainPanel.setCommInfo(true);
				if (editformnumber.equals(-1L)) {
					Services.documents.create(documentname, map,
							getTablesValues(), new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable e) {

									MainPanel.setCommInfo(false);

									new AlertDialog(e).show();
								}

								@Override
								public void onSuccess(Boolean result) {
									MainPanel.setCommInfo(false);
									if (result) {
										reloadList();
									} else {
										new AlertDialog(M.document
												.msgCreateError()).show();
									}

								}
							});
				} else {

					HashMap<String, Set<HashMap<String, Object>>> tablesvalues = getTablesValues();

					// DocumentPanel.this.traceTableValues(tablesvalues);

					Services.documents.update(documentname, editformnumber,
							map, tablesvalues, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									MainPanel.setCommInfo(false);
									new AlertDialog(caught).show();

								}

								@Override
								public void onSuccess(Boolean result) {
									MainPanel.setCommInfo(false);
									if (result) {
										reloadList();
									} else {
										new AlertDialog(M.document
												.msgUpdateError()).show();
									}

								}
							});
				}
			}
		});
		addButton(btnSave);

		btnSaveActivate = new Button(M.document.btnSaveAndActivate());
		btnSaveActivate
				.addSelectionListener(new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						HashMap<String, Object> map = getEditorValues();
						if (map == null) {
							map = new HashMap<String, Object>();
						}

						MainPanel.setCommInfo(true);
						if (editformnumber.equals(-1L)) {
							Services.documents.createAndActivate(documentname,
									map, getTablesValues(),
									new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											MainPanel.setCommInfo(false);
											new AlertDialog(caught).show();
										}

										@Override
										public void onSuccess(Boolean result) {
											MainPanel.setCommInfo(false);
											if (result) {
												reloadList();
											} else {
												new AlertDialog(M.document
														.msgCreateError())
														.show();
											}

										}
									});
						} else {
							Services.documents.updateAndActivate(documentname,
									editformnumber, map, getTablesValues(),
									new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											MainPanel.setCommInfo(false);
											new AlertDialog(caught).show();

										}

										@Override
										public void onSuccess(Boolean result) {
											MainPanel.setCommInfo(false);
											if (result) {
												reloadList();
											} else {
												new AlertDialog(M.document
														.msgUpdateError())
														.show();
											}

										}
									});
						}
					}
				});
		addButton(btnSaveActivate);

		btnActivate = new Button(M.document.btnActivate());
		btnActivate.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!editformnumber.equals(-1L)) {
					MainPanel.setCommInfo(true);
					if (editformisactive) {
						Services.documents.unactivate(documentname,
								editformnumber, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										MainPanel.setCommInfo(false);
										new AlertDialog(caught).show();

									}

									@Override
									public void onSuccess(Void result) {
										MainPanel.setCommInfo(false);
										reloadList();
									}
								});
					} else {
						Services.documents.activate(documentname,
								editformnumber, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										MainPanel.setCommInfo(false);
										new AlertDialog(caught).show();

									}

									@Override
									public void onSuccess(Void result) {
										MainPanel.setCommInfo(false);
										reloadList();
									}
								});

					}
				} else {
					reloadList();
				}

			}
		});
		addButton(btnActivate);

		btnCancel = new Button(M.document.btnCancel());
		btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				formstateCancel();

			}
		});
		addButton(btnCancel);

	}

	/**
	 * Creates the structure.
	 */
	protected void createStructure() {
		fields = new HashMap<String, HeaderField>();
		List<Field> fields = structuredescription.get();
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field f = i.next();
			if (f.isVisible()) {
				String helper = f.getHelper();
				String helpertype = f.getHelpertype();

				IFieldChangeHandler h = null;
				if (helpertype.equals("catalog") && (helper != null)
						&& !helper.isEmpty()) {
					h = new CatalogPanel("catalog." + helper);
				} else if (helpertype.equals("document") && (helper != null)
						&& !helper.isEmpty()) {
					h = new DocumentPanel("document." + helper);
				}
				// Log.error(f.getFieldname());
				addField(
						f.getName().getName(
								LocaleInfo.getCurrentLocale().getLocaleName()),
						f.getFieldname(), f.getType(), f.getDefval(), h);
			}
		}
	}

	/**
	 * Fill editor.
	 * 
	 * @param map
	 *            the map
	 */
	private void fillEditor(DocumentModel map) {
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			fields.get(name).setValue(map.get(name), true);
		}
	}

	private void formstateCancel() {
		btnSave.setEnabled(true);
		btnSaveActivate.setEnabled(true);
		btnActivate.setEnabled(true);
		btnCancel.setEnabled(true);
		editor.setEnabled(true);
		if (hasTablePart()) {
			parts.selectFirstTab();
		}

		fillEditor(grid.getSelectionModel().getSelectedItem());

	}

	private void formstateCreate() {
		cleanEditForm();
		grid.getSelectionModel().deselectAll();
		btnSave.setEnabled(true);
		btnSaveActivate.setEnabled(true);
		btnActivate.setEnabled(false);
		btnCancel.setEnabled(true);
		editor.setEnabled(true);
		if (hasTablePart()) {
			parts.cleanTables();
			parts.selectFirstTab();
		}
	}

	private void formstateEdit() {
		btnSave.setEnabled(true);
		btnSaveActivate.setEnabled(true);
		btnActivate.setEnabled(true);
		btnCancel.setEnabled(true);
		editor.setEnabled(true);
		if (hasTablePart()) {
			parts.selectFirstTab();
		}

	}

	private void formstateEmpty() {
		cleanEditForm();
		if (grid != null) {
			grid.getSelectionModel().deselectAll();
		}
		btnSave.setEnabled(false);
		btnSaveActivate.setEnabled(false);
		btnActivate.setEnabled(false);
		btnCancel.setEnabled(false);
		editor.setEnabled(false);
		if (hasTablePart()) {
			parts.cleanTables();
			parts.selectFirstTab();
		}
	}

	/**
	 * Gets the editor values.
	 * 
	 * @return the editor values
	 */
	private HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			map.put(name, fields.get(name).getValue());
		}

		return map;
	}

	/**
	 * Gets the name.
	 * 
	 * @param number
	 *            the number
	 * @return the name
	 */
	public String getName(Long number) {
		DocumentModel model = store.findModel("number", number);
		if (model != null) {
			return model.get("documentnamevalue");
		}
		return "UNCKNOWN. TRY RELOAD PAGE.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public MenuBar getPaneMenu() {
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu();

		menu.add(new MenuItem(M.document.menuReload(),
				new SelectionListener<MenuEvent>() { // reload
					// users
					// list
					@Override
					public void componentSelected(MenuEvent ce) {
						reloadList();
					}
				}));

		if (canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			menu.add(new MenuItem(M.document.menuCreate(),
					new SelectionListener<MenuEvent>() { // reload
						// users
						// list
						@Override
						public void componentSelected(MenuEvent ce) {
							editformnumber = -1L;
							editformisactive = false;
							formstateCreate();
							openEditor(null);
						}
					}));
		}

		if (Ballance_autosauler_net.sessionId.getUserrole().isAdmin()) {

			menu.add(new MenuItem(M.document.menuScript(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							new ScriptEditor(documentname, DocumentPanel.this);
						}
					}));
		}
		menubar.add(new MenuBarItem(M.menu.menubarDocument(), menu));
		return menubar;

	}

	public DocumentSelector getSelectBox(Long selectednumber) {
		return new DocumentSelector(documentname, selectednumber);
	}

	/**
	 * Gets the tables values.
	 * 
	 * @return the tables values
	 */
	private HashMap<String, Set<HashMap<String, Object>>> getTablesValues() {
		HashMap<String, Set<HashMap<String, Object>>> tableparts = null;
		if (hasTablePart()) {
			tableparts = parts.getValues();
		}
		return tableparts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IFieldChangeHandler#handleFieldChange
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void handleFieldChange(String tag, String newvalueasstring) {
		HashMap<String, String> map = new HashMap<String, String>();

		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			HeaderField hf = fields.get(name);
			map.put(name, hf.getValueAsString());
		}

		HashMap<String, Integer> types = new HashMap<String, Integer>();

		Iterator<Field> j = structuredescription.get().iterator();
		while (j.hasNext()) {
			Field f = j.next();
			types.put(f.getFieldname(), f.getType());
		}

		// Window.alert(map.toString());

		MainPanel.setCommInfo(true);
		Services.scripts.evalOnChange(documentname, "OnChange", tag, map,
				types, new AsyncCallback<HashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);

						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						MainPanel.setCommInfo(false);

						Set<String> names = fields.keySet();
						Iterator<String> i = names.iterator();
						while (i.hasNext()) {
							String name = i.next();
							if (result.containsKey(name)) {
								HeaderField hf = fields.get(name);
								hf.setValue(result.get(name), true);
							}
						}

					}
				});

	}

	/**
	 * Checks for table part.
	 * 
	 * @return true, if successful
	 */
	protected boolean hasTablePart() {
		return !structuredescription.getTables().isEmpty();
	}

	/**
	 * Inits the table parts.
	 * 
	 * @param parts
	 *            the parts
	 */
	protected void initTableParts(final DocumentTableParts parts) {
		List<Table> tables = structuredescription.getTables();
		Iterator<Table> i = tables.iterator();
		while (i.hasNext()) {
			Table table = i.next();

			DocumentTablePart part = new DocumentTablePart(table.getNames()
					.getName(LocaleInfo.getCurrentLocale().getLocaleName()),
					table.getName(), documentname);
			parts.addPart(table.getName(), part);
		}
	}

	/**
	 * Load tables.
	 * 
	 */
	private void loadTables() {
		parts.loadData(documentname, editformnumber);
	}

	/**
	 * Open editor.
	 * 
	 * @param map
	 *            the map
	 */
	private void openEditor(DocumentModel map) {
		if (editor == null) {
			Log.error("Not implemented yet.");
			return;
		}
		if (editformnumber.equals(-1L) && (map == null)) {
			formstateCreate();
		} else {
			fillEditor(map);
			if (editformisactive) {
				btnActivate.setText(M.document.btnUnActivate());
			} else {
				btnActivate.setText(M.document.btnActivate());
			}
			formstateEdit();
		}

	}

	/**
	 * Reload list.
	 */
	@Override
	public void reloadList() {
		// Log.error("reload list");
		store.removeAll();
		formstateEmpty();
		DocumentModel.load(store, documentname);
	}

	protected void traceTableValues(
			HashMap<String, Set<HashMap<String, Object>>> tablesvalues) {

		for (String partname : tablesvalues.keySet()) {
			Log.error("TablePart: " + partname);
			Set<HashMap<String, Object>> records = tablesvalues.get(partname);
			for (HashMap<String, Object> record : records) {
				for (String field : record.keySet()) {
					Log.error(" --" + field + ": "
							+ record.get(field).toString());
				}
			}
		}

	}

}
