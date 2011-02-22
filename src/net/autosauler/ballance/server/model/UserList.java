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

package net.autosauler.ballance.server.model;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.UserRole;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class UserList.
 */
public class UserList {

	/** The Constant collectionname. */
	final private static String collectionname = "registredusers";

	/**
	 * Creates the default records if no users in database.
	 * 
	 * @param db
	 *            the db
	 */
	public static void createDefaultRecords(DB db) {

		if (db != null) {
			DBCollection coll = db.getCollection(collectionname);
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
				coll.createIndex(new BasicDBObject("istrash", 1));
			}
		}
	}

	/**
	 * Gets the users list.
	 * 
	 * @return the users
	 */
	public static net.autosauler.ballance.shared.UserList getUsers() {
		return getUsers(false);
	}

	/**
	 * Gets the users list.
	 * 
	 * @param fromtrash
	 *            the fromtrash
	 * @return the users
	 */
	private static net.autosauler.ballance.shared.UserList getUsers(
			boolean fromtrash) {
		net.autosauler.ballance.shared.UserList list = new net.autosauler.ballance.shared.UserList();

		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection(collectionname);
			BasicDBObject query = new BasicDBObject();
			query.put("istrash", fromtrash);
			DBCursor cur = coll.find(query);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				User dbuser = new User(myDoc);
				net.autosauler.ballance.shared.User user = dbuser.getProxy();
				list.addUser(user);
			}

		}

		return list;
	}

	/**
	 * Gets the users list from trash.
	 * 
	 * @return the users from trash
	 */
	public static net.autosauler.ballance.shared.UserList getUsersFromTrash() {
		return getUsers(true);
	}

	/**
	 * Instantiates a new user list.
	 */
	public UserList() {

	}
}
