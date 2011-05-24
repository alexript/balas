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
import java.util.List;
import java.util.Set;

import javax.script.ScriptException;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.server.struct.StructureFactory;
import net.autosauler.ballance.server.vm.Catalog;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.w3c.dom.Element;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class AbstractCatalog.
 * 
 * @author alexript
 */
public class AbstractCatalog extends AbstractStructuredData implements
		IScriptableObject {
	// TODO: add periodic catalogs
	// TODO: add groups

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
	public AbstractCatalog(String name, String domain, Long number) {
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
	public AbstractCatalog(String name, String domain, String username) {
		super("cat", name, domain);
		setUsername(username);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#addFindAllOrders
	 * ( com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllOrders(final BasicDBObject o) {
		o.put(fieldname_fullname, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindAllQueryParameters (com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllQueryParameters(final BasicDBObject q) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindLastNumberParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindLastNumberParams(BasicDBObject w) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addGetRecordParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addGetRecordParams(BasicDBObject query) {
		return;

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
		sb.append("import java.lang\nimport com.allen_sauer.gwt.log.client.Log\n\n");
		sb.append("function OnCreate(record)\n Log.error('method OnCreate not defined')\nend\n\n");
		sb.append("function OnTrash(record)\n Log.error('method OnTrash not defined')\nend\n\n");
		sb.append("function OnRestore(record)\n Log.error('method OnRestore not defined')\nend\n\n");
		sb.append("function OnUpdate(record)\n Log.error('method OnUpdate not defined')\nend\n\n");

		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String n = i.next();
			String nn = Character.toUpperCase(n.charAt(0)) + n.substring(1);
			String name = "On" + nn + "Change";
			sb.append("// must return HashTable\n");
			sb.append("function " + name + "(hashTable)\n Log.error('method "
					+ name + " not defined')\nreturn hashTable\nend\n\n");

		}

		return sb.toString();

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
		DB db = Database.get(getDomain());
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
		DB db = Database.get(getDomain());
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

		struct.add(fieldname_fullname, DataTypes.DT_STRING, "uncknown");

	}

	/**
	 * Inits the structure.
	 */
	@Override
	protected void initStructure() {
		Description d = StructureFactory.loadDescription("catalog."
				+ getSuffix());
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
	protected void onCreate() {
		Scripts script = new Scripts(this, getDomain(), "catalog."
				+ getSuffix());
		try {
			script.call("OnCreate", new Catalog(this));
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}

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
	 * (com .mongodb.BasicDBObject, com.mongodb.DBCollection)
	 */
	@Override
	protected void onInitDbStruct(final BasicDBObject i, final DBCollection coll) {
		i.put(fieldname_fullname, 1);

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
		Scripts script = new Scripts(this, getDomain(), "catalog."
				+ getSuffix());

		try {
			script.call("OnUpdate", new Catalog(this));
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#restore()
	 */
	@Override
	public void restore() {
		super.restore();
		Scripts script = new Scripts(this, getDomain(), "catalog."
				+ getSuffix());

		try {
			script.call("OnRestore", new Catalog(this));
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#trash()
	 */
	@Override
	public void trash() {
		super.trash();
		Scripts script = new Scripts(this, getDomain(), "catalog."
				+ getSuffix());

		try {
			script.call("OnTrash", new Catalog(this));
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}

	}

}
