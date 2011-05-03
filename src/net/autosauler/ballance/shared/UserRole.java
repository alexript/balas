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

/**
 * The Class UserRole.
 */
public class UserRole implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2003622597617550245L;

	/** The Constant ROLE_GUEST. */
	public static final int ROLE_GUEST = 0;

	/** The Constant ROLE_ADMIN. */
	public static final int ROLE_ADMIN = 1;

	/** The Constant ROLE_DOCUMENTS. */
	public static final int ROLE_DOCUMENTS = 2;

	/** The Constant ROLE_FINANCES. */
	public static final int ROLE_FINANCES = 4;

	/** The Constant ROLE_MANAGER. */
	public static final int ROLE_MANAGER = 8;

	/** The role. */
	private int role = ROLE_GUEST;

	/**
	 * Instantiates a new user role.
	 */
	public UserRole() {
		setRole(ROLE_GUEST);
	}

	/**
	 * Instantiates a new user role.
	 * 
	 * @param therole
	 *            the therole
	 */
	public UserRole(Integer therole) {
		if (therole == null) {
			therole = ROLE_GUEST;
		}
		setRole(therole);
	}

	/**
	 * Instantiates a new user role.
	 * 
	 * @param therole
	 *            the therole
	 */
	public UserRole(UserRole therole) {
		setRole(therole.getRole());
	}

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public int getRole() {
		return role;
	}

	/**
	 * Checks for access.
	 * 
	 * @param chkrole
	 *            the chkrole
	 * @return true, if successful
	 */
	public boolean hasAccess(UserRole chkrole) {
		return (role & chkrole.getRole()) > 0;
	}

	/**
	 * Checks if is admin.
	 * 
	 * @return true, if is admin
	 */
	public boolean isAdmin() {
		return (role & ROLE_ADMIN) == ROLE_ADMIN;
	}

	/**
	 * Checks if is documents.
	 * 
	 * @return true, if is documents
	 */
	public boolean isDocuments() {
		return (role & ROLE_DOCUMENTS) == ROLE_DOCUMENTS;
	}

	/**
	 * Checks if is finances.
	 * 
	 * @return true, if is finances
	 */
	public boolean isFinances() {
		return (role & ROLE_FINANCES) == ROLE_FINANCES;
	}

	/**
	 * Checks if is guest.
	 * 
	 * @return true, if is guest
	 */
	public boolean isGuest() {
		return role == ROLE_GUEST;
	}

	/**
	 * Checks if is manager.
	 * 
	 * @return true, if is manager
	 */
	public boolean isManager() {
		return (role & ROLE_MANAGER) == ROLE_MANAGER;
	}

	/**
	 * Removes the admin.
	 */
	public void removeAdmin() {
		if (isAdmin()) {
			role = role ^ ROLE_ADMIN;
		}
	}

	/**
	 * Removes the documents.
	 */
	public void removeDocuments() {
		if (isDocuments()) {
			role = role ^ ROLE_DOCUMENTS;
		}
	}

	/**
	 * Removes the finances.
	 */
	public void removeFinances() {
		if (isFinances()) {
			role = role ^ ROLE_FINANCES;
		}
	}

	/**
	 * Removes the manager.
	 */
	public void removeManager() {
		if (isManager()) {
			role = role ^ ROLE_MANAGER;
		}
	}

	/**
	 * Sets the admin.
	 */
	public void setAdmin() {
		role = role | ROLE_ADMIN;
	}

	/**
	 * Sets the documents.
	 */
	public void setDocuments() {
		role = role | ROLE_DOCUMENTS;
	}

	/**
	 * Sets the finances.
	 */
	public void setFinances() {
		role = role | ROLE_FINANCES;
	}

	/**
	 * Sets the guest.
	 */
	public void setGuest() {
		role = ROLE_GUEST;
	}

	/**
	 * Sets the manager.
	 */
	public void setManager() {
		role = role | ROLE_MANAGER;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the new role
	 */
	public void setRole(int role) {
		this.role = role;
	}

}
