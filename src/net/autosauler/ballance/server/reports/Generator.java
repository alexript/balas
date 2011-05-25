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

/**
 * @author alexript
 * 
 */
public class Generator {
	public static String generate(String reportname) {
		if (reportname.equals("test")) {
			TestReport report = new TestReport("Test");
			report.setDescription("Test report");
			report.addLabel("test label");

			Query q = new Query("testquery");
			StringBuilder sb = new StringBuilder();

			sb.append("Report.addColumn('column 1')\n");
			sb.append("Report.addColumn('column 2')\n");

			sb.append("Report.putValue('Test 1 string 1')\n");
			sb.append("Report.putValue('Test 1 string 2')\n");
			sb.append("Report.drawRow()\n");

			sb.append("Report.putValue('Test 2 string 1')\n");
			sb.append("Report.putValue('Test 2 string 2')\n");
			sb.append("Report.drawRow()\n");

			String script = sb.toString();
			System.out.println(script);
			q.setScriptText(script);
			report.addQuery(q);

			return report.build();
		}

		return "Uncknown report " + reportname;
	}
}
