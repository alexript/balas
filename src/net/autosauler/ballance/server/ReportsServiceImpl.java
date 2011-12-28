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
import java.util.List;

import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.ReportsService;
import net.autosauler.ballance.server.reports.Query;
import net.autosauler.ballance.shared.ReportFormField;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author alexript
 * 
 */
public class ReportsServiceImpl extends RemoteServiceServlet implements
		ReportsService {

	private static final long serialVersionUID = -8467355644390741008L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.ReportsService#generateReport(java.lang
	 * .String, java.util.HashMap)
	 */
	@Override
	public String generateReport(String reportname,
			HashMap<String, String> params) {
		UserRole role = getRole();
		String domain = getDomain();
		String login = getLogin();
		if (!role.isGuest()) {
			Query q = new Query(domain, login, reportname, params);
			return q.getResult();
		}
		return null;
	}

	/**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
	private String getDomain() {
		HttpSession httpSession = getSession();
		String domain = HttpUtilities.getUserDomain(httpSession);
		return domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.ReportsService#getFields(java.lang.String)
	 */
	@Override
	public List<ReportFormField> getFields(String reportname) {
		UserRole role = getRole();
		String domain = getDomain();
		String login = getLogin();
		if (!role.isGuest()) {
			Query q = new Query(domain, login, reportname);

			return q.getFormDescription().getFields();
		}
		return null;
	}

	/**
	 * Gets the login.
	 * 
	 * @return the login
	 */
	private String getLogin() {
		HttpSession httpSession = getSession();
		String login = HttpUtilities.getUserLogo(httpSession);
		return login;
	}

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	private UserRole getRole() {
		HttpSession httpSession = getSession();
		UserRole role = HttpUtilities.getUserRole(httpSession);
		return role;
	}

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	private HttpSession getSession() {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		return httpSession;
	}

}
