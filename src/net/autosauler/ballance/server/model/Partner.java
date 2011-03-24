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

import com.mongodb.DBObject;

/**
 * The Class Partner.
 * 
 * @author alexript
 */
public class Partner extends CatalogRecord {

	/** The partner name. */
	private String name;

	/**
	 * Instantiates a new partner.
	 * 
	 * @param domain
	 *            the domain
	 * @param number
	 *            the number
	 */
	public Partner(String domain, Long number) {
		super("partners", domain, number);
	}

	/**
	 * Instantiates a new partner.
	 * 
	 * @param domain
	 *            the domain
	 * @param username
	 *            the username
	 */
	public Partner(String domain, String username) {
		super("partners", domain, username);

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
		doc.put("name", getName());
		return doc;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
		setName((String) doc.get("name"));

	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
