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

import com.mongodb.BasicDBObject;

/**
 * The Class IncomingPayment.
 * 
 * @author alexript
 */
public class IncomingPayment extends AbstractDocument {

	/** The Constant docname. */
	private final static String docname = "inpay";

	/** The Constant fieldname_paydate. */
	private final static String fieldname_paydate = "paydate";

	/** The Constant fieldname_currency. */
	private final static String fieldname_currency = "currency";

	/** The Constant fieldname_currvalue. */
	private final static String fieldname_currvalue = "currvalue";

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
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindLastNumberParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindLastNumberParams(final BasicDBObject w) {
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
	 * Gets the.
	 * 
	 * @param numbers
	 *            the numbers
	 * @return the sets the
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
	 * @see net.autosauler.ballance.server.model.AbstractDocument#onActivation()
	 */
	@Override
	protected boolean onActivation() {
		values.set(fieldname_currvalue, Currency.get(
				(String) values.get(fieldname_currency),
				(Date) values.get(fieldname_paydate)));
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
		// TODO: add document-specific default script
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
