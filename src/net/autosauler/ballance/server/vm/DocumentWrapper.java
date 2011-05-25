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
package net.autosauler.ballance.server.vm;

import net.autosauler.ballance.server.model.AbstractDocument;

/**
 * The Class DocumentWrapper.
 * 
 * @author alexript
 */
public class DocumentWrapper {

	/** The impl. */
	private final AbstractDocument impl;

	/**
	 * Instantiates a new document wrapper.
	 * 
	 * @param docimpl
	 *            the docimpl
	 */
	public DocumentWrapper(final AbstractDocument docimpl) {
		impl = docimpl;
	}

	/**
	 * Instantiates a new document wrapper.
	 * 
	 * @param domain
	 *            the domain
	 * @param docname
	 *            the docname
	 * @param number
	 *            the number
	 */
	public DocumentWrapper(String domain, String docname, Long number) {
		impl = new AbstractDocument(docname, domain, number);
	}

	/**
	 * Gets the.
	 * 
	 * @param fieldname
	 *            the fieldname
	 * @return the object
	 */
	public Object get(String fieldname) {
		return impl.getFieldValue(fieldname);
	}

	/**
	 * Gets the table.
	 * 
	 * @param tablename
	 *            the tablename
	 * @return the table
	 */
	public DoctableWrapper getTable(String tablename) {
		return new DoctableWrapper(impl.getPart(tablename));
	}

	/**
	 * Sets the.
	 * 
	 * @param fieldname
	 *            the fieldname
	 * @param val
	 *            the val
	 */
	public void set(String fieldname, Object val) {
		impl.setFieldValue(fieldname, val);
	}

}
