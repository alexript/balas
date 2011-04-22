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

package net.autosauler.ballance.server;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.UsersService;
import net.autosauler.ballance.shared.User;
import net.autosauler.ballance.shared.UserList;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class UsersServiceImpl.
 */
public class UsersServiceImpl extends RemoteServiceServlet implements
		UsersService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2706987676142358826L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.UsersService#createUser(net.autosauler
	 * .ballance.shared.User)
	 */
	@Override
	public boolean createUser(User user) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (role.isAdmin()) {
			HttpServletRequest request = getThreadLocalRequest();
			String urlAddress = request.getRequestURL().toString();
			try {
				URL url = new URL(urlAddress);
				String hostname = url.getHost().trim();
				user.setDomain(hostname);
			} catch (MalformedURLException e) {
				user.setDomain("127.0.0.1");
			}

			net.autosauler.ballance.server.model.User dbuser = new net.autosauler.ballance.server.model.User(
					user);
			result = dbuser.addNewUser();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.UsersService#getTrashedUsers()
	 */
	@Override
	public UserList getTrashedUsers() {
		UserList list = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (role.isAdmin()) {
			list = net.autosauler.ballance.server.model.UserList
					.getUsersFromTrash(HttpUtilities.getUserDomain(httpSession));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.UsersService#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String login) {
		net.autosauler.ballance.shared.User user = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (role.isAdmin()) {
			net.autosauler.ballance.server.model.User dbuser = net.autosauler.ballance.server.model.User
					.find(login, HttpUtilities.getUserDomain(httpSession));

			if (dbuser != null) {
				user = dbuser.getProxy();
			}
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.UsersService#getUsers()
	 */
	@Override
	public UserList getUsers() {
		UserList list = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (role.isAdmin()) {
			list = net.autosauler.ballance.server.model.UserList
					.getUsers(HttpUtilities.getUserDomain(httpSession));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.UsersService#trashUser(java.lang.String)
	 */
	@Override
	public boolean trashUser(String login) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (role.isAdmin()) {
			result = net.autosauler.ballance.server.model.User.trashUser(login,
					HttpUtilities.getUserDomain(httpSession));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.UsersService#updateUser(net.autosauler
	 * .ballance.shared.User)
	 */
	@Override
	public boolean updateUser(User user) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (role.isAdmin()) {
			result = net.autosauler.ballance.server.model.User.updateUser(user);
		}
		return result;
	}

}
