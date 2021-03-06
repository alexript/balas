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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface UsersService.
 */
@RemoteServiceRelativePath("users")
public interface UsersService extends RemoteService {

	/**
	 * Creates the user.
	 * 
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean createUser(User user);

	/**
	 * Gets the trashed users.
	 * 
	 * @return the trashed users
	 */
	public UserList getTrashedUsers();

	/**
	 * Gets the user.
	 * 
	 * @param login
	 *            the login
	 * @return the user
	 */
	public User getUser(String login);

	/**
	 * Gets the users.
	 * 
	 * @return the users
	 */
	public UserList getUsers();

	/**
	 * Trash user.
	 * 
	 * @param login
	 *            the login
	 * @return true, if successful
	 */
	public boolean trashUser(String login);

	/**
	 * Update user.
	 * 
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean updateUser(User user);

}
