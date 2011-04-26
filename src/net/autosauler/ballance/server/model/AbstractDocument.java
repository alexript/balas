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
import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * The Class AbstractDocument.
 * 
 * @author alexript
 */
public abstract class AbstractDocument extends AbstractStructuredData implements
		IScriptableObject {
	// TODO: parent document link

	/** The Constant fieldname_active. */
	private static final String fieldname_active = "active";

	/** The Constant fieldname_activationdate. */
	private static final String fieldname_activationdate = "activationdate";

	/** The tables. */
	private final HashMap<String, AbstractDocumentTablePart> tables;

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
		tables = new HashMap<String, AbstractDocumentTablePart>();
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
		tables = new HashMap<String, AbstractDocumentTablePart>();
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
		tables = new HashMap<String, AbstractDocumentTablePart>();
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
				script.eval("(doc." + getSuffix() + ".onactivate)"); // TODO: do
																		// it
																		// right

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

	/**
	 * Adds the table part.
	 * 
	 * @param name
	 *            the name
	 * @param part
	 *            the part
	 */
	public void addTablePart(String name, AbstractDocumentTablePart part) {
		tables.put(name, part);
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
		sb.append("(define (doc." + getSuffix()
				+ ".onactivate) (log.error \"not defined\"))\n");
		sb.append("(define (doc." + getSuffix()
				+ ".onunactivate) (log.error \"not defined\"))\n");
		sb.append("(define (doc." + getSuffix()
				+ ".oncreate) (log.error \"not defined\"))\n");
		sb.append("(define (doc." + getSuffix()
				+ ".ontrash) (log.error \"not defined\"))\n");
		sb.append("(define (doc." + getSuffix()
				+ ".onrestore) (log.error \"not defined\"))\n");

		Set<String> names = struct.getNames();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = "doc." + getSuffix() + ".onchange." + i.next();
			sb.append("(define (" + name + ") (log.error \"not defined\"))\n");
		}

		names = tables.keySet();
		i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			String prefix = "doc." + getSuffix() + ".onchange." + name + ".";
			AbstractDocumentTablePart part = tables.get(name);
			Set<String> fields = part.struct.getNames();
			Iterator<String> j = fields.iterator();
			while (j.hasNext()) {
				String field = j.next();
				sb.append("(define (" + prefix + field
						+ ") (log.error \"not defined\"))\n");
			}

		}

		sb.append(onGenerateDefaultScript());

		return sb.toString();
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
	 * Gets the part.
	 * 
	 * @param name
	 *            the name
	 * @return the part
	 */
	public AbstractDocumentTablePart getPart(String name) {
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
		AbstractDocumentTablePart table = getPart(name);
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
		return !tables.isEmpty();
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
	}

	/**
	 * Inits the table parts.
	 */
	protected abstract void initTableParts();

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
	protected abstract boolean onActivation();

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
			AbstractDocumentTablePart part = tables.get(name);

			sb.append(part.dump());
		}
		sb.append("</tables>\n");

		return sb;
	}

	/**
	 * On generate default script.
	 * 
	 * @return the string
	 */
	protected abstract String onGenerateDefaultScript();

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
	 * On un activation.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean onUnActivation();

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
		AbstractDocumentTablePart table = getPart(name);
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
	 * Unactivation.
	 */
	public void unactivation() {
		if (isActive()) {
			if (onUnActivation()) {
				Scripts script = new Scripts(this, getDomain(), "document."
						+ getSuffix());
				script.eval("(doc." + getSuffix() + ".onunactivate)"); // TODO:
																		// do it
																		// right

				setActive(false);
			}
		}
	}

}
