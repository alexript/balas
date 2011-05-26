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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface ReportsService.
 * 
 * @author alexript
 */

@RemoteServiceRelativePath("reports")
public interface ReportsService extends RemoteService {

	/**
	 * Generate report.
	 * 
	 * @param reportname
	 *            the reportname
	 * @param params
	 *            the params
	 * @return the string
	 */
	public String generateReport(String reportname,
			HashMap<String, String> params);

	/**
	 * Gets the fields.
	 * 
	 * @param reportname
	 *            the reportname
	 * @return the fields
	 */
	public List<ReportFormField> getFields(String reportname);
}
