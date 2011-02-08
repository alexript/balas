package net.autosauler.ballance.server.model;

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

	/** The username. */
	private String username;

	/** The userrole. */
	private UserRole userrole;

	/** The uid. */
	private Long uid;

	/**
	 * Instantiates a new user.
	 */
	public User() {

	}

	/**
	 * Instantiates a new user.
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
		setUid(0L);
	}

	/**
	 * Find.
	 * 
	 * @param login
	 *            the login
	 * @return the user
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
	 * Creates the.
	 */
	public void create() {
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection("registredusers");

			BasicDBObject doc = new BasicDBObject();

			doc.put("hash", hash);
			doc.put("login", login);
			doc.put("fullname", username);
			doc.put("roles", userrole);

			coll.insert(doc);

		}
	}

	/**
	 * Checks if is valid user.
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

}
