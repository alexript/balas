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

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.datatypes.DataTypes;

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
public abstract class Catalog extends StructuredData {

	/** The Constant fieldname_trash. */
	private static final String fieldname_trash = "trash";

	/** The Constant fieldname_fullname. */
	private static final String fieldname_fullname = "fullname";

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
		super("cat", name, domain);

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
		super("cat", name, domain);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.StructuredData#addFindAllOrders(
	 * com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllOrders(final BasicDBObject o) {
		o.put(fieldname_fullname, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.StructuredData#addFindAllQueryParameters
	 * (com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllQueryParameters(final BasicDBObject q) {
		q.put(fieldname_trash, false);
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
	 * Gets the select data (pairs name-number).
	 * 
	 * @return the select data
	 */
	public HashMap<String, Long> getSelectData() {
		HashMap<String, Long> map = new HashMap<String, Long>();
		DB db = Database.get();
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(getTableName());
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
			DBCollection coll = db.getCollection(getTableName());
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
	@Override
	protected void initGlobalStructure() {

		struct.add(fieldname_trash, DataTypes.DT_BOOLEAN, false);

		struct.add(fieldname_fullname, DataTypes.DT_STRING, "uncknown");

	}

	/**
	 * Inits the structure.
	 */
	@Override
	protected abstract void initStructure();

	/**
	 * Checks if is trash.
	 * 
	 * @return true, if is trash
	 */
	public boolean isTrash() {
		return (Boolean) values.get(fieldname_trash);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.StructuredData#onInitDbStruct(com
	 * .mongodb.BasicDBObject, com.mongodb.DBCollection)
	 */
	@Override
	protected void onInitDbStruct(final BasicDBObject i, final DBCollection coll) {
		i.put(fieldname_fullname, 1);
		i.put(fieldname_trash, 1);
		coll.createIndex(i);

	}

	/**
	 * Restore.
	 */
	public void restore() {
		values.set(fieldname_trash, false);

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
	 * Trash.
	 */
	public void trash() {
		values.set(fieldname_trash, true);
	}

}
