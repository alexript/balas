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

import java.util.Date;
import java.util.List;

import net.autosauler.ballance.server.model.Currency;
import net.autosauler.ballance.shared.CurrValue;

/**
 * The Class CurrencyWrapper.
 * 
 * @author alexript
 */
public class CurrencyWrapper {

	public CurrencyWrapper() {
		// Log.error("Wrapper inited");
	}

	/**
	 * Gets the.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @return the double
	 */
	public Double get(String mnemo) {
		return Currency.get(mnemo);
	}

	/**
	 * Gets the.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param date
	 *            the date
	 * @return the double
	 */
	public Double get(String mnemo, Date date) {
		return Currency.get(mnemo, date);
	}

	/**
	 * Gets the.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the list
	 */
	public List<CurrValue> get(String mnemo, Date start, Date end) {
		return Currency.get(mnemo, start, end);
	}
}
