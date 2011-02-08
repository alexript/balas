package net.autosauler.ballance.server.model;

import net.autosauler.ballance.server.crypt.BCrypt;
import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.UserRole;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class User {
	private String login;
	private String hash;
	private String username;
	private UserRole userrole;
	private Long uid;

	public User() {

	}

	public User(DBObject myDoc) {
		userrole = new UserRole();
		setLogin((String) myDoc.get("login"));
		setHash((String) myDoc.get("hash"));
		setUsername((String) myDoc.get("fullname"));
		userrole.setRole((Integer) myDoc.get("roles"));
		setUid(0L);
	}

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

	public boolean isValidUser(String chkpassword) {
		return BCrypt.checkpw(chkpassword, hash);
	}


	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUserrole(UserRole userrole) {
		this.userrole = userrole;
	}

	public int getUserroleAsInt() {
		return userrole.getRole();
	}

	public UserRole getUserrole() {
		return userrole;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getUid() {
		return uid;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setPassword(String password) {
		this.hash = BCrypt.hashpw(password, BCrypt.gensalt());
	}

}
