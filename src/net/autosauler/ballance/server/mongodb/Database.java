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
import java.util.HashMap;
import java.util.List;

import net.autosauler.ballance.server.model.Currency;
import net.autosauler.ballance.server.model.GlobalSettings;
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

	// TODO: all connect values must be configurable

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

	/** The lockcounter. */
	private static Integer lockcounter = 0;

	/** The releaser. */
	private static Thread releaser = null;

	/** The retaintimeoutmin. */
	private static int retaintimeoutmin = 5;

	/** The settings. */
	private static GlobalSettings settings = null;

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
		}
		if (releaser != null) {
			releaser = null;
		}
		lockcounter = 0;
		if (settings != null) {
			settings.save();
		}
		lock.release();
	}

	/**
	 * Gets the database.
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
			} catch (MongoException e) {
				close();
				Log.error(e.getMessage());
			} catch (InterruptedException e) {
				close();
				Log.error(e.getMessage());
			}
		}

		return mongodatabase;
	}

	/**
	 * Gets the all settings.
	 * 
	 * @return the all settings
	 */
	public static HashMap<String, String> getAllSettings() {
		if (settings == null) {
			loadSettings();
		}
		return settings.getAll();
	}

	/**
	 * Gets the settings.
	 * 
	 * @return the settings
	 */
	public static GlobalSettings getSettings() {
		return settings;
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
				retain();
				// check settings and create new collection if not exists
				GlobalSettings.createDefaultRecords(db);
				// check users and if none - create admin
				UserList.createDefaultRecords(db);
				// check currency values. If none - load today values from cbr
				Currency.createDefaultRecords(db);
				release();

			} else {
				lock.release();
				close();
			}
		} else {
			lock.release();
			close();
		}
		lock.release();

		loadSettings();

	}

	private static void loadSettings() {
		if (mongodatabase != null) {
			settings = new GlobalSettings();
			retaintimeoutmin = settings.get("database.autoclose.timeout.min",
					retaintimeoutmin);
			settings.save();
		} else {
			settings = new GlobalSettings(false);
		}
	}

	/**
	 * Recreate database (drop and close connection).
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
			return false;
		}

		try {
			Database.retain();
			mongodatabase.dropDatabase();
			close();
		} catch (MongoException e) {
			lock.release();
			Log.error(e.getMessage());
			return false;
		}

		lock.release();
		return true;
	}

	/**
	 * Retain database lock counter.
	 */
	public static synchronized void release() {
		lockcounter--;
		if (lockcounter <= 0) {
			lockcounter = 0;
		}
	}

	/**
	 * Retain database lock counter.
	 */
	public static synchronized void retain() {

		lockcounter++;
		startReleaser();
	}

	/**
	 * Sets the settings.
	 * 
	 * @param newvalues
	 *            the newvalues
	 */
	public static void setSettings(HashMap<String, String> newvalues) {
		settings.set(newvalues);
	}

	/**
	 * Start releaser thread.
	 */
	private static void startReleaser() {
		if (releaser == null) {
			releaser = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (lockcounter > 0) {
							Thread.sleep(retaintimeoutmin * 60 * 1000);
						}
						close();
					} catch (InterruptedException e) {
						Log.error(e.getMessage());
					}

				}
			});
			releaser.start();
		}
	}

	/**
	 * Instantiates a new database.
	 */
	public Database() {

	}
}
