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

/**
 * The Class IncomingPayment.
 * 
 * @author alexript
 */
public class IncomingPayment extends Document {

	/** The Constant docname. */
	private final static String docname = "inpay";

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
	 * @see net.autosauler.ballance.server.model.Document#initStructure()
	 */
	@Override
	protected void initStructure() {
		struct.add("partner", DataTypes.DT_LONG, new Long(0L));
		struct.add("paydate", DataTypes.DT_DATE, new Date());
		struct.add("currency", DataTypes.DT_CURRENCY, "RUR");
		struct.add("currvalue", DataTypes.DT_MONEY, new Double(1.0D));
		struct.add("payvalue", DataTypes.DT_MONEY, new Double(0.0D));
		struct.add("paymethod", DataTypes.DT_CATALOGRECORD, new Long(0L));
		struct.add("comments", DataTypes.DT_STRING, "");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.Document#onActivation()
	 */
	@Override
	protected boolean onActivation() {
		values.set(
				"currvalue",
				Currency.get((String) values.get("currency"),
						(Date) values.get("paydate")));
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

}
