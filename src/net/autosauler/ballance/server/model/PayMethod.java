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

import com.mongodb.BasicDBObject;

/**
 * The Class PayMethod.
 * 
 * @author alexript
 */
public class PayMethod extends AbstractCatalog {
	private static final String catname = "paymethod";

	/**
	 * Instantiates a new pay method.
	 * 
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public PayMethod(String domain, Long number) {
		super(catname, domain, number);
	}

	/**
	 * Instantiates a new pay method.
	 * 
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public PayMethod(String domain, String username) {
		super(catname, domain, username);

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
	 * @see net.autosauler.ballance.server.model.AbstractCatalog#initStructure()
	 */
	@Override
	protected void initStructure() {

	}

}
