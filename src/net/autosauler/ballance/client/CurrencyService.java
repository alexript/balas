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
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface CurrencyService.
 * 
 * @author alexript
 */

@RemoteServiceRelativePath("currency")
public interface CurrencyService extends RemoteService {

	/**
	 * Gets the currency values from server.
	 * 
	 * @param mnemos
	 *            the currency mnemos
	 * @param date
	 *            the date
	 * @return the hash map
	 */
	public java.util.HashMap<String, Double> get(List<String> mnemos, Date date);

	/**
	 * Gets the currency value from server.
	 * 
	 * @param mnemo
	 *            the currency mnemo
	 * @param date
	 *            the date
	 * @return the double
	 */
	public Double get(String mnemo, Date date);
}
