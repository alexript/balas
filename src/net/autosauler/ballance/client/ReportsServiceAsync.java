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

import java.util.HashMap;
import java.util.List;

import net.autosauler.ballance.shared.ReportFormField;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author alexript
 * 
 */
public interface ReportsServiceAsync {

	/**
	 * 
	 * @see net.autosauler.ballance.client.ReportsService#generateReport(java.lang.String,
	 *      java.util.HashMap)
	 */
	void generateReport(String reportname, HashMap<String, String> params,
			AsyncCallback<String> callback);

	/**
	 * 
	 * @see net.autosauler.ballance.client.ReportsService#getFields(java.lang.String)
	 */
	void getFields(String reportname,
			AsyncCallback<List<ReportFormField>> callback);

}
