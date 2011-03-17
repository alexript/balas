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
package net.autosauler.ballance.server;

import java.util.Date;
import java.util.List;

import net.autosauler.ballance.client.CurrencyService;
import net.autosauler.ballance.server.model.Currency;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class CurrencyServiceImpl.
 * 
 * @author alexript
 */
public class CurrencyServiceImpl extends RemoteServiceServlet implements
		CurrencyService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8839579117725004124L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.CurrencyService#get(java.util.Set,
	 * java.util.Date)
	 */
	@Override
	public java.util.HashMap<String, Double> get(List<String> mnemos, Date date) {
		return Currency.get(mnemos, date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.CurrencyService#get(java.lang.String,
	 * java.util.Date)
	 */
	@Override
	public Double get(String mnemo, Date date) {
		return Currency.get(mnemo, date);
	}

}
