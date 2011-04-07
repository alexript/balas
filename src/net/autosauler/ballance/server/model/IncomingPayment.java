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

import com.mongodb.DBObject;

/**
 * The Class IncomingPayment.
 * 
 * @author alexript
 */
public class IncomingPayment extends Document {

	/** The Constant docname. */
	private final static String docname = "inpay";

	/** The partner. */
	private Long partner;

	/** The paydate. */
	private Date paydate;

	/** The currency. */
	private String currency;

	/** The currvalue. */
	private Double currvalue;

	/** The payvalue. */
	private Double payvalue;

	/** The paymethod. */
	private Long paymethod;

	/** The comments. */
	private String comments;

	/**
	 * Instantiates a new incoming payment.
	 * 
	 * @param domain
	 *            the domain
	 */
	public IncomingPayment(String domain) {
		super(docname, domain);
	}

	/**
	 * Instantiates a new incoming payment.
	 * 
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public IncomingPayment(String domain, Long number) {
		super(docname, domain, number);
	}

	/**
	 * Instantiates a new incoming payment.
	 * 
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public IncomingPayment(String domain, String username) {
		super(docname, domain, username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.Document#addFieldsToMap(java.util
	 * .HashMap)
	 */
	@Override
	protected HashMap<String, Object> addFieldsToMap(HashMap<String, Object> map) {
		map.put("partner", partner);
		map.put("paydate", paydate.getTime());
		map.put("currency", currency);
		map.put("currvalue", currvalue.toString());
		map.put("payvalue", payvalue.toString());
		map.put("paymethod", paymethod);
		map.put("comments", comments);

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.Document#fillFieldsFromMap(java.
	 * util.HashMap)
	 */
	@Override
	protected void fillFieldsFromMap(HashMap<String, Object> map) {
		partner = (Long) map.get("partner");
		paydate = new Date((Long) map.get("paydate"));
		currency = (String) map.get("currency");
		currvalue = Currency.get(currency, paydate);
		payvalue = Double.parseDouble((String) map.get("payvalue"));
		paymethod = (Long) map.get("paymethod");
		comments = (String) map.get("comments");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.Document#get(java.util.Set)
	 */
	@Override
	public Set<HashMap<String, Object>> get(Set<Long> numbers) {
		{
			Set<HashMap<String, Object>> set = new HashSet<HashMap<String, Object>>();
			Iterator<Long> i = numbers.iterator();
			while (i.hasNext()) {
				Long number = i.next();
				IncomingPayment doc = new IncomingPayment(getDomain(), number);
				if (doc != null) {
					set.add(doc.toMap());
				}
			}
			return set;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.Document#getFields(com.mongodb.DBObject
	 * )
	 */
	@Override
	protected DBObject getFields(DBObject doc) {
		doc.put("partner", partner);
		doc.put("paydate", paydate);
		doc.put("currency", currency);
		doc.put("currvalue", currvalue);
		doc.put("payvalue", payvalue);
		doc.put("paymethod", paymethod);
		doc.put("comments", comments);

		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.Document#onActivation()
	 */
	@Override
	protected boolean onActivation() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.Document#onGenerateDefaultScript()
	 */
	@Override
	protected String onGenerateDefaultScript() {
		// TODO: add document-specific default script
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.Document#onUnActivation()
	 */
	@Override
	protected boolean onUnActivation() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.Document#setFields(com.mongodb.DBObject
	 * )
	 */
	@Override
	protected void setFields(DBObject doc) {
		partner = (Long) doc.get("partner");
		paydate = (Date) doc.get("paydate");
		currency = (String) doc.get("currency");
		currvalue = (Double) doc.get("currvalue");
		payvalue = (Double) doc.get("payvalue");
		paymethod = (Long) doc.get("paymethod");
		comments = (String) doc.get("comments");
	}

}
