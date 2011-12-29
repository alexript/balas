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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.w3c.dom.Element;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class DocumentTablePart.
 * 
 * @author alexript
 */
public class DocumentTablePart extends AbstractStructuredData {

	/** The Constant fieldname_document. */
	private static final String fieldname_document = "docnum";

	/**
	 * Instantiates a new document table part.
	 * 
	 * @param partname
	 *            the partname
	 * @param domain
	 *            the domain
	 */
	public DocumentTablePart(String partname, String domain) {
		super("doctab", partname, domain);
		setDocnum(new Long(0L));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#addFindAllOrders
	 * (com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllOrders(BasicDBObject o) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindAllQueryParameters(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllQueryParameters(BasicDBObject q) {
		q.put(fieldname_document, getDocnum());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindLastNumberParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindLastNumberParams(final BasicDBObject w) {
		w.put(fieldname_document, getDocnum());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addGetRecordParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addGetRecordParams(final BasicDBObject query) {
		query.put(fieldname_document, getDocnum());
	}

	public void addRecord(Hashtable<String, Object> values) {

		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			if (!name.equals(fieldname_createdate)
					&& !name.equals(fieldname_domain)
					&& !name.equals(fieldname_number)
					&& !name.equals(fieldname_username)) {
				if (values.containsKey(name)) {
					this.values.setObject(name, values.get(name));
				}
			}
		}

		setDocnum(getDocnum());
		setUsername("");
		save();
	}

	/**
	 * Gets the docnum.
	 * 
	 * @return the docnum
	 */
	public Long getDocnum() {
		return (Long) values.get(fieldname_document);
	}

	/**
	 * Gets the records.
	 * 
	 * @return the records
	 */
	public Set<HashMap<String, Object>> getRecords() {
		Set<HashMap<String, Object>> set = new HashSet<HashMap<String, Object>>();
		DB db = Database.get(getDomain());
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(getTableName());
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put(fieldname_domain, getDomain());
			q.put(fieldname_document, getDocnum());
			q.put(fieldname_trash, false);
			w.put("$query", q);

			BasicDBObject o = new BasicDBObject();
			o.put(fieldname_number, 1);
			addFindAllOrders(o);
			w.put("$orderby", o);

			DBCursor cur = coll.find(w);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				load(myDoc);
				HashMap<String, Object> map = toMap();
				set.add(map);
			}
			Database.release();
		}
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * initGlobalStructure()
	 */
	@Override
	protected void initGlobalStructure() {
		struct.add(fieldname_document, DataTypes.DT_LONG, new Long(0L));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#initStructure
	 * ()
	 */
	@Override
	protected void initStructure() {
		Structures s = new Structures(getDomain());
		Description d = s.getDescription("table." + getSuffix());

		List<Field> fields = d.get();
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field f = i.next();
			struct.add(f.getFieldname(), f.getType(), f.getDefval());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onCreate()
	 */
	@Override
	public void onCreate() {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#onDump()
	 */
	@Override
	protected StringBuilder onDump() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onGetRecord
	 * (java.lang.Long)
	 */
	@Override
	protected void onGetRecord(Long number) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onInitDbStruct
	 * (com.mongodb.BasicDBObject, com.mongodb.DBCollection)
	 */
	@Override
	protected void onInitDbStruct(BasicDBObject i, DBCollection coll) {
		i.put(fieldname_document, 1);

		coll.createIndex(i);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onRestore
	 * (org.w3c.dom.Element)
	 */
	@Override
	protected void onRestore(Element dump) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onUpdate()
	 */
	@Override
	protected void onUpdate() {
		return;
	}

	/**
	 * Sets the docnum.
	 * 
	 * @param docnum
	 *            the docnum to set
	 */
	public void setDocnum(Long docnum) {
		values.setObject(fieldname_document, docnum);

	}

	/**
	 * Update records.
	 * 
	 * @param username
	 *            the username
	 * @param docnumber
	 *            the docnumber
	 * @param set
	 *            the set
	 * @return true, if successful
	 */
	public boolean updateRecords(String username, Long docnumber,
			Set<HashMap<String, Object>> set) {
		boolean result = true;
		Iterator<HashMap<String, Object>> i = set.iterator();
		while (i.hasNext()) {
			HashMap<String, Object> map = i.next();
			fromMap(map);
			setNumber((Long) map.get(fieldname_number));
			if (getNumber() < 1L) {

				setDocnum(docnumber);
				setUsername(username);
			}
			result = result && save();
		}
		return result;
	}

}
