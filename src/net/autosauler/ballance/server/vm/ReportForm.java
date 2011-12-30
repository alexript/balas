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
package net.autosauler.ballance.server.vm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.autosauler.ballance.server.model.Structures;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.ReportFormField;
import net.autosauler.ballance.shared.datatypes.DataTypes;

/**
 * The Class ReportForm.
 * 
 * @author alexript
 */
public class ReportForm {

	/** The fields. */
	private final List<ReportFormField> fields;
	private final String reportname;

	/**
	 * Instantiates a new report form.
	 */
	public ReportForm(String name, String domain) {
		reportname = name;
		fields = new ArrayList<ReportFormField>();
		Structures s = new Structures(domain);

		Description d = s.getDescription(reportname);

		List<Field> fields = d.get();
		for (Field field : fields) {
			// TODO: send all names. client-side selection is.
			add(field.getFieldname(), field.getName().getName("ru"),
					field.getType(), field.getDefval());
		}
	}

	/**
	 * Adds the.
	 * 
	 * @param name
	 *            the name
	 * @param descr
	 *            the descr
	 * @param type
	 *            the type
	 */
	public void add(String name, String descr, Integer type) {
		add(name, descr, type, null);
	}

	/**
	 * Adds the.
	 * 
	 * @param name
	 *            the name
	 * @param descr
	 *            the descr
	 * @param type
	 *            the type
	 * @param defval
	 *            the defval
	 */
	public void add(String name, String descr, Integer type, Object defval) {
		ReportFormField field = new ReportFormField();
		field.setName(name);
		field.setDescr(descr);
		field.setType(type);
		field.setDefval(type, defval);
		fields.add(field);
	}

	/**
	 * Adds the.
	 * 
	 * @param name
	 *            the name
	 * @param descr
	 *            the descr
	 * @param type
	 *            the type
	 */
	public void add(String name, String descr, String type) {
		int t = DataTypes.fromString(type);
		add(name, descr, t);
	}

	/**
	 * Gets the fields.
	 * 
	 * @return the fields
	 */
	public List<ReportFormField> getFields() {
		return fields;
	}

	/**
	 * Gets the type.
	 * 
	 * @param name
	 *            the name
	 * @return the type
	 */
	public int getType(String name) {
		Iterator<ReportFormField> i = fields.iterator();
		while (i.hasNext()) {
			ReportFormField f = i.next();
			if (f.getName().equals(name)) {
				return f.getType();
			}
		}
		return DataTypes.DT_OBJECT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Form:\n");
		Iterator<ReportFormField> i = fields.iterator();
		while (i.hasNext()) {
			ReportFormField f = i.next();
			sb.append(f.getName() + " '" + f.getDescr() + "' of " + f.getType()
					+ " defval=" + f.getDefval() + "\n");
		}
		return sb.toString();
	}

}
