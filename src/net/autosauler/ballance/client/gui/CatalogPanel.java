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
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.client.model.CatalogModel;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

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
import com.extjs.gxt.ui.client.widget.button.Button;
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
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class CatalogPanel.
 * 
 * @author alexript
 */
// TODO: Pager
public class CatalogPanel extends ContentPanel implements IPaneWithMenu,
		IDialogYesReceiver, IReloadMsgReceiver, IFieldChangeHandler {

	/** The catalogname. */
	private final String catalogname;

	private Button btnEdit;
	private Button btnDelete;

	/** The editor. */

	private FormPanel formpanel;
	FormData formData = new FormData("98%");

	/** The editformnumber. */
	private Long editformnumber;

	/** The fullname. */
	private HeaderField fullname;

	/** The viewdata. */
	private HashMap<Long, String> viewdata = null;

	/** The fields. */
	private HashMap<String, HeaderField> fields;

	private final Description structuredescription;

	private final GridSelectionModel<CatalogModel> sm = new GridSelectionModel<CatalogModel>();
	private Grid<CatalogModel> grid;
	private ListStore<CatalogModel> store;

	/**
	 * Instantiates a new catalog panel.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param image
	 *            the image
	 */
	public CatalogPanel(final String catalogname) {
		super(new BorderLayout());
		setHeading(M.catalog.titleList());

		this.catalogname = catalogname;
		editformnumber = -1L;

		structuredescription = StructureFactory.getDescription("catalog."
				+ this.catalogname);

		createListForm();

		if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())
				|| canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			createEditorForm();
		}

		MainPanel.setCommInfo(true);
		Services.catalogs.getRecordsForView(catalogname,
				new AsyncCallback<HashMap<Long, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<Long, String> result) {
						MainPanel.setCommInfo(false);
						viewdata = result;
						reloadList();
					}
				});

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
		fields.put(field, hf);
		formpanel.add(hf.getField(), formData);
	}

	/**
	 * Can create.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	protected boolean canCreate(UserRole role) {
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
	protected boolean canEdit(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Can trash.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	protected boolean canTrash(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Clean edit form.
	 */
	private void cleanEditForm() {
		if (fullname != null) {
			fullname.setValue("", false);
			Set<String> names = fields.keySet();
			Iterator<String> i = names.iterator();
			while (i.hasNext()) {
				String name = i.next();
				HeaderField hf = fields.get(name);
				hf.reset();
			}
		}
	}

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {

		formpanel = new FormPanel();
		formpanel.setHeading(M.catalog.titleEditor());
		formpanel.setScrollMode(Scroll.AUTO);
		formpanel.setLabelAlign(LabelAlign.RIGHT);
		formpanel.setLabelWidth(150);

		fullname = DataTypeFactory.addField(M.catalog.labelFullname(),
				"fullname", DataTypes.DT_STRING, "", null);
		formpanel.add(fullname.getField(), formData);
		fields = new HashMap<String, HeaderField>();
		createStructure();

		this.add(formpanel, new BorderLayoutData(LayoutRegion.CENTER));
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {

		sm.setSelectionMode(SelectionMode.SINGLE);

		sm.addSelectionChangedListener(new SelectionChangedListener<CatalogModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<CatalogModel> se) {
				cleanEditForm();
				CatalogModel record = se.getSelectedItem();

				fillEditorForm(record);

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
		column.setId("fullname");
		column.setHeader(M.catalog.labelFullname());
		column.setWidth(150);
		column.setRowHeader(true);
		columns.add(column);

		GridCellRenderer<CatalogModel> gridDate = new GridCellRenderer<CatalogModel>() {

			@Override
			public Object render(CatalogModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<CatalogModel> store, Grid<CatalogModel> grid) {
				Long longdate = (Long) model.get("createdate");
				if (longdate == null) {
					return "";
				}
				return DataTypeFactory.formatter.format(new Date(longdate));
			}
		};
		column = new ColumnConfig();
		column.setId("createdate");
		column.setHeader(M.catalog.labelCreateDate());
		column.setWidth(100);
		column.setRowHeader(true);
		column.setRenderer(gridDate);
		columns.add(column);

		column = new ColumnConfig();
		column.setId("username");
		column.setHeader(M.catalog.labelAuthor());
		column.setWidth(150);
		column.setRowHeader(true);
		columns.add(column);

		ColumnModel cm = new ColumnModel(columns);

		store = new ListStore<CatalogModel>();
		reloadList();

		grid = new Grid<CatalogModel>(store, cm);
		grid.setSelectionModel(sm);
		grid.setAutoExpandColumn("fullname");
		grid.setBorders(true);

		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,
				200, 100, 300);
		northData.setMargins(new Margins(5, 5, 5, 5));
		northData.setSplit(true);
		northData.setCollapsible(true);

		this.add(grid, northData);

		btnEdit = new Button(M.catalog.btnSave());
		btnEdit.setEnabled(false);
		btnEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				HashMap<String, Object> map = getEditorValues();
				if (map == null) {
					map = new HashMap<String, Object>();
				}

				String fname = ((String) fullname.getValue()).trim();
				if (fname.isEmpty()) {
					new AlertDialog(M.catalog.errEmptyFullname()).show();
				} else {
					map.put("fullname", fname);
					// Window.alert(map.toString());
					MainPanel.setCommInfo(true);
					if (editformnumber.equals(-1L)) {
						Services.catalogs.addRecord(catalogname, map,
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
											new AlertDialog(M.catalog
													.msgCreateError()).show();
										}

									}
								});
					} else {
						Services.catalogs.updateRecord(catalogname,
								editformnumber, map,
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
											new AlertDialog(M.catalog
													.msgUpdateError()).show();
										}

									}
								});
					}

				}

			}

		});

		addButton(btnEdit);

		Button btnCancel = new Button(M.catalog.btnCancel());
		btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				CatalogModel map = sm.getSelectedItem();
				if (map != null) {
					fillEditorForm(map);
				} else {
					cleanEditForm();
				}

			}
		});

		addButton(btnCancel);

		btnDelete = new Button(M.catalog.btnDelete());
		btnDelete.setEnabled(false);
		btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				CatalogModel map = sm.getSelectedItem();
				new QuestionDialog(M.catalog.qstDeleteRecord() + " "
						+ (String) map.get("fullname"), CatalogPanel.this,
						"trashrecord", map.get("number")).show();

			}
		});
		addButton(btnDelete);

	}

	/**
	 * Creates the structure.
	 */
	private void createStructure() {
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
					h = new CatalogPanel(helper);
				}

				addField(
						f.getName().getName(
								LocaleInfo.getCurrentLocale().getLocaleName()),
						f.getFieldname(), f.getType(), f.getDefval(), h);
			}
		}
	}

	/**
	 * Fill editor form.
	 * 
	 * @param map
	 *            the map
	 */
	private void fillEditorForm(CatalogModel map) {
		if (map == null) {
			cleanEditForm();
			return;
		}
		editformnumber = map.get("number");
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();

			HeaderField hf = fields.get(name);
			hf.setValue(map.get(name), true);

		}

		fullname.setValue(map.get("fullname"), true);
		if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())) {
			if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())) {
				btnEdit.setEnabled(true);
			} else {
				btnEdit.setEnabled(false);
			}
		}

		if (canTrash(Ballance_autosauler_net.sessionId.getUserrole())
				&& (editformnumber > 0L)) {
			btnDelete.setEnabled(true);
		} else {
			btnDelete.setEnabled(false);
		}

	}

	/**
	 * Gets the catalogname.
	 * 
	 * @return the catalogname
	 */
	public String getCatalogname() {
		return catalogname;
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
			HeaderField hf = fields.get(name);
			map.put(name, hf.getValue());
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
		if (viewdata != null) {
			if (viewdata.containsKey(number)) {
				return viewdata.get(number);
			} else {
				return "UNCKNOWN. TRY RELOAD PAGE.";
			}
		} else {
			return "ERROR: NO DATA";
		}
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

		menu.add(new MenuItem(M.catalog.menuReload(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						reloadList();

					}
				}));

		if (canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			menu.add(new MenuItem(M.catalog.menuCreate(),
					new SelectionListener<MenuEvent>() {

						@Override
						public void componentSelected(MenuEvent ce) {
							sm.deselectAll();
							editformnumber = -1L;
							fullname.reset();
							cleanEditForm();
							btnEdit.setEnabled(true);
							btnDelete.setEnabled(false);

							// openEditor();

						}
					}));
		}

		if (Ballance_autosauler_net.sessionId.getUserrole().isAdmin()) {

			menu.add(new MenuItem(M.catalog.menuScript(),
					new SelectionListener<MenuEvent>() {

						@Override
						public void componentSelected(MenuEvent ce) {
							new ScriptEditor("catalog." + catalogname,
									CatalogPanel.this);

						}
					}));
		}

		menubar.add(new MenuBarItem(M.menu.menubarCatalog(), menu));
		return menubar;

	}

	/**
	 * Gets the select box.
	 * 
	 * @param selectednumber
	 *            the selectednumber
	 * @return the select box
	 */
	public CatalogSelector getSelectBox(Long selectednumber) {
		return new CatalogSelector(catalogname, selectednumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IFieldChangeHandler#handleFieldChange
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void handleFieldChange(String tag, String newvalue) {
		// String s = "Catalog " + catalogname + ". eval pair " + tag
		// + " with new value " + newvalue.toString();
		//

		HashMap<String, String> map = new HashMap<String, String>();

		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			HeaderField hf = fields.get(name);
			map.put(name, hf.getValueAsString());
		}
		String fname = ((String) fullname.getValue()).trim();
		map.put("fullname", fname);

		HashMap<String, Integer> types = new HashMap<String, Integer>();
		types.put("fullname", DataTypes.DT_STRING);
		Iterator<Field> j = structuredescription.get().iterator();
		while (j.hasNext()) {
			Field f = j.next();
			types.put(f.getFieldname(), f.getType());
		}

		// Window.alert(map.toString());

		MainPanel.setCommInfo(true);
		Services.scripts.evalOnChange("catalog." + catalogname, "OnChange",
				tag, map, types, new AsyncCallback<HashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);

						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						MainPanel.setCommInfo(false);
						if (result == null) {
							return;
						}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String)
	 */
	@Override
	public void onDialogYesButtonClick(String tag, Object tag2) {
		if (tag.equals("trashrecord")) {
			MainPanel.setCommInfo(true);
			Services.catalogs.trashRecord(catalogname, (Long) tag2,
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
								new AlertDialog(M.catalog.msgTrashError())
										.show();
							}

						}
					});
		}

	}

	/**
	 * Reload list.
	 */
	@Override
	public void reloadList() {
		cleanEditForm();
		if (btnEdit != null) {
			btnEdit.setEnabled(false);
			btnDelete.setEnabled(false);
		}
		store.removeAll();
		CatalogModel.load(store, catalogname);
	}
}
