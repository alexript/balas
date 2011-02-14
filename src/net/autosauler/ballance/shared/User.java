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

package net.autosauler.ballance.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class User.
 */
public class User implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7092920614208715347L;

	/** The login. */
	private String login;

	/** The domain. */
	private String domain;

	/** The username. */
	private String username;

	/** The userrole. */
	private UserRole userrole = null;

	/** The createdate. */
	private Date createdate;

	/** The active. */
	private boolean active;

	/**
	 * Instantiates a new user.
	 */
	public User() {

	}

	/**
	 * Instantiates a new user.
	 * 
	 * @param user
	 *            the user
	 */
	public User(net.autosauler.ballance.server.model.User user) {
		setLogin(user.getLoginWithoutDomain());
		setDomain(user.getDomainOfLogin());
		setUsername(user.getUsername());
		setUserrole(new UserRole(user.getUserrole()));
		setCreatedate(user.getCreatedate());
		setActive(user.isActive());
	}

	/**
	 * @return the createdate
	 */
	public Date getCreatedate() {
		return createdate;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the userrole
	 */
	public UserRole getUserrole() {
		return userrole;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param userrole
	 *            the userrole to set
	 */
	public void setUserrole(UserRole userrole) {
		this.userrole = new UserRole(userrole);
	}
}
