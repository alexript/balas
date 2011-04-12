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

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.mongodb.DBObject;

/**
 * The Class Partner.
 * 
 * @author alexript
 */
public class Partner extends Catalog {

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
	 * net.autosauler.ballance.server.model.ICatalogRecord#addFieldsToMap(java
	 * .util.HashMap)
	 */
	@Override
	public HashMap<String, Object> addFieldsToMap(HashMap<String, Object> map) {
		map.put("email", getEmail());
		map.put("paymethod", getPaymethod());
		map.put("currency", getCurrency());
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
		setEmail((String) map.get("email"));
		setPaymethod((Long) map.get("paymethod"));
		setCurrency((String) map.get("currency"));
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return (String) values.get("currency");
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getEmail() {
		return (String) values.get("email");
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
		doc.put("email", getEmail());
		doc.put("paymethod", getPaymethod());
		doc.put("currency", getCurrency());
		return doc;
	}

	/**
	 * @return the paymethod
	 */
	public Long getPaymethod() {
		return (Long) values.get("paymethod");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.Catalog#initStructure()
	 */
	@Override
	protected void initStructure() {
		struct.add("email", DataTypes.DT_STRING, "");
		struct.add("paymethod", DataTypes.DT_CATALOGRECORD, new Long(0L));
		struct.add("currency", DataTypes.DT_CURRENCY, "RUR");
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		values.set("currency", currency);
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setEmail(String email) {
		values.set("email", email);
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
		setEmail((String) doc.get("email"));
		setPaymethod((Long) doc.get("paymethod"));
		setCurrency((String) doc.get("currency"));
	}

	/**
	 * @param paymethod
	 *            the paymethod to set
	 */
	public void setPaymethod(Long paymethod) {
		values.set("paymethod", paymethod);
	}

}
