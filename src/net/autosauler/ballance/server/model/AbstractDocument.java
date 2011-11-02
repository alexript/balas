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

import javax.script.ScriptException;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.server.struct.StructureFactory;
import net.autosauler.ballance.server.vm.DocumentWrapper;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.Table;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class AbstractDocument.
 * 
 * @author alexript
 */
public class AbstractDocument extends AbstractStructuredData implements
		IScriptableObject {

	/** The Constant fieldname_active. */
	private static final String fieldname_active = "active";

	/** The Constant fieldname_activationdate. */
	private static final String fieldname_activationdate = "activationdate";

	/** The Constant fieldname_parentdoc. */
	private static final String fieldname_parentdoc = "pardoc";

	/** The Constant fieldname_parentdocname. */
	private static final String fieldname_parentdocname = "pardocname";

	/** The tables. */
	private HashMap<String, DocumentTablePart> tables;

	private Description structuredescription;

	/**
	 * Instantiates a new document.
	 * 
	 * @param name
	 *            the name
	 * @param domain
	 *            the domain
	 */
	public AbstractDocument(String name, String domain) {
		super("doc", name, domain);
		initTableParts();
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
	public AbstractDocument(String name, String domain, Long number) {
		super("doc", name, domain);
		initTableParts();
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
	public AbstractDocument(String name, String domain, String username) {
		super("doc", name, domain);
		initTableParts();
		setUsername(username);

	}

	/**
	 * Activation.
	 */
	public void activation() {
		if (!isActive()) {
			if (onActivation()) {

				Scripts script = new Scripts(this, getDomain(), "document."
						+ getSuffix());

				try {
					script.call("OnActivate", new DocumentWrapper(this));
				} catch (ScriptException e) {
					Log.error(e.getMessage());
				} catch (NoSuchMethodException e) {
					Log.error(e.getMessage());
				}

				setActive(true);
				setActivationdate(new Date()); // document activation
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#addFindAllOrders
	 * ( com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllOrders(BasicDBObject o) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindAllQueryParameters (com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllQueryParameters(BasicDBObject q) {
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

	/**
	 * Find all childs.
	 * 
	 * @param docname
	 *            the docname
	 * @return the sets the
	 */
	public Set<Long> findAllChilds(String docname) {
		Set<Long> numbers = new HashSet<Long>();

		DB db = Database.get(getDomain());
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection("doc_" + docname);
			BasicDBObject q = new BasicDBObject();
			BasicDBObject w = new BasicDBObject();
			q.put(fieldname_domain, getDomain());
			q.put(fieldname_trash, false);
			q.put(fieldname_parentdoc, getNumber());
			q.put(fieldname_parentdocname, getSuffix());
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
		sb.append("function OnCreate(document)\n Log.error('method OnCreate not defined')\nend\n\n");
		sb.append("function OnTrash(document)\n Log.error('method OnTrash not defined')\nend\n\n");
		sb.append("function OnRestore(document)\n Log.error('method OnRestore not defined')\nend\n\n");
		sb.append("function OnUpdate(document)\n Log.error('method OnUpdate not defined')\nend\n\n");
		sb.append("function OnActivate(document)\n Log.error('method OnActivate not defined')\nend\n\n");
		sb.append("function OnUnactivate(document)\n Log.error('method OnUnactivate not defined')\nend\n\n");

		// Set<String> names = struct.getNames();
		// Iterator<String> i = names.iterator();
		// while (i.hasNext()) {
		// String n = i.next();
		// String nn = Character.toUpperCase(n.charAt(0)) + n.substring(1);
		// String name = "On" + nn + "Change";
		// sb.append("// must return HashTable\n");
		// sb.append("function " + name + "(hashTable)\n Log.error('method "
		// + name + " not defined')\n return hashTable\nend\n\n");
		// }

		sb.append("// must return HashTable\n");
		sb.append("function OnChange(fieldname, hashTable)\n return hashTable\nend\n\n");

		if (hasTables()) {

			sb.append("// must return HashTable\n");
			sb.append("function OnChangeTable"
					+ "(tablename, fieldname, hashTable)\n return hashTable\nend\n\n");

			// names = tables.keySet();
			// i = names.iterator();
			// while (i.hasNext()) {
			// String name = i.next();
			//
			// String prefix = "On" + Character.toUpperCase(name.charAt(0))
			// + name.substring(1);
			//
			// DocumentTablePart part = tables.get(name);
			// Set<String> fields = part.struct.getNames();
			// Iterator<String> j = fields.iterator();
			// while (j.hasNext()) {
			// String field = j.next();
			// String nn = Character.toUpperCase(field.charAt(0))
			// + field.substring(1);
			// String method = prefix + nn + "Change";
			// sb.append("// must return HashTable\n");
			// sb.append("function " + method
			// + "(hashTable)\n Log.error('method " + method
			// + " not defined')\n return hashTable\nend\n\n");
			// }
			//
			// }
		}

		return sb.toString();
	}

	/**
	 * Gets the.
	 * 
	 * @param numbers
	 *            the numbers
	 * @return the sets the
	 */
	public Set<HashMap<String, Object>> get(Set<Long> numbers) {
		Set<HashMap<String, Object>> set = new HashSet<HashMap<String, Object>>();
		Iterator<Long> i = numbers.iterator();
		while (i.hasNext()) {
			Long number = i.next();
			AbstractDocument doc = new AbstractDocument(getSuffix(),
					getDomain(), number);
			if (doc != null) {
				set.add(doc.toMap());
			}
		}
		return set;
	}

	/**
	 * Gets the activationdate.
	 * 
	 * @return the activationdate
	 */
	public Date getActivationdate() {
		return (Date) values.get(fieldname_activationdate);
	}

	/**
	 * Gets the part.
	 * 
	 * @param name
	 *            the name
	 * @return the part
	 */
	public DocumentTablePart getPart(String name) {
		if (tables.containsKey(name)) {
			return tables.get(name);
		}
		return null;
	}

	/**
	 * Gets the table records.
	 * 
	 * @param name
	 *            the name
	 * @return the table records
	 */
	public Set<HashMap<String, Object>> getTableRecords(String name) {
		Set<HashMap<String, Object>> set = null;
		DocumentTablePart table = getPart(name);
		if (table != null) {
			set = table.getRecords();
		}

		return set;
	}

	/**
	 * Checks for tables.
	 * 
	 * @return true, if successful
	 */
	public boolean hasTables() {
		return (tables != null) && !tables.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * initGlobalStructure()
	 */
	@Override
	protected void initGlobalStructure() {

		struct.add(fieldname_active, DataTypes.DT_BOOLEAN, new Boolean(false));
		struct.add(fieldname_activationdate, DataTypes.DT_DATE, new Date());
		struct.add(fieldname_parentdoc, DataTypes.DT_DOCUMENTRECORD, new Long(
				0L));
		struct.add(fieldname_parentdocname, DataTypes.DT_DOCUMENT, "");
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

		structuredescription = StructureFactory.loadDescription("document."
				+ getSuffix());
		tables = new HashMap<String, DocumentTablePart>();

		List<Field> fields = structuredescription.get();
		Iterator<Field> i = fields.iterator();
		while (i.hasNext()) {
			Field f = i.next();
			struct.add(f.getFieldname(), f.getType(), f.getDefval());
		}

	}

	private void initTableParts() {

		List<Table> doctables = structuredescription.getTables();
		Iterator<Table> j = doctables.iterator();
		while (j.hasNext()) {
			Table t = j.next();

			DocumentTablePart part = new DocumentTablePart(t.getName(),
					getDomain());
			tables.put(t.getName(), part);
		}
	}

	/**
	 * Checks if is active.
	 * 
	 * @return the active
	 */
	public boolean isActive() {
		return (Boolean) values.get(fieldname_active);
	}

	/**
	 * On activation.
	 * 
	 * @return true, if successful
	 */
	protected boolean onActivation() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onCreate()
	 */
	@Override
	protected void onCreate() {
		Scripts script = new Scripts(this, getDomain(), "document."
				+ getSuffix());

		try {
			script.call("OnCreate", new DocumentWrapper(this));
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
		if (!hasTables()) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		sb.append("<tables>\n");
		Set<String> names = tables.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			DocumentTablePart part = tables.get(name);

			sb.append(part.dump());
		}
		sb.append("</tables>\n");

		return sb;
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
		Set<String> names = tables.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			tables.get(name).setDocnum(number);
		}
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

		i.put(fieldname_active, 1);
		coll.createIndex(i);
	}

	/**
	 * On restore.
	 * 
	 * @param dump
	 *            the dump
	 */
	@Override
	protected void onRestore(Element dump) {
		NodeList tablessets = dump.getElementsByTagName("tables");
		if (tablessets.getLength() > 0) {
			for (int i = 0; i < tablessets.getLength(); i++) {
				Element tablesset = (Element) tablessets.item(i);
				NodeList tablenodes = tablesset.getElementsByTagName("doctab");

				if (tablenodes.getLength() > 0) {
					for (int j = 0; j < tablenodes.getLength(); j++) {
						Element tablenode = (Element) tablenodes.item(i);
						String name = tablenode.getAttribute("name");
						DocumentTablePart p = new DocumentTablePart(name,
								getDomain());
						p.restore(tablenode);
					}

				}
			}
		}
	}

	/**
	 * On un activation.
	 * 
	 * @return true, if successful
	 */
	protected boolean onUnActivation() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onUpdate()
	 */
	@Override
	protected void onUpdate() {
		Scripts script = new Scripts(this, getDomain(), "document."
				+ getSuffix());

		try {
			script.call("OnUpdate", new DocumentWrapper(this));
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
		Scripts script = new Scripts(this, getDomain(), "document."
				+ getSuffix());

		try {
			script.call("OnRestore", new DocumentWrapper(this));
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}

	}

	/**
	 * Save table records.
	 * 
	 * @param username
	 *            the username
	 * @param tableparts
	 *            the tableparts
	 * @return true, if successful
	 */
	public boolean saveTableRecords(String username,
			HashMap<String, Set<HashMap<String, Object>>> tableparts) {
		boolean result = true;
		if (tableparts != null) {
			Long num = getNumber();
			if (num.equals(0L)) {
				result = save();
				num = getNumber();
			}
			Set<String> names = tableparts.keySet();
			Iterator<String> i = names.iterator();
			while (i.hasNext()) {
				String name = i.next();
				result = result
						&& saveTableRecords(username, num, name,
								tableparts.get(name));
			}
		}
		return result;
	}

	/**
	 * Save table records.
	 * 
	 * @param username
	 *            the username
	 * @param docnumber
	 *            the docnumber
	 * @param name
	 *            the name
	 * @param set
	 *            the set
	 * @return true, if successful
	 */
	public boolean saveTableRecords(String username, Long docnumber,
			String name, Set<HashMap<String, Object>> set) {

		boolean result = true;
		DocumentTablePart table = getPart(name);
		if (table != null) {
			result = table.updateRecords(username, docnumber, set);
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
	 * Sets the parent.
	 * 
	 * @param d
	 *            the new parent
	 */
	public void setParent(AbstractDocument d) {
		values.set(fieldname_parentdocname, d.getSuffix());
		values.set(fieldname_parentdoc, d.getNumber());
	}

	/**
	 * Sets the parent.
	 * 
	 * @param docname
	 *            the docname
	 * @param num
	 *            the new parent
	 */
	public void setParent(String docname, Long num) {
		values.set(fieldname_parentdocname, docname);
		values.set(fieldname_parentdoc, num);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#trash()
	 */
	@Override
	public void trash() {
		super.trash();
		Scripts script = new Scripts(this, getDomain(), "document."
				+ getSuffix());

		try {
			script.call("OnTrash", new DocumentWrapper(this));
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}

	}

	/**
	 * Unactivation.
	 */
	public void unactivation() {
		if (isActive()) {
			if (onUnActivation()) {
				Scripts script = new Scripts(this, getDomain(), "document."
						+ getSuffix());

				try {
					script.call("OnUnactivate", new DocumentWrapper(this));
				} catch (ScriptException e) {
					Log.error(e.getMessage());
				} catch (NoSuchMethodException e) {
					Log.error(e.getMessage());
				}

				setActive(false);
			}
		}
	}

}
