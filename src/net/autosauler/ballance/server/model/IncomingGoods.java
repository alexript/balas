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
import java.util.Iterator;
import java.util.Set;

import com.mongodb.BasicDBObject;

/**
 * The Class IncomingGoods.
 * 
 * @author alexript
 */
public class IncomingGoods extends AbstractDocument {

	/** The Constant docname. */
	private final static String docname = "ingoods";

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
