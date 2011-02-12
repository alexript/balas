/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client;

import java.io.Serializable;

import net.autosauler.ballance.shared.UserRole;

/**
 * The Class SessionId.
 */
public class SessionId implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2546318251991425119L;

	/** The session id. */
	private String sessionId = "";

	/** The username. */
	private String username = "";

	/** The userrole. */
	private UserRole userrole = new UserRole();

	/** The uid. */
	private Long uid = (long) -1;

	/**
	 * Sets the session.
	 * 
	 * @param session
	 *            the new session
	 */
	public void setSession(SessionId session) {
		setSessionId(session.getSessionId());
		setUsername(session.getUsername());
		setUserrole(session.getUserrole());
		setUid(session.getUid());
	}

	/**
	 * Reset.
	 */
	public void reset() {
		sessionId = "";
		username = "Anonymous";
		userrole = new UserRole();
		uid = -1L;
	}

	/**
	 * Sets the session id.
	 * 
	 * @param sessionId
	 *            the new session id
	 */
	public void setSessionId(String sessionId) {
		if (sessionId == null) {
			this.sessionId = "";
		} else {
			this.sessionId = sessionId;
		}
	}// end setSessionId

	/**
	 * Gets the session id.
	 * 
	 * @return the session id
	 */
	public String getSessionId() {
		return sessionId;
	}// end getSessionId

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the userrole.
	 * 
	 * @param userrole
	 *            the new userrole
	 */
	public void setUserrole(UserRole userrole) {
		this.userrole.setRole(userrole.getRole());
	}

	/**
	 * Gets the userrole.
	 * 
	 * @return the userrole
	 */
	public UserRole getUserrole() {
		return userrole;
	}

	/**
	 * Sets the uid.
	 * 
	 * @param uid
	 *            the new uid
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * Gets the uid.
	 * 
	 * @return the uid
	 */
	public Long getUid() {
		return uid;
	}
}
