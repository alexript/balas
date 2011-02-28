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

import java.util.Comparator;
import java.util.Date;

import net.autosauler.ballance.client.databases.UsersDatabase;
import net.autosauler.ballance.shared.User;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class UsersPanel.
 */
public class UsersPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The root. */
	private static VerticalPanel root = null;

	/** The l. */
	private static UsersMessages l;
	private static MenuImages images;
	private static ToolsMessages tools;

	/** The cell table. */
	@UiField(provided = true)
	static CellTable<User> cellTable;

	/** The trashstate. */
	private boolean trashstate = false;

	/**
	 * The pager used to change the range of data.
	 */
	@UiField(provided = true)
	static SimplePager pager;

	private static Button del;
	private static Button edit;

	/**
	 * Inits the table columns.
	 * 
	 * @param selectionModel
	 *            the selection model
	 * @param sortHandler
	 *            the sort handler
	 */
	private static void initTableColumns(
			final SelectionModel<User> selectionModel,
			ListHandler<User> sortHandler) {

		// Checkbox column. This table will uses a checkbox column for
		// selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to
		// enable
		// mouse selection.
		Column<User, Boolean> checkColumn = new Column<User, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(User object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		cellTable.addColumn(checkColumn,
				SafeHtmlUtils.fromSafeConstant("<br/>"));
		cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Full name. ----------------------------------------------------------
		Column<User, String> firstNameColumn = new Column<User, String>(
				new TextCell()) {
			@Override
			public String getValue(User object) {
				return object.getUsername();
			}
		};
		firstNameColumn.setSortable(true);
		sortHandler.setComparator(firstNameColumn, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getUsername().compareTo(o2.getUsername());
			}
		});
		cellTable.addColumn(firstNameColumn, l.userName());

		cellTable.setColumnWidth(firstNameColumn, 150, Unit.PCT);

		// Registration date.
		// ----------------------------------------------------------
		Column<User, Date> createdateColumn = new Column<User, Date>(
				new DateCell()) {
			@Override
			public Date getValue(User object) {
				return object.getCreatedate();
			}
		};
		createdateColumn.setSortable(true);
		sortHandler.setComparator(createdateColumn, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getCreatedate().compareTo(o2.getCreatedate());
			}
		});
		cellTable.addColumn(createdateColumn, l.crateDate());

		cellTable.setColumnWidth(createdateColumn, 100, Unit.PCT);

		// Active flag.
		// ----------------------------------------------------------

		Column<User, ImageResource> activeColumn = new Column<User, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(User user) {
				if (user.isActive()) {
					return images.Ok();
				}
				return images.Cancel();
			}
		};
		activeColumn.setSortable(true);
		sortHandler.setComparator(activeColumn, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getCreatedate().compareTo(o2.getCreatedate());
			}
		});

		cellTable.addColumn(activeColumn, l.isActive());

		cellTable.setColumnWidth(activeColumn, 40, Unit.PCT);

	}

	/**
	 * Refresh pane.
	 */
	public static void refreshPane() {
		root.clear();
		// Set a key provider that provides a unique key for each contact. If
		// key is
		// used to identify contacts when fields (such as the name and address)
		// change.
		cellTable = new CellTable<User>(User.KEY_PROVIDER);

		cellTable.setWidth("100%", true);

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		ListHandler<User> sortHandler = new ListHandler<User>(UsersDatabase
				.get().getDataProvider().getList());
		cellTable.addColumnSortHandler(sortHandler);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(cellTable);

		// Add a selection model so we can select cells.
		final SelectionModel<User> selectionModel = new SingleSelectionModel<User>(
				User.KEY_PROVIDER);

		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<User> createCheckboxManager());

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						User user = ((SingleSelectionModel<User>) selectionModel)
								.getSelectedObject();
						if (user != null) {
							edit.setEnabled(true);
							del.setEnabled(true);
						} else {
							edit.setEnabled(false);
							del.setEnabled(false);

						}
					}
				});

		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		UsersDatabase.get().addDataDisplay(cellTable);
		root.add(cellTable);
		HorizontalPanel bottom = new HorizontalPanel();
		bottom.add(pager);

		edit = new Button();
		edit.setText(tools.btnEdit());
		edit.setEnabled(false);
		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				User user = ((SingleSelectionModel<User>) selectionModel)
						.getSelectedObject();
				if (user != null) {
					Log.error("Edit " + user.getId());
				}

			}
		});
		bottom.add(edit);

		del = new Button();
		del.setText(tools.btnDelete());
		del.setEnabled(false);
		del.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				User user = ((SingleSelectionModel<User>) selectionModel)
						.getSelectedObject();
				if (user != null) {
					Log.error("Delete " + user.getId());
				}

			}
		});
		bottom.add(del);

		root.add(bottom);

	}

	/**
	 * Instantiates a new users panel.
	 */
	public UsersPanel() {
		l = GWT.create(UsersMessages.class);
		images = GWT.create(MenuImages.class);
		tools = GWT.create(ToolsMessages.class);
		trashstate = false;
		root = new VerticalPanel();
		root.setWidth("100%");
		initWidget(root);
		reloadList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public Widget getPaneMenu() {
		MenuBar menu = new MenuBar();

		menu.addItem(l.menuAddUser(), new Command() { // create new user
					@Override
					public void execute() {
						new EditUserDialog(UsersPanel.this);
					}
				});

		menu.addItem(l.menuReload(), new Command() { // reload users list
					@Override
					public void execute() {
						reloadList();
					}
				});

		menu.addItem(l.menuNotTrashedUsers(), new Command() { // Live users
					@Override
					public void execute() {
						trashstate = false;
						reloadList();
					}
				});

		menu.addItem(l.menuTrashedUsers(), new Command() { // Trashed users
					@Override
					public void execute() {
						trashstate = true;
						reloadList();
					}
				});

		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String)
	 */
	@Override
	public void onDialogYesButtonClick(String tag) {
		if (tag.equals("reload")) {
			reloadList();
		}

	}

	/**
	 * Reload list.
	 */
	private void reloadList() {
		UsersDatabase.get().getUsers(trashstate);
	}
}
