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
 * The Class AbstractStructuredData.
 * 
 * @author alexript
 */
public abstract class AbstractStructuredData {
	// TODO: store structure per domain
	// TODO: edit structure per domain

	/** The tableprefix. */
	private final String tableprefix;

	/** The tablesuffix. */
	private final String tablesuffix;

	/** The tablename. */
	private final String tablename;

	/** The struct. */
	protected Structure struct;

	/** The values. */
	protected StructValues values;

	/** The Constant fieldname_domain. */
	protected static final String fieldname_domain = "domain";

	/** The Constant fieldname_username. */
	protected static final String fieldname_username = "username";

	/** The Constant fieldname_number. */
	protected static final String fieldname_number = "number";
	/** The Constant fieldname_createdate. */
	protected static final String fieldname_createdate = "createdate";

	/** The Constant fieldname_trash. */
	protected static final String fieldname_trash = "trash";

	/**
	 * Instantiates a new structured data.
	 * 
	 * @param prefix
	 *            the prefix
	 * @param suffix
	 *            the suffix
	 * @param domain
	 *            the domain
	 */
	public AbstractStructuredData(String prefix, String suffix, String domain) {
		tableprefix = prefix;
		tablesuffix = suffix;
		tablename = prefix + "_" + suffix;
		initMetaStructure();
		initDBStruct();
		setDomain(domain);
		restore();
	}

	/**
	 * Adds the find all orders.
	 * 
	 * @param o
	 *            the o
	 */
	protected abstract void addFindAllOrders(final BasicDBObject o);

	/**
	 * Adds the find all query parameters.
	 * 
	 * @param q
	 *            the q
	 */
	protected abstract void addFindAllQueryParameters(final BasicDBObject q);

	/**
	 * Adds the find last number params.
	 * 
	 * @param w
	 *            the w
	 */
	protected abstract void addFindLastNumberParams(final BasicDBObject w);

	/**
	 * Adds the get record params.
	 * 
	 * @param query
	 *            the query
	 */
	protected abstract void addGetRecordParams(final BasicDBObject query);

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
				DBCollection coll = db.getCollection(getTableName());
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

		sb.append("<" + getPrefix() + " name=\"" + getSuffix() + "\">\n");

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
		sb.append("</" + getPrefix() + ">\n");

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
			DBCollection coll = db.getCollection(getTableName());
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put(fieldname_domain, getDomain());
			q.put(fieldname_trash, false);

			addFindAllQueryParameters(q);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put(fieldname_number, 1);
			addFindAllOrders(o);
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
	protected Long findLastNumber() {
		Long last = 0L;

		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(getTableName());
			BasicDBObject w = new BasicDBObject();
			w.put(fieldname_domain, getDomain());
			addFindLastNumberParams(w);
			BasicDBObject query = new BasicDBObject();
			query.put("$query", w);
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
			if (!name.equals(fieldname_createdate)
					&& !name.equals(fieldname_domain)
					&& !name.equals(fieldname_number)
					&& !name.equals(fieldname_username)) {
				if (map.containsKey(name)) {
					values.setObject(name, map.get(name));
				}
			}
		}
	}

	/**
	 * Gets the.
	 * 
	 * @param number
	 *            the number
	 */
	protected void get(Long number) {
		DBObject doc = getRecord(number);
		if (doc != null) {
			load(doc);
		}
	}

	/**
	 * Gets the createdate.
	 * 
	 * @return the createdate
	 */
	public Date getCreatedate() {
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
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public Long getNumber() {
		return (Long) values.get(fieldname_number);
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return tableprefix;
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
			DBCollection coll = db.getCollection(getTableName());
			BasicDBObject query = new BasicDBObject();
			query.put(fieldname_domain, getDomain());
			addGetRecordParams(query);
			query.put(fieldname_number, number);

			doc = coll.findOne(query);
			Database.release();
			onGetRecord(number);
		}

		return doc;
	}

	/**
	 * Gets the suffix.
	 * 
	 * @return the suffix
	 */
	public String getSuffix() {
		return tablesuffix;
	}

	/**
	 * Gets the table name.
	 * 
	 * @return the table name
	 */
	public String getTableName() {
		return tablename;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return (String) values.get(fieldname_username);
	}

	/**
	 * Inits the db struct.
	 */
	private void initDBStruct() {
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(getTableName());
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();
				i.put(fieldname_number, 1);
				coll.createIndex(i);

				i.put(fieldname_domain, 1);
				coll.createIndex(i);

				i.put(fieldname_trash, 1);
				coll.createIndex(i);

				onInitDbStruct(i, coll);

			}
			Database.release();
		}

	}

	/**
	 * Inits the global structure (in classes Catalog or Document or some other
	 * abstract class).
	 */
	protected abstract void initGlobalStructure();

	/**
	 * Inits the document structure.
	 */
	private void initMetaStructure() {
		struct = new Structure();

		struct.add(fieldname_domain, DataTypes.DT_DOMAIN, "127.0.0.1");
		struct.add(fieldname_username, DataTypes.DT_STRING, "uncknown");
		struct.add(fieldname_number, DataTypes.DT_LONG, new Long(0L));
		struct.add(fieldname_createdate, DataTypes.DT_DATE, new Date());
		struct.add(fieldname_trash, DataTypes.DT_BOOLEAN, new Boolean(false));

		initGlobalStructure();

		initStructure();

		values = new StructValues(struct);
	}

	/**
	 * Inits the structure for The class.
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
	protected void load(DBObject doc) {
		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			values.set(name, doc.get(name));
		}
	}

	/**
	 * On get record.
	 * 
	 * @param number
	 *            the number
	 */
	protected abstract void onGetRecord(Long number);

	/**
	 * On init db struct.
	 * 
	 * @param i
	 *            the i
	 * @param coll
	 *            the coll
	 */
	protected abstract void onInitDbStruct(final BasicDBObject i,
			final DBCollection coll);

	/**
	 * Restore.
	 */
	public void restore() {
		values.set(fieldname_trash, new Boolean(false));

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
	 * Sets the createdate.
	 * 
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(Date createdate) {
		values.set(fieldname_createdate, createdate);
	}

	/**
	 * Sets the domain.
	 * 
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		values.set(fieldname_domain, domain);
	}

	/**
	 * Sets the number.
	 * 
	 * @param number
	 *            the number to set
	 */
	public void setNumber(Long number) {
		values.set(fieldname_number, number);
		onGetRecord(number);
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		values.set(fieldname_username, username);
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
			map.put(name, values.getObject(name));
		}

		return map;
	}

	/**
	 * Trash.
	 */
	public void trash() {
		values.set(fieldname_trash, new Boolean(true));
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
				DBCollection coll = db.getCollection(getTableName());
				coll.save(doc);
				Database.release();
				result = true;
			}
		}
		return result;
	}

}
