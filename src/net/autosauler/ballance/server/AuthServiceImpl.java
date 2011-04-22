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

import net.autosauler.ballance.client.AuthService;
import net.autosauler.ballance.client.SessionId;
import net.autosauler.ballance.server.model.User;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class AuthServiceImpl.
 */
public class AuthServiceImpl extends RemoteServiceServlet implements
		AuthService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5272457632267707581L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.AuthService#chkAuth(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public SessionId chkAuth(String login, String password) {
		login = login.trim();
		password = password.trim();
		HttpServletRequest request = getThreadLocalRequest();
		String urlAddress = request.getRequestURL().toString();
		String hostname = "127.0.0.1";
		try {
			URL url = new URL(urlAddress);
			hostname = url.getHost().trim();

		} catch (MalformedURLException e) {
			Log.error(e.getMessage());
		}

		boolean valid = false;

		String username = "Anonymous";
		UserRole userrole = new UserRole();
		Long uid = -1L;

		User user = User.find(login, hostname);
		if (user != null) {
			valid = user.isValidUser(password) && user.isActive()
					&& !user.isTrash();
			if (valid) {
				username = user.getUsername();
				userrole.setRole(user.getUserroleAsInt());
				uid = user.getUid();

				HttpSession httpSession = getThreadLocalRequest().getSession();
				httpSession.setMaxInactiveInterval(1000 * 60 * 60);
				SessionId sessionid = new SessionId();
				sessionid.setSessionId(httpSession.getId());
				sessionid.setUsername(username);
				httpSession.setAttribute("username", username);
				sessionid.setUserrole(userrole);
				httpSession.setAttribute("userrole", userrole.getRole());
				sessionid.setUid(uid);
				httpSession.setAttribute("uid", uid);
				httpSession.setAttribute("login", login);
				httpSession.setAttribute("domain", hostname);
				return sessionid;

			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.AuthService#logoff()
	 */
	@Override
	public void logoff() {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		httpSession.removeAttribute("login");
		httpSession.removeAttribute("domain");
		httpSession.removeAttribute("uid");
		httpSession.removeAttribute("username");
		httpSession.removeAttribute("userrole");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.AuthService#session(net.autosauler.ballance
	 * .client.SessionId)
	 */
	@Override
	public SessionId session(SessionId sessionId) {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		if (httpSession != null) {
			try {
				sessionId.setSessionId(httpSession.getId());
				String username = (String) httpSession.getAttribute("username");
				// System.err.println("stored username: " + username);
				if (username == null) {
					username = "Anonymous";
				}
				sessionId.setUsername(username);
				UserRole userrole = HttpUtilities.getUserRole(httpSession);
				sessionId.setUserrole(userrole);
				Long uid = (Long) httpSession.getAttribute("uid");
				if (uid == null) {
					uid = -1L;
				}
				sessionId.setUid(uid);
			} catch (IllegalStateException e) {
				System.err.println(e.getMessage());
				// sessionId.setUsername(e.getMessage());
				sessionId.setSessionId("");
			}
			return sessionId;
		}// end if(result == null)
		return null;
	}

}
