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
import java.util.Set;

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.mongodb.BasicDBObject;

/**
 * The Class IncomingGoods.
 * 
 * @author alexript
 */
public class IncomingGoods extends AbstractDocument {

	/** The Constant docname. */
	private final static String docname = "ingoods";

	/** The Constant fieldname_partner. */
	private final static String fieldname_partner = "partner";

	/** The Constant fieldname_senddate. */
	private final static String fieldname_senddate = "senddate";

	/** The Constant fieldname_goodsnumber. */
	private final static String fieldname_goodsnumber = "gnum";

	/** The Constant fieldname_invoicenum. */
	private final static String fieldname_invoicenum = "invoice";

	/** The Constant fieldname_awb. */
	private final static String fieldname_awb = "awb";

	/** The Constant fieldname_gtd. */
	private final static String fieldname_gtd = "gtd";

	/**
	 * Instantiates a new incoming goods.
	 * 
	 * @param domain
	 *            the domain
	 */
	public IncomingGoods(String domain) {
		super(docname, domain);
	}

	/**
	 * Instantiates a new incoming goods.
	 * 
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public IncomingGoods(String domain, Long number) {
		super(docname, domain, number);
	}

	/**
	 * Instantiates a new incoming goods.
	 * 
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public IncomingGoods(String domain, String username) {
		super(docname, domain, username);
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
	 * net.autosauler.ballance.server.model.AbstractDocument#get(java.util.Set)
	 */
	@Override
	public Set<HashMap<String, Object>> get(Set<Long> numbers) {
		Set<HashMap<String, Object>> set = new HashSet<HashMap<String, Object>>();
		Iterator<Long> i = numbers.iterator();
		while (i.hasNext()) {
			Long number = i.next();
			IncomingGoods doc = new IncomingGoods(getDomain(), number);
			if (doc != null) {
				set.add(doc.toMap());
			}
		}
		return set;
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
		struct.add(fieldname_partner, DataTypes.DT_LONG, new Long(0L));
		struct.add(fieldname_senddate, DataTypes.DT_DATE, new Date());
		struct.add(fieldname_invoicenum, DataTypes.DT_STRING, "");
		struct.add(fieldname_goodsnumber, DataTypes.DT_INT, new Integer(0));
		struct.add(fieldname_invoicenum, DataTypes.DT_STRING, "");
		struct.add(fieldname_awb, DataTypes.DT_STRING, "");
		struct.add(fieldname_gtd, DataTypes.DT_STRING, "");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractDocument#initTableParts()
	 */
	@Override
	protected void initTableParts() {
		// TODO Auto-generated method stub

		// content:
		// partner
		// weight
		// summ
		// currency
		// boxesnum

		// additional pays:
		// partner
		// date
		// summ
		// currency
		// decription
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractDocument#onActivation()
	 */
	@Override
	protected boolean onActivation() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractDocument#onGenerateDefaultScript
	 * ()
	 */
	@Override
	protected String onGenerateDefaultScript() {

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractDocument#onUnActivation()
	 */
	@Override
	protected boolean onUnActivation() {

		return true;
	}

}
