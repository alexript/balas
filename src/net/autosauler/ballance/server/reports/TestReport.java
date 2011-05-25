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

import java.util.HashMap;

import com.googlecode.g2re.HTMLReportBuilder;
import com.googlecode.g2re.domain.ReportDefinition;
import com.googlecode.g2re.html.Label;

/**
 * @author alexript
 * 
 */
public class TestReport {
	private final ReportDefinition report;

	public TestReport(String name) {
		report = new ReportDefinition();
		report.setName(name);
	}

	public void addLabel(String text) {
		Label lab = new Label(text);
		report.getWebPage().addChildElement(lab);
	}

	public void addQuery(Query q) {
		report.getDataQueries().add(q);
		report.getWebPage().addChildElement(q.getTable());
	}

	@SuppressWarnings("rawtypes")
	public String build() {
		return HTMLReportBuilder.build(report, new HashMap(), true);
	}

	public void setDescription(String descr) {
		report.setDescription(descr);
	}
}
