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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.databases.DocumentsDatabase;
import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.Table;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class DocumentPanel.
 * 
 * @author alexript
 */
public class DocumentPanel extends Composite implements IPaneWithMenu,
		IReloadMsgReceiver, IFieldChangeHandler {

	/** The documentname. */
	private final String documentname;

	/** The tabimage. */
	private final Image tabimage;

	/** The progress. */
	private static final Image progress = new Image(Images.menu.progress());

	/** The root. */
	private AbsolutePanel root;

	/** The list. */
	private VerticalPanel list;

	/** The editor. */
	private VerticalPanel editor;

	/** The editorscroll. */
	private ScrollPanel editorscroll;

	/** The listscroll. */
	private ScrollPanel listscroll;

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

	/** The cell table. */
	@UiField(provided = true)
	private CellTable<HashMap<String, Object>> cellTable;

	/** The pager. */
	@UiField(provided = true)
	private SimplePager pager;

	/** The pager. */
	@UiField(provided = true)
	private SimplePager pager2;

	/** The db. */
	private DocumentsDatabase db;

	/** The parts. */
	private DocumentTableParts parts;

	/** The fields. */
	private HashMap<String, HeaderField> fields;

	/** The structuredescription. */
	private final Description structuredescription;

	// /** The defaultvalues. */
	// private final HashMap<String, Object> defaultvalues;
	//
	// /** The datatypes. */
	// private final HashMap<String, Integer> datatypes;

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

	/**
	 * Instantiates a new document panel.
	 * 
	 * @param docname
	 *            the docname
	 * @param image
	 *            the image
	 */
	public DocumentPanel(String docname, Image image) {
		documentname = docname;
		tabimage = image;
		// defaultvalues = new HashMap<String, Object>();
		// datatypes = new HashMap<String, Integer>();
		structuredescription = StructureFactory.getDescription("document."
				+ docname);
		createStructure();
	}

	/**
	 * Adds the column.
	 * 
	 * @param name
	 *            the name
	 * @param field
	 *            the field
	 * @param type
	 *            the type
	 * @param width
	 *            the width
	 * @param defval
	 *            the defval
	 * @param helper
	 *            the helper
	 */
	private void addColumn(final String name, final String field,
			final int type, final int width, final Object defval,
			final Object helper) {

		DataTypeFactory.addCell(cellTable, name, field, type, width, defval,
				helper);

		// defaultvalues.put(field, defval);
		// datatypes.put(field, type);

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
		// String nn = Character.toUpperCase(field.charAt(0)) +
		// field.substring(1);
		hf.setChangeHandler(field, this);

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
	}

	/**
	 * Creates the document header editor.
	 * 
	 * @return the widget
	 */
	private VerticalPanel createDocumentHeaderEditor() {
		VerticalPanel p = new VerticalPanel();
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			p.add(fields.get(name));
		}

		return p;
	}

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		VerticalPanel editform = createDocumentHeaderEditor();
		if (editform == null) {
			Log.error("Document editor form is not implemented!!!");
			return;
		}
		editor = new VerticalPanel();
		// editor.setVisible(false);

		editor.add(editform);

		if (hasTablePart()) {
			parts = new DocumentTableParts();
			initTableParts(parts);
			parts.selectTab(0);
			editor.add(parts);
		}

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(3);

		btnSave = new Button(M.document.btnSave());
		btnSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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
					Services.documents.update(documentname, editformnumber,
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
												.msgUpdateError()).show();
									}

								}
							});
				}
			}
		});
		buttons.add(btnSave);

		btnSaveActivate = new Button(M.document.btnSaveAndActivate());
		btnSaveActivate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, Object> map = getEditorValues();
				if (map == null) {
					map = new HashMap<String, Object>();
				}

				MainPanel.setCommInfo(true);
				if (editformnumber.equals(-1L)) {
					Services.documents.createAndActivate(documentname, map,
							getTablesValues(), new AsyncCallback<Boolean>() {

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
												.msgCreateError()).show();
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
												.msgUpdateError()).show();
									}

								}
							});
				}
			}
		});
		buttons.add(btnSaveActivate);

		btnActivate = new Button(M.document.btnActivate());
		btnActivate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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
		buttons.add(btnActivate);

		btnCancel = new Button(M.document.btnCancel());
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reloadList();

			}
		});
		buttons.add(btnCancel);

		editor.add(buttons);

		editorscroll = new ScrollPanel(editor);
		editorscroll.setSize("100%", "450px");
		editorscroll.setAlwaysShowScrollBars(true);
		editorscroll.setVisible(false);

		root.add(editorscroll, 0, 0);
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);

		db = DocumentsDatabase.get(documentname);

		list = new VerticalPanel();

		cellTable = new CellTable<HashMap<String, Object>>(KEY_PROVIDER);
		cellTable.setWidth("100%", true);

		// ListHandler<HashMap<String, Object>> sortHandler = null;
		// ListHandler<HashMap<String, Object>> sortHandler = new
		// ListHandler<HashMap<String, Object>>(
		// db.getDataProvider().getList());
		// cellTable.addColumnSortHandler(sortHandler);

		final SelectionModel<HashMap<String, Object>> selectionModel = new SingleSelectionModel<HashMap<String, Object>>(
				KEY_PROVIDER);

		cellTable.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						HashMap<String, Object> map = ((SingleSelectionModel<HashMap<String, Object>>) selectionModel)
								.getSelectedObject();
						if (map != null) {
							editformnumber = (Long) map.get("number");
							editformisactive = (Boolean) map.get("active");
							if (hasTablePart()) {
								loadTables();
							}
							openEditor(map);
						} else {
							// edit.setEnabled(false);
							// del.setEnabled(false);

						}
					}
				});

		initTableColumns(selectionModel); // , sortHandler);
		db.addDataDisplay(cellTable);

		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(cellTable);
		list.add(pager);

		list.add(cellTable);
		db.getDataProvider().reload();

		pager2 = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager2.setDisplay(cellTable);
		list.add(pager2);

		listscroll = new ScrollPanel(list);
		listscroll.setSize("100%", "500px");
		listscroll.setAlwaysShowScrollBars(true);

		root.add(listscroll, 0, 0);
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

				CatalogPanel h = null;
				if (helpertype.equals("catalog") && (helper != null)
						&& !helper.isEmpty()) {
					h = new CatalogPanel(helper, null);
				}

				addField(
						f.getName().getName(
								LocaleInfo.getCurrentLocale().getLocaleName()),
						f.getFieldname(), f.getType(), f.getDefval(), h);
			}
		}
	}

	/**
	 * Effect hide.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectHide(Element element) /*-{
		new $wnd.Effect.DropOut(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Effect show.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectShow(Element element) /*-{
		new $wnd.Effect.SlideDown(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Fill editor.
	 * 
	 * @param map
	 *            the map
	 */
	private void fillEditor(HashMap<String, Object> map) {
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			fields.get(name).setValue(map.get(name), true);
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
	 * Gets the list form.
	 * 
	 * @return the list form
	 */
	public DocumentPanel getListForm() {
		root = new AbsolutePanel();
		root.setWidth("100%");
		root.setHeight("500px");

		createListForm();

		if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())
				|| canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			createEditorForm();
		}
		initWidget(root);

		return this;
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
							cleanEditForm();
							if (hasTablePart()) {
								parts.cleanTables();
							}
							openEditor(null);
						}
					}));
		}

		if (Ballance_autosauler_net.sessionId.getUserrole().isAdmin()) {

			menu.add(new MenuItem(M.document.menuScript(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							new ScriptEditor("document." + documentname,
									DocumentPanel.this);
						}
					}));
		}
		menubar.add(new MenuBarItem(M.menu.menubarDocument(), menu));
		return menubar;

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
		Services.scripts.evalOnChange("document." + documentname, "OnChange",
				tag, map, types, new AsyncCallback<HashMap<String, String>>() {

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
	 * Inits the table columns.
	 * 
	 * @param selectionModel
	 *            the selection model
	 */
	private void initTableColumns(
			final SelectionModel<HashMap<String, Object>> selectionModel) {
		// , ListHandler<HashMap<String, Object>> sortHandler) {

		// Doc number.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> docNumberColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				return ((Long) map.get("number")).toString();
			}
		};
		// docNumberColumn.setSortable(true);
		// sortHandler.setComparator(docNumberColumn,
		// new Comparator<HashMap<String, Object>>() {
		// @Override
		// public int compare(HashMap<String, Object> m1,
		// HashMap<String, Object> m2) {
		// Long n1 = (Long) m1.get("number");
		// Long n2 = (Long) m2.get("number");
		// return n1.compareTo(n2);
		// }
		// });
		cellTable.addColumn(docNumberColumn, M.document.colNumber());

		cellTable.setColumnWidth(docNumberColumn, 50, Unit.PX);

		// Active flag.
		// ----------------------------------------------------------

		Column<HashMap<String, Object>, ImageResource> activeColumn = new Column<HashMap<String, Object>, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(HashMap<String, Object> map) {
				Boolean isactive = (Boolean) map.get("active");
				if (isactive) {
					return Images.menu.Ok();
				}
				return Images.menu.Cancel();
			}
		};

		activeColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		cellTable.addColumn(activeColumn, "");

		cellTable.setColumnWidth(activeColumn, 50, Unit.PX);

		// Doc creation date.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> docDateColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				return DataTypeFactory.formatter.format(new Date((Long) map
						.get("createdate")));
			}
		};
		cellTable.addColumn(docDateColumn, M.document.colCreateDate());

		cellTable.setColumnWidth(docDateColumn, 100, Unit.PX);

		// Doc author.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> docAuthorColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				return (String) map.get("username");
			}
		};
		cellTable.addColumn(docAuthorColumn, M.document.colAuthor());

		cellTable.setColumnWidth(docAuthorColumn, 100, Unit.PX);

		List<Field> fields = structuredescription.get();
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field f = i.next();
			if (f.isInlist()) {
				String helper = f.getHelper();
				String helpertype = f.getHelpertype();

				CatalogPanel h = null;
				if (helpertype.equals("catalog") && (helper != null)
						&& !helper.isEmpty()) {
					h = new CatalogPanel(helper, null);
				}

				addColumn(
						f.getName().getName(
								LocaleInfo.getCurrentLocale().getLocaleName()),
						f.getFieldname(), f.getType(), f.getColumnwidth(),
						f.getDefval(), h);
			}
		}

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
	private void openEditor(HashMap<String, Object> map) {
		if (editor == null) {
			Log.error("Not implemented yet.");
			return;
		}
		if (editformnumber.equals(-1L) && (map == null)) {
			btnActivate.setVisible(false);
		} else {
			fillEditor(map);
			if (editformisactive) {
				btnActivate.setText(M.document.btnUnActivate());
			} else {
				btnActivate.setText(M.document.btnActivate());
			}
			btnActivate.setVisible(true);
		}
		effectHide(listscroll.getElement());
		effectShow(editorscroll.getElement());
	}

	/**
	 * Reload list.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void reloadList() {
		db.getDataProvider().reload();

		HashMap<String, Object> selection = ((SingleSelectionModel<HashMap<String, Object>>) cellTable
				.getSelectionModel()).getSelectedObject();
		if (selection != null) {

			cellTable.getSelectionModel().setSelected(selection, false);
		}
		if (!listscroll.isVisible() && (editor != null) && editor.isVisible()) {
			effectHide(editorscroll.getElement());
			effectShow(listscroll.getElement());
		}
	}

}
