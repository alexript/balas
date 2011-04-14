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

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * The Class AbstractDocumentTablePart.
 * 
 * @author alexript
 */
public abstract class AbstractDocumentTablePart extends AbstractStructuredData {

	private static final String fieldname_document = "docnum";
	private Long docnum;

	/**
	 * Instantiates a new document table part.
	 * 
	 * @param partname
	 *            the partname
	 * @param domain
	 *            the domain
	 */
	public AbstractDocumentTablePart(String partname, String domain) {
		super("doctab", partname, domain);
		setDocnum(new Long(0L));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#addFindAllOrders
	 * (com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllOrders(BasicDBObject o) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindAllQueryParameters(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindAllQueryParameters(BasicDBObject q) {
		q.put(fieldname_document, getDocnum());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindLastNumberParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindLastNumberParams(final BasicDBObject w) {
		w.put(fieldname_document, getDocnum());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addGetRecordParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addGetRecordParams(final BasicDBObject query) {
		query.put(fieldname_document, getDocnum());
	}

	/**
	 * @return the docnum
	 */
	public Long getDocnum() {
		return docnum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * initGlobalStructure()
	 */
	@Override
	protected void initGlobalStructure() {
		struct.add(fieldname_document, DataTypes.DT_LONG, new Long(0L));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#initStructure
	 * ()
	 */
	@Override
	protected abstract void initStructure();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onInitDbStruct
	 * (com.mongodb.BasicDBObject, com.mongodb.DBCollection)
	 */
	@Override
	protected void onInitDbStruct(BasicDBObject i, DBCollection coll) {
		i.put(fieldname_document, 1);

		coll.createIndex(i);

	}

	/**
	 * @param docnum
	 *            the docnum to set
	 */
	public void setDocnum(Long docnum) {
		this.docnum = docnum;
	}

}
