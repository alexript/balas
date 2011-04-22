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

import com.google.gwt.view.client.ProvidesKey;

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

	/** The password. */
	private String password;

	/** The Constant KEY_PROVIDER. */
	public static final ProvidesKey<User> KEY_PROVIDER = new ProvidesKey<User>() {

		/**
		 * Gets the key.
		 * 
		 * @param item
		 *            the item
		 * @return the key
		 */
		@Override
		public Object getKey(User item) {
			return item == null ? null : item.getId();
		}
	};

	/**
	 * Instantiates a new user.
	 */
	public User() {

	}

	/**
	 * Gets the createdate.
	 * 
	 * @return the createdate
	 */
	public Date getCreatedate() {
		return createdate;
	}

	/**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return getLogin();
	}

	/**
	 * Gets the login.
	 * 
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
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
	 * Gets the userrole.
	 * 
	 * @return the userrole
	 */
	public UserRole getUserrole() {
		return userrole;
	}

	/**
	 * Inits the as default.
	 */
	public void initAsDefault() {
		login = "";
		domain = "127.0.0.1";
		username = "";
		userrole = new UserRole();
		createdate = new Date();
		active = false;
		password = "";
	}

	/**
	 * Checks if is active.
	 * 
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Sets the createdate.
	 * 
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	/**
	 * Sets the domain.
	 * 
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Sets the login.
	 * 
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Sets the userrole.
	 * 
	 * @param userrole
	 *            the userrole to set
	 */
	public void setUserrole(UserRole userrole) {
		this.userrole = new UserRole(userrole);
	}
}
