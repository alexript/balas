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

package net.autosauler.ballance.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface CurrencyServiceAsync.
 * 
 * @author alexript
 */
public interface CurrencyServiceAsync {

	/**
	 * Gets the.
	 * 
	 * @param notfoundmnemos
	 *            the mnemos
	 * @param date
	 *            the date
	 * @param callback
	 *            the callback
	 */
	void get(List<String> notfoundmnemos, Date date,
			AsyncCallback<HashMap<String, Double>> callback);

	/**
	 * Gets the.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param date
	 *            the date
	 * @param callback
	 *            the callback
	 * @see net.autosauler.ballance.client.CurrencyService#get(java.lang.String,
	 *      java.util.Date)
	 */
	void get(String mnemo, Date date, AsyncCallback<Double> callback);

	/**
	 * Gets the used currencyes.
	 * 
	 * @param callback
	 *            the callback
	 * @return the used currencyes
	 */
	void getUsedCurrencyes(AsyncCallback<Set<String>> callback);

}
