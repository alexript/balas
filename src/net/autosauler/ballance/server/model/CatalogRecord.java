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
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author alexript
 * 
 */
public abstract class CatalogRecord implements ICatalogRecord {

	private Catalog catalog = null;

	private Long number;
	private String authorname;
	private Date createdate;
	private boolean trash;
	private String domain;

	public CatalogRecord(String name, String domain, Long number) {
		catalog = new Catalog(name, domain, null);
		get(number);
	}

	public CatalogRecord(String name, String domain, String username) {
		catalog = new Catalog(name, domain, username);
		setDefaultValues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#findAll()
	 */
	@Override
	public Set<Long> findAll() {
		return catalog.allRecords();
	}

	/**
	 * Gets the.
	 * 
	 * @param number
	 *            the number
	 */
	private void get(Long number) {
		DBObject doc = catalog.getRecord(number);
		if (doc != null) {
			load(doc);
		} else {
			setDefaultValues();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#getAuthor()
	 */
	@Override
	public String getAuthor() {

		return authorname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#getCreateDate()
	 */
	@Override
	public Date getCreateDate() {

		return createdate;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#getNumber()
	 */
	@Override
	public Long getNumber() {

		return number;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#isTrash()
	 */
	@Override
	public boolean isTrash() {
		return trash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#load(com.mongodb.
	 * DBObject)
	 */
	@Override
	public void load(DBObject doc) {
		setNumber((Long) doc.get("number"));
		setAuthor((String) doc.get("author"));
		setCreateDate((Date) doc.get("createdate"));
		setDomain((String) doc.get("domain"));
		boolean f = (Boolean) doc.get("trash");
		if (f) {
			trash();
		} else {
			restore();
		}
		setFields(doc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#restore()
	 */
	@Override
	public void restore() {
		trash = false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#save()
	 */
	@Override
	public boolean save() {

		return catalog.save(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#setAuthor(java.lang
	 * .String)
	 */
	@Override
	public void setAuthor(String userlogin) {
		authorname = userlogin;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#setCreateDate()
	 */
	@Override
	public void setCreateDate(Date createdate) {
		this.createdate = createdate;
	}

	private void setDefaultValues() {
		setNumber(0L);
		setAuthor("none");
		setCreateDate(new Date());
		restore();
	}

	@Override
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#setNumber(java.lang
	 * .Long)
	 */
	@Override
	public void setNumber(Long number) {
		this.number = number;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.ICatalogRecord#store(com.mongodb
	 * .DBObject)
	 */
	@Override
	public DBObject store(DBObject doc) {
		if (doc == null) {
			doc = new BasicDBObject();
		}
		doc.put("number", getNumber());
		doc.put("author", getAuthor());
		doc.put("createdate", getCreateDate());
		doc.put("trash", isTrash());
		doc.put("domain", getDomain());
		doc = getFields(doc);
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.ICatalogRecord#trash()
	 */
	@Override
	public void trash() {
		trash = true;

	}
}
