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

import javax.servlet.http.HttpSession;

import net.autosauler.ballance.shared.UserRole;

/**
 * The Class HttpUtilities.
 */
public class HttpUtilities {

	/**
	 * Gets the user domain.
	 * 
	 * @param session
	 *            the session
	 * @return the user domain
	 */
	public static String getUserDomain(HttpSession session) {
		if (session == null) {
			return "127.0.0.1";
		}

		return (String) session.getAttribute("domain");
	}

	/**
	 * Gets the user login.
	 * 
	 * @param session
	 *            the session
	 * @return the user login
	 */
	private static String getUserLogin(HttpSession session) {
		if (session == null) {
			return "uncknown";
		}

		return (String) session.getAttribute("login");
	}

	/**
	 * Gets the user logo.
	 * 
	 * @param session
	 *            the session
	 * @return the user logo
	 */
	public static String getUserLogo(HttpSession session) {
		return getUserLogin(session);

	}

	/**
	 * Gets the user role from session.
	 * 
	 * @param session
	 *            the session
	 * @return the user role
	 */
	public static UserRole getUserRole(HttpSession session) {
		if (session == null) {
			UserRole role = new UserRole();
			role.setGuest();
			return role;
		}
		Integer roleint = (Integer) session.getAttribute("userrole");
		UserRole role = new UserRole();
		if (roleint != null) {
			role.setRole(roleint);
		}
		return role;
	}
}
