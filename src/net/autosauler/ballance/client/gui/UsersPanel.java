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
import java.util.List;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.client.model.UsersModel;
import net.autosauler.ballance.shared.UserRole;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The Class UsersPanel.
 */
public class UsersPanel extends ContentPanel implements IPaneWithMenu,
		IDialogYesReceiver {

	private Grid<UsersModel> grid;
	private ListStore<UsersModel> store;
	private CheckBoxSelectionModel<UsersModel> sm;

	/** The trashstate. */
	private boolean trashstate = false;

	private Button del;
	private Button edit;

	private static UsersPanel impl = null;

	public static UsersPanel get() {
		if (impl == null) {
			impl = new UsersPanel();
		}
		return impl;
	}

	/**
	 * Instantiates a new users panel.
	 */
	private UsersPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		addButtons();
		buildTable();

	}

	private void addButtons() {
		edit = new Button();
		edit.setText(M.tools.btnEdit());
		edit.setEnabled(false);
		edit.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				UsersModel user = sm.getSelectedItem();
				if (user != null) {

					new EditUserDialog((String) user.get("id"), UsersPanel.this);
				}

			}

		});

		del = new Button();
		del.setText(M.tools.btnDelete());
		del.setEnabled(false);
		del.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				UsersModel user = sm.getSelectedItem();
				if (user != null) {
					new QuestionDialog(M.users.qstTrashUser() + " "
							+ user.get("username"), UsersPanel.this,
							"trashuser").show();
				}

			}
		});

		addButton(edit);
		addButton(del);

	}

	private void buildTable() {

		sm = new CheckBoxSelectionModel<UsersModel>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		sm.addSelectionChangedListener(new SelectionChangedListener<UsersModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<UsersModel> se) {
				UsersModel user = se.getSelectedItem();

				if (user != null) {
					edit.setEnabled(true);
					del.setEnabled(true);
				} else {
					edit.setEnabled(false);
					del.setEnabled(false);

				}

			}
		});

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		columns.add(sm.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId("username");
		column.setHeader(M.users.userName());
		column.setWidth(150);
		column.setRowHeader(true);
		columns.add(column);

		column = new ColumnConfig();
		column.setId("roles");
		column.setHeader(M.users.columnRoles());
		column.setWidth(90);
		column.setRowHeader(true);
		GridCellRenderer<UsersModel> gridRole = new GridCellRenderer<UsersModel>() {

			@Override
			public Object render(UsersModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<UsersModel> store, Grid<UsersModel> grid) {

				return new RolesWidget((UserRole) model.get("roles"))
						.toString();
			}
		};
		column.setAlignment(HorizontalAlignment.CENTER);
		column.setRenderer(gridRole);
		columns.add(column);

		column = new ColumnConfig();
		column.setId("createdate");
		column.setHeader(M.users.crateDate());
		column.setWidth(200);
		column.setRowHeader(true);
		columns.add(column);

		GridCellRenderer<UsersModel> gridActive = new GridCellRenderer<UsersModel>() {

			@Override
			public Object render(UsersModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<UsersModel> store, Grid<UsersModel> grid) {

				Boolean isactive = (Boolean) model.get("active");
				if (isactive) {
					return AbstractImagePrototype.create(Images.menu.Ok())
							.createImage();

				}
				return AbstractImagePrototype.create(Images.menu.Cancel())
						.createImage();

			}
		};
		column = new ColumnConfig();
		column.setId("active");
		column.setHeader(M.users.isActive());
		column.setWidth(100);
		column.setRowHeader(true);
		column.setRenderer(gridActive);
		columns.add(column);

		ColumnModel cm = new ColumnModel(columns);

		store = new ListStore<UsersModel>();
		loadList();

		grid = new Grid<UsersModel>(store, cm);
		grid.setSelectionModel(sm);
		grid.setAutoExpandColumn("username");
		add(grid);

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

		menu.add(new MenuItem(M.users.menuAddUser(),
				new SelectionListener<MenuEvent>() { // create new user

					@Override
					public void componentSelected(MenuEvent ce) {
						new EditUserDialog(UsersPanel.this);

					}
				}));

		menu.add(new MenuItem(M.users.menuReload(),
				new SelectionListener<MenuEvent>() { // reload users list

					@Override
					public void componentSelected(MenuEvent ce) {
						loadList();

					}
				}));

		menu.add(new MenuItem(M.users.menuNotTrashedUsers(),
				new SelectionListener<MenuEvent>() { // Live

					@Override
					public void componentSelected(MenuEvent ce) {
						trashstate = false;
						loadList();

					}
				}));

		menu.add(new MenuItem(M.users.menuTrashedUsers(),
				new SelectionListener<MenuEvent>() { // Trashed
					// users

					@Override
					public void componentSelected(MenuEvent ce) {
						trashstate = true;
						loadList();

					}
				}));

		menubar.add(new MenuBarItem(M.menu.itemUsers(), menu));
		return menubar;
	}

	private void loadList() {
		store.removeAll();
		UsersModel.load(trashstate, store);

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
		if (tag.equals("reload")) {
			loadList();
		} else if (tag.equals("trashuser")) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {

				UsersModel user = sm.getSelectedItem();
				if (user != null) {

					Services.users.trashUser((String) user.get("id"),
							new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									new AlertDialog(caught).show();

								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										loadList();
									} else {
										new AlertDialog(M.users.logTrashError())
												.show();
									}

								}
							});
				}
			}

		}

	}

}
