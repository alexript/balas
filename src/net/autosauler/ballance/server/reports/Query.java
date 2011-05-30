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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptException;

import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.server.model.IScriptableObject;
import net.autosauler.ballance.server.model.Scripts;
import net.autosauler.ballance.server.vm.ReportForm;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.googlecode.g2re.HTMLReportBuilder;
import com.googlecode.g2re.domain.DataColumn;
import com.googlecode.g2re.domain.DataQuery;
import com.googlecode.g2re.domain.DataType;
import com.googlecode.g2re.domain.ReportDefinition;
import com.googlecode.g2re.html.DataElement;
import com.googlecode.g2re.html.DataTable;
import com.googlecode.g2re.html.GridCell;
import com.googlecode.g2re.html.GridRow;
import com.googlecode.g2re.html.Label;
import com.googlecode.g2re.html.RawHTML;
import com.googlecode.g2re.jdbc.DataSet;

/**
 * The Class Query.
 * 
 * @author alexript
 */
public class Query extends DataQuery implements IScriptableObject {

	/** The report. */
	private ReportDefinition report;

	/** The scriptname. */
	private String scriptname;

	/** The table. */
	private DataTable table;

	/** The counter. */
	private int counter;

	/** The table header. */
	private GridRow tableHeader;

	/** The table body. */
	private GridRow tableBody;

	/** The set. */
	private DataSet set;

	/** The currentrow. */
	private List<Object> currentrow;

	/** The domain. */
	private String domain;

	/** The script. */
	private Scripts script;

	/** The params. */
	private final HashMap<String, String> params;

	/** The form. */
	private ReportForm form;

	/** The Constant formatter. */
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy/MM/dd");

	/**
	 * Instantiates a new query.
	 * 
	 * @param domain
	 *            the domain
	 * @param reportscriptname
	 *            the reportscriptname
	 */
	public Query(String domain, String reportscriptname) {
		initMembers(domain, reportscriptname);
		params = new HashMap<String, String>();
	}

	/**
	 * Instantiates a new query.
	 * 
	 * @param domain
	 *            the domain
	 * @param reportscriptname
	 *            the reportscriptname
	 * @param params
	 *            the params
	 */
	public Query(String domain, String reportscriptname,
			HashMap<String, String> params) {
		initMembers(domain, reportscriptname);
		this.params = params;
	}

	/**
	 * Adds the column.
	 * 
	 * @param name
	 *            the name
	 */
	@SuppressWarnings("unchecked")
	public void addColumn(String name) {
		DataColumn col = new DataColumn();
		col.setName(name);
		col.setOrder(counter);
		col.setType(DataType.STRING);
		getColumns().add(col);
		tableHeader.addCell(new GridCell(new RawHTML(name)));
		tableBody.addCell(new GridCell(new DataElement(col, counter)));

		counter++;
	}

	/**
	 * Adds the description.
	 * 
	 * @param descr
	 *            the descr
	 */
	public void addDescription(String descr) {
		report.setDescription(descr);
	}

	/**
	 * Adds the label.
	 * 
	 * @param text
	 *            the text
	 */
	public void addLabel(String text) {
		Label lab = new Label(text);
		report.getWebPage().addChildElement(lab);
	}

	/**
	 * Draw row.
	 */
	@SuppressWarnings({ "unchecked" })
	public void drawRow() {
		set.getRows().add(currentrow.toArray(new Object[currentrow.size()]));
		currentrow.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.g2re.domain.DataQuery#execute()
	 */
	@Override
	public DataSet execute() {
		// eval execute(this)
		try {

			script.call("ExecuteReport", this);
			table.getHeaderRows().add(tableHeader);
			table.getBodyRows().add(tableBody);
			report.getWebPage().addChildElement(table);

		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.IScriptableObject#generateDefaultScript
	 * ()
	 */
	@Override
	public String generateDefaultScript() {
		StringBuilder sb = new StringBuilder();

		sb.append("function CreateForm(form)\n");
		sb.append("form.add('currency', 'Currency', DT_CURRENCY, 'USD')\n");
		sb.append("form.add('startd', 'Start date', 'DT_DATE')\n");
		sb.append("form.add('endd', 'End date', 'DT_DATE')\n");
		sb.append("end\n\n");
		sb.append("function ExecuteReport(report)\n");
		sb.append("report.addLabel('Currency values (' + report.get('currency') + ')')\n");
		sb.append("report.addDescription('Currency values per date')\n");
		sb.append("report.addColumn('Date')\n");
		sb.append("report.addColumn('Value')\n");

		sb.append("values = Currency.get(report.get('currency'), report.get('startd'), report.get('endd'))\n");
		sb.append("i = values.iterator()\n");
		sb.append("while i.hasNext()\n");
		sb.append(" v = i.next()\n");
		sb.append(" report.putValue(v.getDate())\n");
		sb.append(" report.putValue(v.getValue())\n");
		sb.append(" report.drawRow()\n");
		sb.append("end\n");

		sb.append("end\n");

		String script = sb.toString();
		return script;
	}

	/**
	 * Gets the.
	 * 
	 * @param name
	 *            the name
	 * @return the object
	 */
	public Object get(String name) {
		if (!params.containsKey(name)) {
			return null;
		}

		return DataTypes.fromString(form.getType(name), params.get(name));
	}

	/**
	 * Gets the form description.
	 * 
	 * @return the form description
	 */
	public ReportForm getFormDescription() {

		return form;
	}

	/**
	 * Gets the result.
	 * 
	 * @return the result
	 */
	@SuppressWarnings("rawtypes")
	public String getResult() {

		report.getDataQueries().add(this);
		table.setDataQuery(this);

		return HTMLReportBuilder.build(report, new HashMap(), true);
	}

	/**
	 * Inits the members.
	 * 
	 * @param domain
	 *            the domain
	 * @param reportscriptname
	 *            the reportscriptname
	 */
	private void initMembers(String domain, String reportscriptname) {
		this.domain = domain;
		report = new ReportDefinition();
		report.setName(reportscriptname);
		setName(reportscriptname);
		scriptname = "report." + reportscriptname;
		table = new DataTable();
		tableHeader = new GridRow();
		tableBody = new GridRow();
		set = new DataSet();
		currentrow = new ArrayList<Object>();

		script = new Scripts(this, this.domain, scriptname);

		counter = 0;

		form = new ReportForm();

		try {
			script.call("CreateForm", form);
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
		}

	}

	/**
	 * Put value.
	 * 
	 * @param val
	 *            the val
	 */
	public void putValue(Object val) {
		if (val instanceof Date) {
			currentrow.add(formatter.format((Date) val));
		} else {
			currentrow.add(val);
		}
	}

}
