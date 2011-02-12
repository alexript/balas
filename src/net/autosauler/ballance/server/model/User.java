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

import java.util.Date;

import net.autosauler.ballance.server.crypt.BCrypt;
import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.UserRole;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * The Class User.
 */
public class User {

	/** The login. */
	private String login;

	/** The hash. */
	private String hash;

	/** The user full name. */
	private String username;

	/** The user role. */
	private UserRole userrole;

	/** The uid. */
	private Long uid;

	/** The user's create date. */
	private Date createdate;

	/** Is user active. */
	private boolean active;

	/** Is user in trash. */
	private boolean trash;

	/**
	 * Instantiates a new user.
	 */
	public User() {

	}

	/**
	 * Instantiates a new user from DB record.
	 * 
	 * @param myDoc
	 *            the my doc
	 */
	public User(DBObject myDoc) {
		userrole = new UserRole();
		setLogin((String) myDoc.get("login"));
		setHash((String) myDoc.get("hash"));
		setUsername((String) myDoc.get("fullname"));
		userrole.setRole((Integer) myDoc.get("roles"));
		createdate = (Date) myDoc.get("createdate");
		active = (Boolean) myDoc.get("isactive");
		trash = (Boolean) myDoc.get("istrash");
		setUid(0L);
	}

	/**
	 * Find user by login.
	 * 
	 * @param login
	 *            the login
	 * @return the user or null if not found
	 */
	public static User find(String login) {
		User user = null;

		DBObject myDoc = null;
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection("registredusers");
			BasicDBObject query = new BasicDBObject();
			query.put("login", login);
			myDoc = coll.findOne(query);
			if (myDoc != null) {
				user = new User(myDoc);
			}
		}

		return user;
	}

	/**
	 * Creates the new user record.
	 */
	private void create() {
		DB db = Database.get();
		if (db != null) {
			createdate = new Date();
			active = true;
			trash = false;

			DBCollection coll = db.getCollection("registredusers");

			BasicDBObject doc = new BasicDBObject();

			doc.put("hash", hash);
			doc.put("login", login);
			doc.put("fullname", username);
			doc.put("roles", userrole.getRole());
			doc.put("createdate", createdate);
			doc.put("isactive", active);
			doc.put("istrash", trash);
			coll.insert(doc);

		}
	}

	/**
	 * Adds the new user to database. If login exists - false;
	 * 
	 * @return true, if successful
	 */
	public boolean addNewUser() {
		User user = User.find(getLogin());
		if (user != null) {
			return false;
		}
		create();
		return true;
	}

	/**
	 * Checks if is valid user password.
	 * 
	 * @param chkpassword
	 *            the chkpassword
	 * @return true, if is valid user
	 */
	public boolean isValidUser(String chkpassword) {
		return BCrypt.checkpw(chkpassword, hash);
	}

	/**
	 * Sets the hash.
	 * 
	 * @param hash
	 *            the new hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * Gets the hash.
	 * 
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

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
		this.userrole = userrole;
	}

	/**
	 * Gets the userrole as int.
	 * 
	 * @return the userrole as int
	 */
	public int getUserroleAsInt() {
		return userrole.getRole();
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

	/**
	 * Sets the login.
	 * 
	 * @param login
	 *            the new login
	 */
	public void setLogin(String login) {
		this.login = login;
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
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.hash = BCrypt.hashpw(password, BCrypt.gensalt());
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
	 * Sets the active.
	 * 
	 * @param active
	 *            the new active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Checks if is active.
	 * 
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the trash.
	 * 
	 * @param trash
	 *            the new trash
	 */
	public void setTrash(boolean trash) {
		this.trash = trash;
	}

	/**
	 * Checks if is trash.
	 * 
	 * @return true, if is trash
	 */
	public boolean isTrash() {
		return trash;
	}

}
