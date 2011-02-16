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
package net.autosauler.ballance.server.mongodb;

import java.net.UnknownHostException;
import java.util.List;

import net.autosauler.ballance.server.model.UserList;
import net.autosauler.ballance.server.util.Mutex;

import com.allen_sauer.gwt.log.client.Log;
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

	/** The mongo. */
	private static Mongo mongo = null;
	/** The mongodatabase. */
	private static DB mongodatabase = null;

	/** The lock. */
	private static Mutex lock = new Mutex();

	/**
	 * Close connection.
	 */
	public static synchronized void close() {
		try {
			lock.acquire();
			mongodatabase = null;
			if (mongo != null) {
				mongo.close();
				mongo = null;
			}

		} catch (InterruptedException e) {
			Log.error(e.getMessage());
			// e.printStackTrace();
		}
		lock.release();
	}

	/**
	 * Gets the.
	 * 
	 * @return the dB
	 */
	public static synchronized DB get() {
		if (mongodatabase == null) {
			try {
				initConnection();
			} catch (UnknownHostException e) {
				close();
				Log.error(e.getMessage());
				// e.printStackTrace();
			} catch (MongoException e) {
				close();
				Log.error(e.getMessage());
				// e.printStackTrace();
			} catch (InterruptedException e) {
				close();
				Log.error(e.getMessage());
				// e.printStackTrace();
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
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private static synchronized void initConnection()
	// TODO: fix creating connection per session.
	// this singleton not work for multiple class loaders (gwt or jetty case?)
	// tomcat tests needed
			throws UnknownHostException, MongoException, InterruptedException {
		lock.acquire();
		try {
			if (mongo == null) {
				mongo = new Mongo(host, port);
			}
		} catch (com.mongodb.MongoInternalException e) {
			lock.release();
			close();
			throw (e);
		}
		DB db = mongo.getDB("admin");
		boolean auth = db.authenticate(adminuser, adminpassword.toCharArray());
		if (auth) {
			List<String> databases = mongo.getDatabaseNames();
			db = mongo.getDB(database);

			if (!databases.contains(database)) {
				db.addUser(user, password.toCharArray());
			}

			auth = db.authenticate(user, password.toCharArray());
			if (auth) {
				mongodatabase = db;

				// check users and if none - create admin
				UserList.createDefaultRecords(db);

			} else {
				lock.release();
				close();
			}
		} else {
			lock.release();
			close();
		}
		lock.release();
	}

	/**
	 * Recreate db.
	 * 
	 * @return true, if successful
	 */
	public static synchronized boolean recreateDb() {

		if (mongodatabase == null) {
			return false;
		}

		try {
			lock.acquire();
		} catch (InterruptedException e) {
			Log.error(e.getMessage());
			// e.printStackTrace();
			return false;
		}

		try {
			mongodatabase.dropDatabase();
			close();
		} catch (MongoException e) {
			lock.release();
			Log.error(e.getMessage());
			// e.printStackTrace();
			return false;
		}

		lock.release();
		return true;
	}

	/**
	 * Instantiates a new database.
	 */
	public Database() {

	}
}
