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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.client.utils.SimpleDateFormat;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class CurrencyValuesStorage.
 */
public class CurrencyValuesStorage {

	/** The cache. */
	private static HashMap<String, Double> cache = null;

	/** The date formatter. */
	private static SimpleDateFormat formatter = null;

	/** The Constant KEYDATEFORMATTER. */
	private static final String KEYDATEFORMATTER = "yyyyMMdd";

	/**
	 * Clean cache.
	 */
	public static void clean() {
		if (cache != null) {
			cache.clear();
			cache = null;
		}
	}

	/**
	 * Gets the currency values for date.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param date
	 *            the date
	 * @param mnemos
	 *            the set of currency mnemos
	 */
	public static void get(final ICurrencyValuesReceiver receiver,
			final Date date, Set<String> mnemos) {
		final HashMap<String, Double> values = new HashMap<String, Double>(); // found
																				// values
		final List<String> notfoundmnemos = new ArrayList<String>(); // mnemos
																		// is
																		// not
																		// in
																		// cache

		if (formatter == null) {
			formatter = new SimpleDateFormat(KEYDATEFORMATTER);
		}
		if (cache == null) {
			cache = new HashMap<String, Double>();
		}

		final String day = formatter.format(date);

		// search values in cache
		Iterator<String> i = mnemos.iterator();
		while (i.hasNext()) {
			String mnemo = i.next();
			String key = mnemo + day;

			if (!cache.containsKey(key)) {
				notfoundmnemos.add(mnemo); // not found
			} else {
				values.put(mnemo, cache.get(key)); // found
			}

		}

		if (!notfoundmnemos.isEmpty()) { // if not found some mnemos
			MainPanel.setCommInfo(true);
			Services.currency.get(notfoundmnemos, date,
					new AsyncCallback<HashMap<String, Double>>() { // get values
						// from
						// server

						@Override
						public void onFailure(Throwable caught) {
							// comm error - set values to default
							Double defaultval = new Double(1.0);
							Iterator<String> i = notfoundmnemos.iterator();
							while (i.hasNext()) {
								String mnemo = i.next();
								values.put(mnemo, defaultval);
							}

							MainPanel.setCommInfo(false);
							new AlertDialog(caught).show();

							receiver.doCurrencyValues(date, values);

						}

						@Override
						public void onSuccess(HashMap<String, Double> result) {
							// ok, values received
							Set<String> keys = result.keySet();
							Iterator<String> i = keys.iterator();
							while (i.hasNext()) {
								String mnemo = i.next();
								Double val = result.get(mnemo);
								cache.put(mnemo + day, val);
								values.put(mnemo, val);
							}
							MainPanel.setCommInfo(false);
							receiver.doCurrencyValues(date, values);
						}
					});

		} else {
			// no problem: all values is in cache
			receiver.doCurrencyValues(date, values);
		}
	}

	/**
	 * Gets the currency values for today.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param mnemos
	 *            the set of currency mnemos
	 */
	public static void get(ICurrencyValuesReceiver receiver, Set<String> mnemos) {
		get(receiver, new Date(), mnemos);
	}

	/**
	 * Gets the currency value for today.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param mnemo
	 *            the mnemo
	 */
	public static void get(ICurrencyValuesReceiver receiver, String mnemo) {
		get(receiver, mnemo, new Date());
	}

	/**
	 * Gets the currency value for day.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param mnemo
	 *            the mnemo
	 * @param date
	 *            the date
	 */
	public static void get(final ICurrencyValuesReceiver receiver,
			final String mnemo, final Date date) {
		Double value = new Double(1.0);

		if (formatter == null) {
			formatter = new SimpleDateFormat(KEYDATEFORMATTER);
		}

		String day = formatter.format(date);
		final String key = mnemo + day;

		if (cache == null) {
			cache = new HashMap<String, Double>();
		}

		if (!cache.containsKey(key)) {
			MainPanel.setCommInfo(true);
			Services.currency.get(mnemo, date, new AsyncCallback<Double>() {

				@Override
				public void onFailure(Throwable caught) {
					MainPanel.setCommInfo(false);
					new AlertDialog(caught).show();
					receiver.doCurrencyValue(mnemo, date, new Double(1.0));

				}

				@Override
				public void onSuccess(Double result) {
					MainPanel.setCommInfo(false);
					cache.put(key, result);
					receiver.doCurrencyValue(mnemo, date, result);

				}
			});

		} else {
			value = cache.get(key);
			receiver.doCurrencyValue(mnemo, date, value);
		}
	}
}
