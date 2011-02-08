package net.autosauler.ballance.server.mongodb;

import java.net.UnknownHostException;
import java.util.List;

import net.autosauler.ballance.server.model.UserList;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * The Class Database.
 */
public class Database {

	/** The Constant host. */
	private static final String host = "127.0.0.1";

	/** The Constant port. */
	private static final int port = 27017;

	/** The Constant database. */
	private static final String database = "blncatslrnt";

	/** The Constant user. */
	private static final String user = "user";

	/** The Constant password. */
	private static final String password = "user";

	/** The Constant adminuser. */
	private static final String adminuser = "root";

	/** The Constant adminpassword. */
	private static final String adminpassword = "root";

	/** The mongodatabase. */
	private static DB mongodatabase = null;

	/**
	 * Instantiates a new database.
	 */
	public Database() {

	}

	/**
	 * Gets the.
	 * 
	 * @return the dB
	 */
	public static DB get() {
		if (mongodatabase == null) {
			try {
				initConnection();
			} catch (UnknownHostException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return mongodatabase;
	}

	/**
	 * Inits the connection.
	 * 
	 * @throws UnknownHostException
	 *             the unknown host exception
	 * @throws MongoException
	 *             the mongo exception
	 */
	private static void initConnection() throws UnknownHostException,
			MongoException {
		Mongo m = new Mongo(host, port);
		DB db = m.getDB("admin");
		boolean auth = db.authenticate(adminuser, adminpassword.toCharArray());
		if (auth) {
			List<String> databases = m.getDatabaseNames();
			db = m.getDB(database);

			if (!databases.contains(database)) {
				db.addUser(user, password.toCharArray());
			}

			auth = db.authenticate(user, password.toCharArray());
			if (auth) {
				mongodatabase = db;

				// check users and if none - create admin
				UserList.createDefaultRecords(db);

			}
		}
	}
}
