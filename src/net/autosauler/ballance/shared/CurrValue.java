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

package net.autosauler.ballance.shared;

import java.util.Date;

/**
 * The Class CurrValue.
 * 
 * @author alexript
 */
public class CurrValue {

	/** The d. */
	private final Date d;

	/** The val. */
	private final Double val;

	/**
	 * Instantiates a new curr value.
	 * 
	 * @param date
	 *            the date
	 * @param value
	 *            the value
	 */
	public CurrValue(Date date, Double value) {
		d = date;
		val = value;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return d;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Double getValue() {
		return val;
	}
}
