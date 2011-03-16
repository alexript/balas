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

	final private static String USERSTABLE = "registredusers";

	/**
	 * Find user by login.
	 * 
	 * @param login
	 *            the login
	 * @return the user or null if not found
	 */
	public static User find(String login) {
		User user = null;

		DBObject myDoc = findObject(login);
		if (myDoc != null) {
			user = new User(myDoc);
		}

		return user;
	}

	/**
	 * Find.
	 * 
	 * @param login
	 *            the login
	 * @param domain
	 *            the domain
	 * @return the user
	 */
	public static User find(String login, String domain) {
		return find(login.trim() + "@" + domain.trim());
	}

	/**
	 * Find object.
	 * 
	 * @param login
	 *            the login
	 * @return the dB object
	 */
	private static DBObject findObject(String login) {
		DBObject myDoc = null;
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection(USERSTABLE);
			BasicDBObject query = new BasicDBObject();
			query.put("login", login);
			myDoc = coll.findOne(query);
		}

		return myDoc;
	}

	/**
	 * Trash user.
	 * 
	 * @param loginanddomain
	 *            the loginanddomain
	 * @return true, if successful
	 */
	public static boolean trashUser(String loginanddomain) {
		boolean result = false;
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection(USERSTABLE);

			BasicDBObject query = new BasicDBObject();
			query.put("login", loginanddomain);

			BasicDBObject obj = new BasicDBObject();
			obj.put("istrash", true);
			obj.put("isactive", false);

			coll.update(query, new BasicDBObject("$set", obj));

			result = true;

		}

		return result;
	}

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
	 * Instantiates a new user.
	 * 
	 * @param proxy
	 *            the proxy
	 */
	public User(net.autosauler.ballance.shared.User proxy) {
		this.setLogin(proxy.getLogin(), proxy.getDomain());
		setUsername(proxy.getUsername());
		setUserrole(proxy.getUserrole());
		setActive(proxy.isActive());
		setPassword(proxy.getPassword());
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
	 * Creates the new user record.
	 */
	private void create() {
		DB db = Database.get();
		if (db != null) {
			createdate = new Date();
			active = true;
			trash = false;

			DBCollection coll = db.getCollection(USERSTABLE);

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
	 * Gets the createdate.
	 * 
	 * @return the createdate
	 */
	public Date getCreatedate() {
		return createdate;
	}

	/**
	 * Gets the domain of login.
	 * 
	 * @return the domain of login
	 */
	public String getDomainOfLogin() {
		String l = getLogin();
		String[] arr = l.split("@", 2);
		if (arr.length != 2) {
			return "";
		}
		return arr[1];
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
	 * Gets the login.
	 * 
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the login without domain.
	 * 
	 * @return the login without domain
	 */
	public String getLoginWithoutDomain() {
		String l = getLogin();
		String[] arr = l.split("@", 2);
		if (arr.length != 2) {
			return l;
		}
		return arr[0];
	}

	/**
	 * Gets the proxy.
	 * 
	 * @return the proxy
	 */
	public net.autosauler.ballance.shared.User getProxy() {
		net.autosauler.ballance.shared.User user = new net.autosauler.ballance.shared.User();
		user.setLogin(getLoginWithoutDomain());
		user.setDomain(getDomainOfLogin());
		user.setUsername(getUsername());
		user.setUserrole(new UserRole(getUserrole()));
		user.setCreatedate(getCreatedate());
		user.setActive(isActive());
		user.setPassword("");
		return user;
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
	 * Gets the userrole as int.
	 * 
	 * @return the userrole as int
	 */
	public int getUserroleAsInt() {
		return userrole.getRole();
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
	 * Checks if is trash.
	 * 
	 * @return true, if is trash
	 */
	public boolean isTrash() {
		return trash;
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
	 * Update record.
	 * 
	 * @return true, if successful
	 */
	public boolean save() {
		return true;
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
	 * Sets the hash.
	 * 
	 * @param hash
	 *            the new hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
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
	 * Sets the login.
	 * 
	 * @param login
	 *            the login
	 * @param domain
	 *            the domain
	 */
	public void setLogin(String login, String domain) {
		setLogin(login.trim() + "@" + domain.trim());
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		hash = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * Sets the trash.
	 * 
	 * @param trash
	 *            the new trash
	 */
	public void setTrash(boolean trash) {
		this.trash = trash;
		if (trash) {
			setActive(false);
		}
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
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
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
}
