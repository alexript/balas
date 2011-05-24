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

import net.autosauler.ballance.server.model.AbstractCatalog;

/**
 * The Class Catalog.
 * 
 * @author alexript
 */
public class Catalog {

	/** The impl. */
	private final AbstractCatalog impl;

	/**
	 * Instantiates a new catalog.
	 * 
	 * @param catimpl
	 *            the catimpl
	 */
	public Catalog(final AbstractCatalog catimpl) {
		impl = catimpl;
	}

	/**
	 * Instantiates a new catalog.
	 * 
	 * @param domain
	 *            the domain
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 */
	public Catalog(String domain, String catalogname, Long number) {
		impl = new AbstractCatalog(catalogname, domain, number);
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
