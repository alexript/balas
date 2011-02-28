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

package net.autosauler.ballance.client.databases;

import net.autosauler.ballance.client.UsersService;
import net.autosauler.ballance.client.UsersServiceAsync;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.client.gui.UsersPanel;
import net.autosauler.ballance.shared.User;
import net.autosauler.ballance.shared.UserList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

/**
 * The Class UsersDatabase.
 * 
 * @author alexript
 */
public class UsersDatabase {

	/**
	 * The singleton instance of the database.
	 */
	private static UsersDatabase instance;

	/**
	 * Get the singleton instance of the contact database.
	 * 
	 * @return the singleton instance
	 */
	public static UsersDatabase get() {
		if (instance == null) {
			instance = new UsersDatabase();

		}
		return instance;
	}

	/**
	 * The provider that holds the list of contacts in the database.
	 */
	private final ListDataProvider<User> dataProvider = new ListDataProvider<User>();

	/**
	 * Construct a new users database.
	 */
	private UsersDatabase() {

	}

	/**
	 * Add a display to the database. The current range of interest of the
	 * display will be populated with data.
	 * 
	 * @param display
	 *            a {@Link HasData}.
	 */
	public void addDataDisplay(HasData<User> display) {
		dataProvider.addDataDisplay(display);
	}

	/**
	 * Gets the data provider.
	 * 
	 * @return the data provider
	 */
	public ListDataProvider<User> getDataProvider() {
		return dataProvider;
	}

	/**
	 * Gets the users.
	 * 
	 * @return the users
	 */
	public void getUsers(boolean fromtrash) {
		MainPanel.setCommInfo(true);
		UsersServiceAsync service = GWT.create(UsersService.class);
		if (fromtrash) {
			service.getTrashedUsers(new AsyncCallback<UserList>() {

				@Override
				public void onFailure(Throwable caught) {
					MainPanel.setCommInfo(false);
					new AlertDialog("Communication error", caught.getMessage())
							.show();
				}

				@Override
				public void onSuccess(UserList result) {
					MainPanel.setCommInfo(false);
					if (result == null) {
						get().getDataProvider().setList(
								new UserList().getList());
					} else {
						get().getDataProvider().setList(result.getList());
					}
					UsersPanel.get().refreshPane();
					refreshDisplays();
				}
			});
		} else {
			service.getUsers(new AsyncCallback<UserList>() {

				@Override
				public void onFailure(Throwable caught) {
					MainPanel.setCommInfo(false);
					new AlertDialog("Communication error", caught.getMessage())
							.show();
				}

				@Override
				public void onSuccess(UserList result) {
					MainPanel.setCommInfo(false);
					if (result == null) {
						get().getDataProvider().setList(
								new UserList().getList());
					} else {
						get().getDataProvider().setList(result.getList());
					}
					UsersPanel.get().refreshPane();
					refreshDisplays();
				}
			});
		}

	}

	/**
	 * Refresh all displays.
	 */
	public void refreshDisplays() {

		dataProvider.refresh();
	}
}
