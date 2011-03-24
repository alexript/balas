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

import com.mongodb.DBObject;

/**
 * The Interface ICatalogRecord.
 * 
 * @author alexript
 */
public interface ICatalogRecord {

	/**
	 * Find all records numbers.
	 * 
	 * @return the sets the
	 */
	public Set<Long> findAll();

	/**
	 * Gets the record's author.
	 * 
	 * @return the author
	 */
	public String getAuthor();

	/**
	 * Gets the record's creates the date.
	 * 
	 * @return the creates the date
	 */
	public Date getCreateDate();

	public String getDomain();

	/**
	 * Gets the fields.
	 * 
	 * @param doc
	 *            the doc
	 * @return the fields
	 */
	abstract DBObject getFields(DBObject doc);

	/**
	 * Gets the number of record.
	 * 
	 * @return the number
	 */
	public Long getNumber();

	/**
	 * Checks if record is in trash.
	 * 
	 * @return true, if is trash
	 */
	public boolean isTrash();

	/**
	 * Load.
	 * 
	 * @param doc
	 *            the doc
	 */
	public void load(DBObject doc);

	/**
	 * Restore record from trash.
	 */
	public void restore();

	/**
	 * Save record.
	 * 
	 * @return true, if successful
	 */
	public boolean save();

	/**
	 * Sets the record's author.
	 * 
	 * @param userlogin
	 *            the new author
	 */
	public void setAuthor(String userlogin);

	/**
	 * Sets the record's create date.
	 * 
	 * @param createdate
	 *            the new creates the date
	 */
	public void setCreateDate(Date createdate);

	public void setDomain(String domain);

	/**
	 * Sets the fields.
	 * 
	 * @param doc
	 *            the new fields
	 */
	abstract void setFields(DBObject doc);

	/**
	 * Sets the record number.
	 * 
	 * @param number
	 *            the new number
	 */
	public void setNumber(Long number);

	/**
	 * Store.
	 * 
	 * @param doc
	 *            the doc
	 * @return the dB object
	 */
	public DBObject store(DBObject doc);

	/**
	 * Trash record.
	 */
	public void trash();

}
