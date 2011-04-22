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

package net.autosauler.ballance.client;

import net.autosauler.ballance.shared.User;
import net.autosauler.ballance.shared.UserList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface UsersServiceAsync.
 */
public interface UsersServiceAsync {

	/**
	 * Creates the user.
	 * 
	 * @param user
	 *            the user
	 * @param callback
	 *            the callback
	 */
	void createUser(User user, AsyncCallback<Boolean> callback);

	/**
	 * Gets the trashed users.
	 * 
	 * @param callback
	 *            the callback
	 */
	void getTrashedUsers(AsyncCallback<UserList> callback);

	/**
	 * Gets the user.
	 * 
	 * @param login
	 *            the login
	 * @param callback
	 *            the callback
	 */
	void getUser(String login, AsyncCallback<User> callback);

	/**
	 * Gets the users.
	 * 
	 * @param callback
	 *            the callback
	 */
	void getUsers(AsyncCallback<UserList> callback);

	/**
	 * Trash user.
	 * 
	 * @param login
	 *            the login
	 * @param callback
	 *            the callback
	 */
	void trashUser(String login, AsyncCallback<Boolean> callback);

	/**
	 * Update user.
	 * 
	 * @param user
	 *            the user
	 * @param callback
	 *            the callback
	 */
	void updateUser(User user, AsyncCallback<Boolean> callback);

}
