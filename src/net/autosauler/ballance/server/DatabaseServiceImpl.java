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

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.DatabaseService;
import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class DatabaseServiceImpl.
 */
public class DatabaseServiceImpl extends RemoteServiceServlet implements
		DatabaseService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6110913528303260672L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.DatabaseService#dropDatabase()
	 */
	@Override
	public boolean dropDatabase() {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);

		if (role.isAdmin()) {
			result = Database.recreateDb();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DatabaseService#dumpDatabase(java.lang
	 * .String)
	 */
	@Override
	public void dumpDatabase(String filename) {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);

		if (role.isAdmin()) {
			Database.dumpdatabase(HttpUtilities.getUserDomain(httpSession),
					HttpUtilities.getUserLogo(httpSession), filename);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.DatabaseService#getSettings()
	 */
	@Override
	public HashMap<String, String> getSettings() {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		return Database
				.getAllSettings(HttpUtilities.getUserDomain(httpSession));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DatabaseService#restoreDatabase(java.lang
	 * .String)
	 */
	@Override
	public void restoreDatabase(String filename) {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);

		if (role.isAdmin()) {
			Database.restoredatabase(HttpUtilities.getUserDomain(httpSession),
					HttpUtilities.getUserLogo(httpSession), filename);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DatabaseService#setSettings(java.util.
	 * HashMap)
	 */
	@Override
	public void setSettings(HashMap<String, String> newvalues) {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);

		if (role.isAdmin()) {
			Database.setSettings(newvalues);
		}

	}

}
