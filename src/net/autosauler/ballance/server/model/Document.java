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
 * The Class Document.
 * 
 * @author alexript
 */
public abstract class Document {

	/** The Constant TABLEPREFIX. */
	private final static String TABLEPREFIX = "doc_";

	/** The tablename. */
	private final String tablename;

	/** The documentname. */
	private String documentname;

	/** The domain. */
	private String domain;

	/** The username. */
	private String username;

	/** The number. */
	private Long number;

	/** The createdate. */
	private Date createdate;

	/** The active. */
	private boolean active;

	/** The activationdate. */
	private Date activationdate;

	/**
	 * Instantiates a new document.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public Document(String name, String domain, Long number) {
		tablename = TABLEPREFIX + name;
		initStruct();
		setDocumentname(name);
		setDomain(domain);
		get(number);
	}

	/**
	 * Instantiates a new document.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public Document(String name, String domain, String username) {
		tablename = TABLEPREFIX + name;
		initStruct();
		setDocumentname(name);
		setDomain(domain);
		setUsername(username);
		setDefaultValues();
	}

	/**
	 * Activation.
	 */
	public void activation() {
		if (!isActive()) {
			setActive(true);
			setActivationdate(new Date()); // document activation
			save();
		}
	}

	/**
	 * Adds the fields to map.
	 * 
	 * @param map
	 *            the map
	 * @return the hash map
	 */
	protected abstract HashMap<String, Object> addFieldsToMap(
			HashMap<String, Object> map);

	/**
	 * Creates the record.
	 * 
	 * @return true, if successful
	 */
	private boolean createRecord() {

		boolean result = false;
		setNumber(findLastNumber());
		setCreatedate(new Date());

		DB db = Database.get();
		if (db != null) {
			BasicDBObject doc = (BasicDBObject) store(null);
			if (doc != null) {
				Database.retain();
				DBCollection coll = db.getCollection(tablename);
				coll.insert(doc);
				Database.release();
				result = true;
			}
		}

		return result;
	}

	/**
	 * Fill fields from map.
	 * 
	 * @param map
	 *            the map
	 */
	protected abstract void fillFieldsFromMap(HashMap<String, Object> map);

	/**
	 * Find all.
	 * 
	 * @return the sets the
	 */
	public Set<Long> findAll() {
		Set<Long> numbers = new HashSet<Long>();

		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(tablename);
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put("domain", domain);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put("number", 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				numbers.add((Long) myDoc.get("number"));
			}
			Database.release();
		}

		return numbers;
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
			DBCollection coll = db.getCollection(tablename);
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
	 * From map.
	 * 
	 * @param map
	 *            the map
	 */
	public void fromMap(HashMap<String, Object> map) {
		fillFieldsFromMap(map);
	}

	/**
	 * Gets the.
	 * 
	 * @param number
	 *            the number
	 */
	private void get(Long number) {
		DBObject doc = getRecord(number);
		if (doc != null) {
			load(doc);
		} else {
			setDefaultValues();
		}
	}

	/**
	 * Gets the activationdate.
	 * 
	 * @return the activationdate
	 */
	public Date getActivationdate() {
		return activationdate;
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
	 * Gets the documentname.
	 * 
	 * @return the documentname
	 */
	public String getDocumentname() {
		return documentname;
	}

	/**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Gets the fields.
	 * 
	 * @param doc
	 *            the doc
	 * @return the fields
	 */
	protected abstract DBObject getFields(DBObject doc);

	/**
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}

	/**
	 * Gets the record.
	 * 
	 * @param number
	 *            the number
	 * @return the record
	 */
	private DBObject getRecord(Long number) {
		DBObject doc = null;
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(tablename);
			BasicDBObject query = new BasicDBObject();
			query.put("domain", domain);
			query.put("number", number);

			doc = coll.findOne(query);
			Database.release();
		}

		return doc;
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
	 * Inits the struct.
	 */
	private void initStruct() {
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(tablename);
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();
				i.put("number", 1);
				coll.createIndex(i);

				i.put("domain", 1);
				coll.createIndex(i);

				i.put("active", 1);
				coll.createIndex(i);

			}
			Database.release();
		}
	}

	/**
	 * Checks if is active.
	 * 
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Load.
	 * 
	 * @param doc
	 *            the doc
	 */
	private void load(DBObject doc) {
		setNumber((Long) doc.get("number"));
		setUsername((String) doc.get("author"));
		setCreatedate((Date) doc.get("createdate"));
		setDomain((String) doc.get("domain"));
		setActive((Boolean) doc.get("active"));
		setActivationdate((Date) doc.get("acdate"));

		setFields(doc);
	}

	/**
	 * Save.
	 * 
	 * @return true, if successful
	 */
	public boolean save() {

		boolean result = false;

		if (getNumber() == 0) {
			result = createRecord();
		} else {
			result = updateRecord();
		}
		return result;
	}

	/**
	 * Sets the activationdate.
	 * 
	 * @param activationdate
	 *            the activationdate to set
	 */
	public void setActivationdate(Date activationdate) {
		this.activationdate = activationdate;
	}

	/**
	 * Sets the active.
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Sets the createdate.
	 * 
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	/**
	 * Sets the default values.
	 */
	private void setDefaultValues() {
		setNumber(0L);
		setCreatedate(new Date());
		setActive(false);
		setActivationdate(new Date());
	}

	/**
	 * Sets the documentname.
	 * 
	 * @param documentname
	 *            the documentname to set
	 */
	public void setDocumentname(String documentname) {
		this.documentname = documentname;
	}

	/**
	 * Sets the domain.
	 * 
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Sets the fields.
	 * 
	 * @param doc
	 *            the new fields
	 */
	protected abstract void setFields(DBObject doc);

	/**
	 * Sets the number.
	 * 
	 * @param number
	 *            the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Store.
	 * 
	 * @param doc
	 *            the doc
	 * @return the dB object
	 */
	private DBObject store(DBObject doc) {
		if (doc == null) {
			doc = new BasicDBObject();
		}
		doc.put("domain", getDomain());
		doc.put("author", getUsername());
		doc.put("number", getNumber());
		doc.put("createdate", getCreatedate());
		doc.put("active", isActive());
		doc.put("acdate", getActivationdate());
		doc = getFields(doc);
		return doc;
	}

	/**
	 * To map.
	 * 
	 * @return the hash map
	 */
	public HashMap<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("number", getNumber());
		map.put("author", getUsername());
		map.put("createdate", getCreatedate().getTime());
		map.put("active", isActive());
		map.put("activationdate", getActivationdate());

		map = addFieldsToMap(map);

		return map;
	}

	/**
	 * Unactivation.
	 */
	public void unactivation() {
		if (isActive()) {
			setActive(false);
			save();
		}
	}

	/**
	 * Update record.
	 * 
	 * @return true, if successful
	 */
	private boolean updateRecord() {
		boolean result = false;
		DBObject doc = getRecord(getNumber());
		if (doc == null) {
			result = createRecord();
		} else {
			doc = store(doc);
			DB db = Database.get();
			if (db != null) {
				Database.retain();
				DBCollection coll = db.getCollection(tablename);
				coll.save(doc);
				Database.release();
				result = true;
			}
		}
		return result;
	}
}
