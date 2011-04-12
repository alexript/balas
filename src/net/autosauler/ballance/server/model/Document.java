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
 * The Class Document.
 * 
 * @author alexript
 */
public abstract class Document implements IScriptableObject {

	/** The Constant TABLEPREFIX. */
	private final static String TABLEPREFIX = "doc_";

	/** The tablename. */
	private final String tablename;

	/** The documentname. */
	private String documentname;

	/** The struct. */
	protected Structure struct;

	/** The values. */
	protected StructValues values;

	/** The Constant fieldname_domain. */
	private static final String fieldname_domain = "domain";

	/** The Constant fieldname_username. */
	private static final String fieldname_username = "username";

	/** The Constant fieldname_number. */
	private static final String fieldname_number = "number";

	/** The Constant fieldname_createdate. */
	private static final String fieldname_createdate = "createdate";

	/** The Constant fieldname_active. */
	private static final String fieldname_active = "active";

	/** The Constant fieldname_activationdate. */
	private static final String fieldname_activationdate = "activationdate";

	/**
	 * Instantiates a new document.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 */
	public Document(String name, String domain) {
		tablename = TABLEPREFIX + name;
		initDocumentStructure();
		setDomain(domain);
	}

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
		initDocumentStructure();
		initDBStruct();
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
		initDocumentStructure();
		initDBStruct();
		setDocumentname(name);
		setDomain(domain);
		setUsername(username);
	}

	/**
	 * Activation.
	 */
	public void activation() {
		if (!isActive()) {
			if (onActivation()) {

				Scripts script = new Scripts(this,
						(String) values.get(fieldname_domain), "document."
								+ documentname);
				script.eval("(onactivate)"); // TODO: do it right

				setActive(true);
				setActivationdate(new Date()); // document activation
			}
		}
	}

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
			q.put(fieldname_domain, values.get(fieldname_domain));
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put(fieldname_number, 1);
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
			DBCollection coll = db.getCollection(tablename);
			BasicDBObject query = new BasicDBObject();
			query.put(
					"$query",
					new BasicDBObject(fieldname_domain, values
							.get(fieldname_domain)));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.IScriptableObject#generateDefaultScript
	 * ()
	 */
	@Override
	public String generateDefaultScript() {
		StringBuilder sb = new StringBuilder();
		sb.append("(def onactivate ())");

		sb.append(onGenerateDefaultScript());

		return sb.toString();
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
			values = new StructValues(struct);
		}
	}

	/**
	 * Gets the.
	 * 
	 * @param numbers
	 *            the numbers
	 * @return the sets the
	 */
	public abstract Set<HashMap<String, Object>> get(Set<Long> numbers);

	/**
	 * Gets the activationdate.
	 * 
	 * @return the activationdate
	 */
	public Date getActivationdate() {
		return (Date) values.get(fieldname_activationdate);
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
			query.put(fieldname_domain, values.get(fieldname_domain));
			query.put(fieldname_number, number);

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
		return (String) values.get(fieldname_username);
	}

	/**
	 * Inits the struct.
	 */
	private void initDBStruct() {
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(tablename);
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();
				i.put(fieldname_number, 1);
				coll.createIndex(i);

				i.put(fieldname_domain, 1);
				coll.createIndex(i);

				i.put(fieldname_active, 1);
				coll.createIndex(i);

			}
			Database.release();
		}
	}

	/**
	 * Inits the document structure.
	 */
	private void initDocumentStructure() {
		struct = new Structure();
		struct.add(fieldname_domain, DataTypes.DT_DOMAIN, "127.0.0.1");
		struct.add(fieldname_username, DataTypes.DT_STRING, "uncknown");
		struct.add(fieldname_number, DataTypes.DT_LONG, new Long(0L));
		struct.add(fieldname_createdate, DataTypes.DT_DATE, new Date());
		struct.add(fieldname_active, DataTypes.DT_BOOLEAN, false);
		struct.add(fieldname_activationdate, DataTypes.DT_DATE, new Date());

		initStructure();

		values = new StructValues(struct);
	}

	/**
	 * Inits the structure.
	 */
	protected abstract void initStructure();

	/**
	 * Checks if is active.
	 * 
	 * @return the active
	 */
	public boolean isActive() {
		return (Boolean) values.get(fieldname_active);
	}

	/**
	 * Load.
	 * 
	 * @param doc
	 *            the doc
	 */
	private void load(DBObject doc) {
		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			values.set(name, doc.get(name));
		}
	}

	/**
	 * On activation.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean onActivation();

	/**
	 * On generate default script.
	 * 
	 * @return the string
	 */
	protected abstract String onGenerateDefaultScript();

	/**
	 * On un activation.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean onUnActivation();

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
		values.set(fieldname_activationdate, activationdate);
	}

	/**
	 * Sets the active.
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		values.set(fieldname_active, active);
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
	 * Unactivation.
	 */
	public void unactivation() {
		if (isActive()) {
			if (onUnActivation()) {
				Scripts script = new Scripts(this,
						(String) values.get(fieldname_domain), "document."
								+ documentname);
				script.eval("(onunactivate)"); // TODO: do it right

				setActive(false);
			}
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