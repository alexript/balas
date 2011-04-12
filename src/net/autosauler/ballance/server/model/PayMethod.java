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

import com.mongodb.DBObject;

/**
 * The Class PayMethod.
 * 
 * @author alexript
 */
public class PayMethod extends Catalog {

	/**
	 * Instantiates a new pay method.
	 * 
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public PayMethod(String domain, Long number) {
		super("paymethod", domain, number);
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
		super("paymethod", domain, username);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#addFieldsToMap(java
	 * .util.HashMap)
	 */
	@Override
	public HashMap<String, Object> addFieldsToMap(HashMap<String, Object> map) {
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#fillFieldsFromMap
	 * (java.util.HashMap)
	 */
	@Override
	public void fillFieldsFromMap(HashMap<String, Object> map) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#getFields(com.mongodb
	 * .DBObject)
	 */
	@Override
	public DBObject getFields(DBObject doc) {

		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.Catalog#initStructure()
	 */
	@Override
	protected void initStructure() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#setFields(com.mongodb
	 * .DBObject)
	 */
	@Override
	public void setFields(DBObject doc) {
		return;

	}

}
