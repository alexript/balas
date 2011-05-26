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
package net.autosauler.ballance.server.reports;

import java.util.Date;
import java.util.HashMap;

import net.autosauler.ballance.shared.datatypes.DataTypes;

/**
 * The Class Generator.
 * 
 * @author alexript
 */
public class Generator {

	/**
	 * Generate.
	 * 
	 * @param reportname
	 *            the reportname
	 * @return the string
	 */
	public static String generate(String reportname) {
		if (reportname.equals("test")) {

			HashMap<String, String> p = new HashMap<String, String>();
			p.put("currency", "USD");
			p.put("startd", DataTypes.toString(DataTypes.DT_DATE, new Date()));

			Query q = new Query("127.0.0.1", "crrvalues", p);
			System.out.println(q.getFormDescription().toString());
			return q.getResult();
		}

		return "Uncknown report " + reportname;
	}
}
