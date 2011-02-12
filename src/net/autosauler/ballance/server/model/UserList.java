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

package net.autosauler.ballance.server.model;

import net.autosauler.ballance.shared.UserRole;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * The Class UserList.
 */
public class UserList {

	/**
	 * Instantiates a new user list.
	 */
	public UserList() {

	}

	/**
	 * Creates the default records if no users in database.
	 * 
	 * @param db
	 *            the db
	 */
	public static void createDefaultRecords(DB db) {

		if (db != null) {
			DBCollection coll = db.getCollection("registredusers");
			if (coll.getCount() < 1) {
				UserRole defaultroles = new UserRole();
				defaultroles.setAdmin();

				User user = new User();
				user.setPassword("admin");
				user.setLogin("admin@127.0.0.1");
				user.setUsername("Admin The Great");
				user.setUserrole(defaultroles);
				user.addNewUser();

				coll.createIndex(new BasicDBObject("login", 1));
			}
		}
	}
}
