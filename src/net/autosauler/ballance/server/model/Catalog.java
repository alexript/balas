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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.datatypes.DataTypes;
import net.autosauler.ballance.shared.datatypes.StructValues;
import net.autosauler.ballance.shared.datatypes.Structure;

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
public abstract class Catalog {

	/** The Constant CATALOGTABLE. */
	private static final String CATALOGTABLE = "cat_";

	/** The catalogname. */
	private String catalogname;

	/** The Constant fieldname_number. */
	private static final String fieldname_number = "number";

	/** The Constant fieldname_author. */
	private static final String fieldname_author = "authorname";

	/** The Constant fieldname_createdate. */
	private static final String fieldname_createdate = "createdate";

	/** The Constant fieldname_trash. */
	private static final String fieldname_trash = "trash";

	/** The Constant fieldname_domain. */
	private static final String fieldname_domain = "domain";

	/** The Constant fieldname_fullname. */
	private static final String fieldname_fullname = "fullname";

	/** The struct. */
	protected Structure struct;

	/** The values. */
	protected StructValues values;

	/**
	 * Instantiates a new catalog record.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public Catalog(String name, String domain, Long number) {
		initCatalogStructure();
		initDbStruct(name, domain, null);
		get(number);
	}

	/**
	 * Instantiates a new catalog record.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public Catalog(String name, String domain, String username) {
		initCatalogStructure();
		initDbStruct(name, domain, username);
	}

	/**
	 * Creates the record.
	 * 
	 * @return true, if successful
	 */
	private boolean createRecord() {

		boolean result = false;
		setNumber(findLastNumber());
		setCreateDate(new Date());
		restore();

		DB db = Database.get();
		if (db != null) {
			BasicDBObject doc = (BasicDBObject) store(null);
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
	 * Dump.
	 * 
	 * @return the string
	 */
	public String dump() {
		StringBuilder sb = new StringBuilder();

		sb.append("<catalog name=\"" + catalogname + "\">\n");

		sb.append(struct.toString());

		sb.append("<records>\n");

		Set<Long> numbers = findAll();
		Iterator<Long> i = numbers.iterator();
		while (i.hasNext()) {
			Long number = i.next();
			DBObject doc = getRecord(number);
			if (doc != null) {
				load(doc);
				sb.append(values.toString());
			}
		}

		sb.append("</records>\n");
		sb.append("</catalog>\n");

		return sb.toString();
	}

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
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put(fieldname_domain, getDomain());
			q.put(fieldname_trash, false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put(fieldname_fullname, 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				numbers.add((Long) myDoc.get(fieldname_number));
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
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject query = new BasicDBObject();
			query.put("$query",
					new BasicDBObject(fieldname_domain, getDomain()));
			query.put("$orderby", new BasicDBObject(fieldname_number, -1));

			DBObject doc = null;
			try {
				doc = coll.findOne(query);
			} catch (com.mongodb.MongoException e) {
				last = 1L;
			}
			if (doc != null) {
				last = (Long) doc.get(fieldname_number) + 1L;
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
		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			int type = struct.getType(name);
			if (type == DataTypes.DT_BOOLEAN) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_CATALOG) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_CATALOGRECORD) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_CURRENCY) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_DATE) {
				values.set(name, new Date((Long) map.get(name)));
			} else if (type == DataTypes.DT_DOCUMENT) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_DOCUMENTRECORD) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_DOMAIN) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_DOUBLE) {
				values.set(name, Double.parseDouble((String) map.get(name)));
			} else if (type == DataTypes.DT_INT) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_LONG) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_MONEY) {
				values.set(name, Double.parseDouble((String) map.get(name)));
			} else if (type == DataTypes.DT_OBJECT) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_SCRIPT) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_SETTING) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_SETTINGVALUE) {
				values.set(name, map.get(name));
			} else if (type == DataTypes.DT_STRING) {
				values.set(name, map.get(name));
			}
		}
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
		}
	}

	/**
	 * Gets the author.
	 * 
	 * @return the author
	 */
	public String getAuthor() {

		return (String) values.get(fieldname_author);
	}

	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public Date getCreateDate() {

		return (Date) values.get(fieldname_createdate);
	}

	/**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
	public String getDomain() {
		return (String) values.get(fieldname_domain);
	}

	/**
	 * Gets the fullname.
	 * 
	 * @return the fullname
	 */
	public String getFullname() {
		return (String) values.get(fieldname_fullname);
	}

	/**
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public Long getNumber() {

		return (Long) values.get(fieldname_number);
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
			DBCollection coll = db.getCollection(catalogname);
			BasicDBObject query = new BasicDBObject();
			query.put(fieldname_domain, getDomain());
			query.put(fieldname_number, number);

			doc = coll.findOne(query);
			Database.release();
		}
		return doc;
	}

	/**
	 * Gets the select data (pairs name-number).
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
			q.put(fieldname_domain, getDomain());
			q.put(fieldname_trash, false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put(fieldname_fullname, 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				map.put((String) myDoc.get(fieldname_fullname),
						(Long) myDoc.get(fieldname_number));
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
			q.put(fieldname_domain, getDomain());
			q.put(fieldname_trash, false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put(fieldname_fullname, 1);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				map.put((Long) myDoc.get(fieldname_number),
						(String) myDoc.get(fieldname_fullname));
			}
			Database.release();
		}
		return map;
	}

	/**
	 * Inits the catalog structure.
	 */
	private void initCatalogStructure() {

		struct = new Structure();
		struct.add(fieldname_number, DataTypes.DT_LONG, new Long(0L));
		struct.add(fieldname_author, DataTypes.DT_STRING, "uncknown");
		struct.add(fieldname_createdate, DataTypes.DT_DATE, new Date());
		struct.add(fieldname_trash, DataTypes.DT_BOOLEAN, false);
		struct.add(fieldname_domain, DataTypes.DT_DOMAIN, "127.0.0.1");
		struct.add(fieldname_fullname, DataTypes.DT_STRING, "uncknown");

		initStructure();

		values = new StructValues(struct);

	}

	/**
	 * Inits the db struct.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	private void initDbStruct(String name, String domain, String username) {
		catalogname = CATALOGTABLE + name.trim().toLowerCase();
		setDomain(domain);
		setAuthor(username);
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(catalogname);
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();
				i.put(fieldname_number, 1);
				coll.createIndex(i);

				i.put(fieldname_domain, 1);
				coll.createIndex(i);

				i.put(fieldname_fullname, 1);
				i.put(fieldname_trash, 1);
				coll.createIndex(i);

			}
			Database.release();
		}
	}

	/**
	 * Inits the structure.
	 */
	protected abstract void initStructure();

	/**
	 * Checks if is trash.
	 * 
	 * @return true, if is trash
	 */
	public boolean isTrash() {
		return (Boolean) values.get(fieldname_trash);
	}

	/**
	 * Load.
	 * 
	 * @param doc
	 *            the doc
	 */
	public void load(DBObject doc) {
		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			values.set(name, doc.get(name));
		}

	}

	/**
	 * Restore.
	 */
	public void restore() {
		values.set(fieldname_trash, false);

	}

	/**
	 * Restore.
	 * 
	 * @param xmldump
	 *            the xmldump
	 */
	public void restore(String xmldump) {
		// TODO: restore data
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
	 * Sets the author.
	 * 
	 * @param userlogin
	 *            the new author
	 */
	public void setAuthor(String userlogin) {
		values.set(fieldname_author, userlogin);

	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param createdate
	 *            the new creates the date
	 */
	public void setCreateDate(Date createdate) {
		values.set(fieldname_createdate, createdate);
	}

	/**
	 * Sets the domain.
	 * 
	 * @param domain
	 *            the new domain
	 */
	public void setDomain(String domain) {
		values.set(fieldname_domain, domain);
	}

	/**
	 * Sets the fullname.
	 * 
	 * @param fullname
	 *            the new fullname
	 */
	public void setFullname(String fullname) {
		values.set(fieldname_fullname, fullname);
	}

	/**
	 * Sets the number.
	 * 
	 * @param number
	 *            the new number
	 */
	public void setNumber(Long number) {
		values.set(fieldname_number, number);

	}

	/**
	 * Store.
	 * 
	 * @param doc
	 *            the doc
	 * @return the dB object
	 */
	public DBObject store(DBObject doc) {
		if (doc == null) {
			doc = new BasicDBObject();
		}

		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			doc.put(name, values.get(name));
		}
		return doc;

	}

	/**
	 * To map.
	 * 
	 * @return the hash map
	 */
	public HashMap<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			int type = struct.getType(name);
			if (type == DataTypes.DT_BOOLEAN) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_CATALOG) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_CATALOGRECORD) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_CURRENCY) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_DATE) {
				map.put(name, ((Date) values.get(name)).getTime());
			} else if (type == DataTypes.DT_DOCUMENT) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_DOCUMENTRECORD) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_DOMAIN) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_DOUBLE) {
				map.put(name, ((Double) values.get(name)).toString());
			} else if (type == DataTypes.DT_INT) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_LONG) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_MONEY) {
				map.put(name, ((Double) values.get(name)).toString());
			} else if (type == DataTypes.DT_OBJECT) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_SCRIPT) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_SETTING) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_SETTINGVALUE) {
				map.put(name, values.get(name));
			} else if (type == DataTypes.DT_STRING) {
				map.put(name, values.get(name));
			}
		}

		return map;
	}

	/**
	 * Trash.
	 */
	public void trash() {
		values.set(fieldname_trash, true);
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
				DBCollection coll = db.getCollection(catalogname);
				coll.save(doc);
				Database.release();
				result = true;
			}
		}
		return result;
	}
}
