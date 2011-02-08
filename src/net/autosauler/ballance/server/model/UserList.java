package net.autosauler.ballance.server.model;

import net.autosauler.ballance.shared.UserRole;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class UserList {
	public UserList() {

	}

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
				user.create();
				
				coll.createIndex(new BasicDBObject("login", 1));
			}
		}
	}
}
