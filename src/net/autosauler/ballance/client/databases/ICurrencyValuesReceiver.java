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

package net.autosauler.ballance.client.databases;

import java.util.Date;
import java.util.HashMap;

/**
 * The Interface ICurrencyValuesReceiver.
 * 
 * @author alexript
 */
public interface ICurrencyValuesReceiver {

	/**
	 * Do received currency value.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param date
	 *            the date
	 * @param value
	 *            the value
	 */
	public void doCurrencyValue(String mnemo, Date date, Double value);

	/**
	 * Do received currency values.
	 * 
	 * @param date
	 *            the date
	 * @param values
	 *            the values
	 */
	public void doCurrencyValues(Date date, HashMap<String, Double> values);
}
