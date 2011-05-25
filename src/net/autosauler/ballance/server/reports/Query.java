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
import java.util.List;

import javax.script.ScriptException;

import net.autosauler.ballance.server.vm.VM;

import com.allen_sauer.gwt.log.client.Log;
import com.googlecode.g2re.domain.DataColumn;
import com.googlecode.g2re.domain.DataQuery;
import com.googlecode.g2re.domain.DataType;
import com.googlecode.g2re.html.DataElement;
import com.googlecode.g2re.html.DataTable;
import com.googlecode.g2re.html.GridCell;
import com.googlecode.g2re.html.GridRow;
import com.googlecode.g2re.html.RawHTML;
import com.googlecode.g2re.jdbc.DataSet;

/**
 * @author alexript
 * 
 */
public class Query extends DataQuery {
	private final String scriptname;
	private String scripttext;

	private final DataTable table;
	private int counter;
	private final GridRow tableHeader;
	private final GridRow tableBody;
	private final DataSet set;
	private final VM vm;
	private final List<Object> currentrow;

	public Query(String reportscriptname) {

		setName(reportscriptname);
		scriptname = "report." + reportscriptname;
		table = new DataTable();
		tableHeader = new GridRow();
		tableBody = new GridRow();
		set = new DataSet();
		currentrow = new ArrayList<Object>();
		vm = new VM("127.0.0.1");

		counter = 0;

	}

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
			vm.putObject("Report", this);
			vm.eval(scripttext);
		} catch (ScriptException e) {
			Log.error(e.getMessage());
		}
		return set;
	}

	public DataTable getTable() {
		table.setDataQuery(this);
		table.getHeaderRows().add(tableHeader);
		table.getBodyRows().add(tableBody);
		return table;
	}

	public void putValue(Object val) {
		currentrow.add(val);
	}

	public void setScriptText(String text) {
		scripttext = text;
	}

}
