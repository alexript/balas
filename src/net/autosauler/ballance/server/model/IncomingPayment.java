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

import com.mongodb.DBObject;

/**
 * The Class IncomingPayment.
 * 
 * @author alexript
 */
public class IncomingPayment extends Document {

	/** The Constant docname. */
	private final static String docname = "inpay";

	private Long partner;
	private Date paydate;
	private String currency;
	private Double currvalue;
	private Double payvalue;

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
		map.put("currvalue", currvalue);
		map.put("payvalue", payvalue);

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
		payvalue = (Double) map.get("payvalue");
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

		return doc;
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
	}

}
