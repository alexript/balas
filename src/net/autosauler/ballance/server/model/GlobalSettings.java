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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class GlobalSettings.
 * 
 * @author alexript
 */
public class GlobalSettings {
	/** The Constant SETTINGSTABLE. */
	private static final String SETTINGSTABLE = "globalsettings";

	/**
	 * Creates the default records.
	 * 
	 * @param db
	 *            the db
	 */
	public static void createDefaultRecords(DB db) {

		if (db != null) {
			DBCollection coll = db.getCollection(SETTINGSTABLE);
			if (coll.getCount() < 1) {

				BasicDBObject rec = new BasicDBObject();
				rec.put("name", "default.record");
				rec.put("val", "default.value");
				coll.insert(rec);

				BasicDBObject i = new BasicDBObject();
				i.put("domain", 1);
				i.put("name", 1);
				coll.createIndex(i);

			}
		}
	}

	private String domain = "127.0.0.1";

	/** The values. */
	private HashMap<String, String> values = null;

	/** The changed. */
	private boolean changed;

	/**
	 * Instantiates a new global settings.
	 * 
	 * @param isloadfromdatabase
	 *            the isloadfromdatabase
	 */
	public GlobalSettings(boolean isloadfromdatabase, String domain) {
		this.domain = domain;
		values = new HashMap<String, String>();
		if (isloadfromdatabase) {
			loadFromDatabase();
		}
	}

	/**
	 * Instantiates a new global settings.
	 */
	public GlobalSettings(String domain) {
		this.domain = domain;
		values = new HashMap<String, String>();
		loadFromDatabase();
	}

	/**
	 * Dump.
	 * 
	 * @return the string
	 */
	public String dump() {
		StringBuilder sb = new StringBuilder();

		sb.append("<settings>\n");

		Set<String> names = values.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			String val = values.get(name);
			sb.append("<value name=\"" + name + "\" val=\"" + val + "\"/>\n");
		}

		sb.append("</settings>\n");

		return sb.toString();
	}

	/**
	 * Gets the Integer value.
	 * 
	 * @param name
	 *            the name
	 * @param defval
	 *            the defval
	 * @return the int
	 */
	public int get(String name, Integer defval) {
		String v = get(name, defval.toString()).trim();
		return Integer.parseInt(v);
	}

	/**
	 * Gets the String value.
	 * 
	 * @param name
	 *            the name
	 * @param defval
	 *            the defval
	 * @return the string
	 */
	public String get(String name, String defval) {
		if (!values.containsKey(name)) {
			set(name, defval);
		}

		return values.get(name);
	}

	/**
	 * Gets the all setting records.
	 * 
	 * @return the all
	 */
	public HashMap<String, String> getAll() {
		return values;
	}

	/**
	 * Load from database.
	 */
	private void loadFromDatabase() {
		Database.retain();
		DB db = Database.get(domain);
		if (db != null) {
			DBCollection coll = db.getCollection(SETTINGSTABLE);

			BasicDBObject w = new BasicDBObject();
			BasicDBObject q = new BasicDBObject();
			q.put("domain", domain);
			w.put("$query", q);

			DBCursor cur = coll.find(w);
			if (cur != null) {
				while (cur.hasNext()) {
					DBObject myDoc = cur.next();
					values.put((String) myDoc.get("name"),
							(String) myDoc.get("val"));
				}
			}
		}
		Database.release();
		changed = false;
	}

	/**
	 * Restore.
	 * 
	 * @param val
	 *            the val
	 */
	public void restore(Element vals) {
		NodeList nodes = vals.getElementsByTagName("value");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element val = (Element) nodes.item(i);
			String name = val.getAttribute("name");
			String v = val.getAttribute("val");
			set(name, v);
		}
		save();
	}

	/**
	 * Save changed values.
	 */
	public void save() {
		if (changed) {
			DBObject myDoc = null;
			Database.retain();
			DB db = Database.get(domain);
			if (db != null) {
				DBCollection coll = db.getCollection(SETTINGSTABLE);
				Set<String> keys = values.keySet();
				Iterator<String> i = keys.iterator();
				while (i.hasNext()) {
					String name = i.next();
					String val = values.get(name);

					BasicDBObject query = new BasicDBObject();
					query.put("name", name);
					query.put("domain", domain);
					myDoc = coll.findOne(query);
					if (myDoc != null) {
						if (!val.equals(myDoc.get("val"))) {
							myDoc.put("val", val);
							coll.save(myDoc);
						}
					} else {
						myDoc = new BasicDBObject();
						myDoc.put("name", name);
						myDoc.put("domain", domain);
						myDoc.put("val", val);
						coll.insert(myDoc);
					}
				}
				changed = false;
			}
			Database.release();

		}
	}

	/**
	 * Sets the values hash.
	 * 
	 * @param newvalues
	 *            the newvalues
	 */
	public void set(HashMap<String, String> newvalues) {
		Set<String> keys = newvalues.keySet();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			String name = i.next();
			set(name, newvalues.get(name));
		}
	}

	/**
	 * Sets the new value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	private void set(String name, String value) {
		name = name.trim().toLowerCase();
		value = value.trim();
		if (values.containsKey(name)) {
			String oldval = values.get(name);
			if (!oldval.equals(value)) {
				changed = true;
			}
		} else {
			changed = true;
		}
		values.put(name, value);
	}
}
