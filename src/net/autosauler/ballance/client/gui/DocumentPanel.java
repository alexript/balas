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
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.client.DocumentServiceAsync;
import net.autosauler.ballance.client.databases.DocumentsDatabase;
import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class DocumentPanel.
 * 
 * @author alexript
 */
public abstract class DocumentPanel extends Composite implements IPaneWithMenu {

	/** The documentname. */
	private final String documentname;

	/** The service. */
	private static DocumentServiceAsync service = null;

	/** The tabimage. */
	private final Image tabimage;

	/** The l. */
	private static DocumentMessages l = null;

	/** The progress. */
	private static Image progress = null;

	/** The images. */
	protected static MenuImages images = GWT.create(MenuImages.class);

	/** The Constant DATEFORMATTER. */
	private static final String DATEFORMATTER = "yyyy/MM/dd HH:mm";

	/** The formatter. */
	private static SimpleDateFormat formatter = null;

	/** The root. */
	private AbsolutePanel root;

	/** The list. */
	private VerticalPanel list;

	/** The editor. */
	private VerticalPanel editor;

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

	/** The db. */
	private DocumentsDatabase db;

	/** The parts. */
	private DocumentTableParts parts;

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
		if (service == null) {
			service = GWT.create(DocumentService.class);
			l = GWT.create(DocumentMessages.class);
			progress = new Image(images.progress());
			formatter = new SimpleDateFormat(DATEFORMATTER);
		}
	}

	/**
	 * Can activate.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canActivate(UserRole role);

	/**
	 * Can create.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canCreate(UserRole role);

	/**
	 * Can edit.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canEdit(UserRole role);

	/**
	 * Clean edit form.
	 */
	abstract void cleanEditForm();

	/**
	 * Creates the document header editor.
	 * 
	 * @return the widget
	 */
	protected abstract Widget createDocumentHeaderEditor();

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		Widget editform = createDocumentHeaderEditor();
		if (editform == null) {
			Log.error("Document editor form is not implemented!!!");
			return;
		}
		editor = new VerticalPanel();
		editor.setVisible(false);

		editor.add(editform);

		if (hasTablePart()) {
			parts = new DocumentTableParts();
			initTableParts(parts);
			parts.selectTab(0);
			editor.add(parts);
		}

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(3);

		btnSave = new Button(l.btnSave());
		btnSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, Object> map = getEditorValues();
				if (map == null) {
					map = new HashMap<String, Object>();
				}

				MainPanel.setCommInfo(true);
				if (editformnumber.equals(-1L)) {
					service.create(documentname, map, getTablesValues(),
							new AsyncCallback<Boolean>() {

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
										new AlertDialog(l.msgCreateError())
												.show();
									}

								}
							});
				} else {
					service.update(documentname, editformnumber, map,
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
										new AlertDialog(l.msgUpdateError())
												.show();
									}

								}
							});
				}
			}
		});
		buttons.add(btnSave);

		btnSaveActivate = new Button(l.btnSaveAndActivate());
		btnSaveActivate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, Object> map = getEditorValues();
				if (map == null) {
					map = new HashMap<String, Object>();
				}

				MainPanel.setCommInfo(true);
				if (editformnumber.equals(-1L)) {
					service.createAndActivate(documentname, map,
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
										new AlertDialog(l.msgCreateError())
												.show();
									}

								}
							});
				} else {
					service.updateAndActivate(documentname, editformnumber,
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
										new AlertDialog(l.msgUpdateError())
												.show();
									}

								}
							});
				}
			}
		});
		buttons.add(btnSaveActivate);

		btnActivate = new Button(l.btnActivate());
		btnActivate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!editformnumber.equals(-1L)) {
					MainPanel.setCommInfo(true);
					if (editformisactive) {
						service.unactivate(documentname, editformnumber,
								new AsyncCallback<Void>() {

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
						service.activate(documentname, editformnumber,
								new AsyncCallback<Void>() {

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

		btnCancel = new Button(l.btnCancel());
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reloadList();

			}
		});
		buttons.add(btnCancel);

		editor.add(buttons);

		root.add(editor, 0, 0);
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {
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
		list.add(cellTable);

		db.getDataProvider().reload();

		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(cellTable);

		list.add(pager);

		root.add(list, 0, 0);
	}

	/**
	 * Draw document row for list.
	 * 
	 * @param map
	 *            the map
	 * @return the string
	 */
	protected abstract String drawDocumentRowForList(HashMap<String, Object> map);

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
	protected abstract void fillEditor(HashMap<String, Object> map);

	/**
	 * Gets the editor values.
	 * 
	 * @return the editor values
	 */
	protected abstract HashMap<String, Object> getEditorValues();

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
		MenuBar menu = new MenuBar();

		menu.addItem(l.menuReload(), new Command() { // reload users list
					@Override
					public void execute() {
						reloadList();
					}
				});

		if (canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			menu.addItem(l.menuCreate(), new Command() { // reload users list
						@Override
						public void execute() {
							editformnumber = -1L;
							editformisactive = false;
							cleanEditForm();
							if (hasTablePart()) {
								parts.cleanTables();
							}
							openEditor(null);
						}
					});
		}
		return menu;

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

	/**
	 * Checks for table part.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean hasTablePart();

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
		cellTable.addColumn(docNumberColumn, l.colNumber());

		cellTable.setColumnWidth(docNumberColumn, 50, Unit.PX);

		// Active flag.
		// ----------------------------------------------------------

		Column<HashMap<String, Object>, ImageResource> activeColumn = new Column<HashMap<String, Object>, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(HashMap<String, Object> map) {
				Boolean isactive = (Boolean) map.get("active");
				if (isactive) {
					return images.Ok();
				}
				return images.Cancel();
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
				return formatter.format(new Date((Long) map.get("createdate")));
			}
		};
		cellTable.addColumn(docDateColumn, l.colCreateDate());

		cellTable.setColumnWidth(docDateColumn, 150, Unit.PX);

		// Doc author.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> docAuthorColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				return (String) map.get("username");
			}
		};
		cellTable.addColumn(docAuthorColumn, l.colAuthor());

		cellTable.setColumnWidth(docAuthorColumn, 100, Unit.PX);

		// Document title.
		// ----------------------------------------------------------

		Column<HashMap<String, Object>, SafeHtml> rolesColumn = new Column<HashMap<String, Object>, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(HashMap<String, Object> map) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant(drawDocumentRowForList(map));
				return sb.toSafeHtml();
			}

		};
		rolesColumn.setSortable(false);

		cellTable.addColumn(rolesColumn, l.colDocument());

		cellTable.setColumnWidth(rolesColumn, 300, Unit.PX);

	}

	/**
	 * Inits the table parts.
	 * 
	 * @param parts
	 *            the parts
	 */
	protected abstract void initTableParts(final DocumentTableParts parts);

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
				btnActivate.setText(l.btnUnActivate());
			} else {
				btnActivate.setText(l.btnActivate());
			}
			btnActivate.setVisible(true);
		}
		effectHide(list.getElement());
		effectShow(editor.getElement());
	}

	/**
	 * Reload list.
	 */
	@SuppressWarnings("unchecked")
	private void reloadList() {
		db.getDataProvider().reload();

		HashMap<String, Object> selection = ((SingleSelectionModel<HashMap<String, Object>>) cellTable
				.getSelectionModel()).getSelectedObject();
		if (selection != null) {

			cellTable.getSelectionModel().setSelected(selection, false);
		}
		if (!list.isVisible() && (editor != null) && editor.isVisible()) {
			effectHide(editor.getElement());
			effectShow(list.getElement());
		}
	}

}
