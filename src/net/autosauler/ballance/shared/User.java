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
	String login;
	
	/** The domain. */
	String domain;
	
	/** The username. */
	String username;
	
	/** The userrole. */
	UserRole userrole;
	
	/** The createdate. */
	Date createdate;
	
	/** The active. */
	boolean active;
	
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
		login = user.getLoginWithoutDomain();
		domain = user.getDomainOfLogin();
		username = user.getUsername();
		userrole = new UserRole(user.getUserrole());
		createdate = user.getCreatedate();
		active = user.isActive();
	}
}
