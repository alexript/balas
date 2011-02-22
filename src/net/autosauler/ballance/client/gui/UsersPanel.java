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

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

/**
 * The Class UsersPanel.
 */
public class UsersPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The root. */
	private static VerticalPanel root = null;

	/** The l. */
	private static UsersMessages l;

	/** The cell table. */
	@UiField(provided = true)
	static CellTable<User> cellTable;

	/**
	 * The pager used to change the range of data.
	 */
	@UiField(provided = true)
	static SimplePager pager;

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
		// Column<User, Boolean> checkColumn = new Column<User, Boolean>(
		// new CheckboxCell(true, false)) {
		// @Override
		// public Boolean getValue(User object) {
		// // Get the value from the selection model.
		// return selectionModel.isSelected(object);
		// }
		// };
		// cellTable.addColumn(checkColumn,
		// SafeHtmlUtils.fromSafeConstant("<br/>"));
		// cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Full name. ----------------------------------------------------------
		Column<User, String> firstNameColumn = new Column<User, String>(
				new EditTextCell()) {
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
		firstNameColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			@Override
			public void update(int index, User object, String value) {
				// Called when the user changes the value.
				object.setUsername(value);
				UsersDatabase.get().refreshDisplays();
			}
		});
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

		Column<User, Boolean> activeColumn = new Column<User, Boolean>(
				new CheckboxCell()) {
			@Override
			public Boolean getValue(User object) {
				return object.isActive();
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
		final SelectionModel<User> selectionModel = new MultiSelectionModel<User>(
				User.KEY_PROVIDER);
		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<User> createCheckboxManager());

		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		UsersDatabase.get().addDataDisplay(cellTable);
		root.add(cellTable);
		root.add(pager);

	}

	/**
	 * Instantiates a new users panel.
	 */
	public UsersPanel() {
		l = GWT.create(UsersMessages.class);
		root = new VerticalPanel();
		root.setWidth("100%");
		initWidget(root);
		UsersDatabase.get().getUsers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public Widget getPaneMenu() {
		MenuBar menu = new MenuBar();

		menu.addItem(l.menuAddUser(), new Command() {
			@Override
			public void execute() {
				new EditUserDialog(UsersPanel.this);
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
			UsersDatabase.get().getUsers();
		}

	}
}
