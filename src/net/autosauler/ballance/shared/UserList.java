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
import java.util.ArrayList;
import java.util.List;

/**
 * The Class UserList.
 */
public class UserList implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -388597750089950183L;

	/** The list. */
	private List<User> list = null;

	/**
	 * Instantiates a new user list.
	 */
	public UserList() {
		list = new ArrayList<User>();
	}

	/**
	 * Adds the user.
	 * 
	 * @param user
	 *            the user
	 */
	public void addUser(User user) {
		if (getList() == null) {
			setList(new ArrayList<User>());
		}
		getList().add(user);
	}

	/**
	 * @return the list
	 */
	public List<User> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<User> list) {
		this.list = list;
	}
}
