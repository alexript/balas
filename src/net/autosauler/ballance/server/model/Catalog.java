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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class Catalog.
 * 
 * @author alexript
 */
public class Catalog {

	/** The Constant CATALOGTABLE. */
	private static final String CATALOGTABLE = "cat_";

	/** The catalogname. */
	private final String catalogname;

	/** The domain. */
	private final String domain;

	/** The username. */
	private final String username;

	/**
	 * Instantiates a new catalog.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public Catalog(String name, String domain, String username) {
		catalogname = CATALOGTABLE + name.trim().toLowerCase();
		this.domain = domain;
		this.username = username;
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();
				i.put("number", 1);
				coll.createIndex(i);

				i.put("domain", 1);
				coll.createIndex(i);

				i.put("fullname", 1);
				i.put("trash", 1);
				coll.createIndex(i);

			}
			Database.release();
		}
	}

	/**
	 * All records.
	 * 
	 * @return the sets the
	 */
	public Set<Long> allRecords() {
		Set<Long> numbers = new HashSet<Long>();

		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put("domain", domain);
			q.put("trash", false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put("fullname", 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				numbers.add((Long) myDoc.get("number"));
			}
			Database.release();
		}

		return numbers; // numbers of records

	}

	/**
	 * Creates the record.
	 * 
	 * @param record
	 *            the record
	 * @return true, if successful
	 */
	private boolean createRecord(ICatalogRecord record) {

		boolean result = false;
		if (username != null) {
			record.setAuthor(username);
		}
		record.setDomain(domain);
		record.setNumber(findLastNumber());
		record.setCreateDate(new Date());
		record.restore();

		DB db = Database.get();
		if (db != null) {
			BasicDBObject doc = (BasicDBObject) record.store(null);
			if (doc != null) {
				Database.retain();
				DBCollection coll = db.getCollection(catalogname);
				coll.insert(doc);
				Database.release();
				result = true;
			}
		}

		return result;
	}

	/**
	 * Find last number.
	 * 
	 * @return the long
	 */
	private Long findLastNumber() {
		Long last = 0L;

		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject query = new BasicDBObject();
			query.put("$query", new BasicDBObject("domain", domain));
			query.put("$orderby", new BasicDBObject("number", -1));

			DBObject doc = null;
			try {
				doc = coll.findOne(query);
			} catch (com.mongodb.MongoException e) {
				last = 1L;
			}
			if (doc != null) {
				last = (Long) doc.get("number") + 1L;
			}
			Database.release();
		}

		if (last.equals(0L)) {
			last = 1L;
		}

		return last;
	}

	/**
	 * Gets the record.
	 * 
	 * @param number
	 *            the number
	 * @return the record
	 */
	public DBObject getRecord(Long number) {
		DBObject doc = null;
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject query = new BasicDBObject();
			query.put("domain", domain);
			query.put("number", number);

			doc = coll.findOne(query);
			Database.release();
		}

		return doc;
	}

	/**
	 * Gets the select data.
	 * 
	 * @return the select data
	 */
	public HashMap<String, Long> getSelectData() {
		HashMap<String, Long> map = new HashMap<String, Long>();
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put("domain", domain);
			q.put("trash", false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put("fullname", 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				map.put((String) myDoc.get("fullname"),
						(Long) myDoc.get("number"));
			}
			Database.release();
		}
		return map;
	}

	/**
	 * Gets the view data.
	 * 
	 * @return the view data
	 */
	public HashMap<Long, String> getViewData() {
		HashMap<Long, String> map = new HashMap<Long, String>();
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put("domain", domain);
			q.put("trash", false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put("fullname", 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				map.put((Long) myDoc.get("number"),
						(String) myDoc.get("fullname"));
			}
			Database.release();
		}
		return map;
	}

	/**
	 * Save.
	 * 
	 * @param record
	 *            the record
	 * @return true, if successful
	 */
	public boolean save(ICatalogRecord record) {
		boolean result = false;

		if (record.getNumber() == 0) {
			result = createRecord(record);
		} else {
			result = updateRecord(record);
		}
		return result;
	}

	/**
	 * Update record.
	 * 
	 * @param record
	 *            the record
	 * @return true, if successful
	 */
	private boolean updateRecord(ICatalogRecord record) {
		boolean result = false;
		DBObject doc = getRecord(record.getNumber());
		if (doc == null) {
			result = createRecord(record);
		} else {
			doc = record.store(doc);
			DB db = Database.get();
			if (db != null) {
				Database.retain();
				DBCollection coll = db.getCollection(catalogname);
				coll.save(doc);
				Database.release();
				result = true;
			}
		}
		return result;
	}
}
