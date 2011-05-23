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

/**
 * The Class Catalogs.
 * 
 * @author alexript
 */
public class Catalogs {

	/** The domain. */
	private final String domain;

	/**
	 * Instantiates a new catalogs.
	 * 
	 * @param domain
	 *            the domain
	 */
	public Catalogs(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param number
	 *            the number
	 * @return the catalog
	 */
	public Catalog get(String catalogname, Long number) {
		return new Catalog(domain, catalogname, number);
	}
}
