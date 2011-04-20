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

package net.autosauler.ballance.client.gui;

import java.util.Date;
import java.util.HashMap;

import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

/**
 * A factory for creating DataType objects.
 * 
 * @author alexript
 */
public class DataTypeFactory {

	/** The images. */
	private static MenuImages images = GWT.create(MenuImages.class);

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy/MM/dd");

	/**
	 * Adds the cell.
	 * 
	 * @param table
	 *            the table
	 * @param name
	 *            the name
	 * @param field
	 *            the field
	 * @param type
	 *            the type
	 * @param width
	 *            the width
	 * @param defval
	 *            the defval
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addCell(final CellTable table, String name,
			final String field, final int type, int width, final Object defval,
			final Object helper) {
		Column column = null;

		if (type == DataTypes.DT_BOOLEAN) {

			column = new Column<HashMap<String, Object>, ImageResource>(
					new ImageResourceCell()) {
				@Override
				public ImageResource getValue(HashMap<String, Object> map) {
					Boolean isactive = (Boolean) DataTypes.fromMapping(type,
							map.get(field));
					if (isactive) {
						return images.Ok();
					}
					return images.Cancel();
				}
			};

			column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		} else if (type == DataTypes.DT_DATE) {
			column = new Column<HashMap<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {

					return formatter.format((Date) DataTypes.fromMapping(type,
							map.get(field)));
				}
			};

		} else if (type == DataTypes.DT_CATALOGRECORD) {

			column = new Column<HashMap<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {

					return ((CatalogPanel) helper).getName((Long) DataTypes
							.fromMapping(type, map.get(field)));

				}
			};
		} else if (type == DataTypes.DT_DOCUMENTRECORD) {
			// TODO: add cell (docname, number, date)

		} else {
			column = new Column<HashMap<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {

					return DataTypes.fromMapping(type, map.get(field))
							.toString();
				}
			};

		}

		if (column != null) {
			table.addColumn(column, name);

			table.setColumnWidth(column, width, Unit.PX);
		}
	}

	/**
	 * Adds the editable cell.
	 * 
	 * @param table
	 *            the table
	 * @param name
	 *            the name
	 * @param field
	 *            the field
	 * @param type
	 *            the type
	 * @param width
	 *            the width
	 * @param defval
	 *            the defval
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addEditableCell(final CellTable table, String name,
			final String field, int type, int width, final Object defval,
			final Object helper) {
		Column column = null;

		if (type == DataTypes.DT_BOOLEAN) {
			column = new Column<HashMap<String, Object>, Boolean>(
					new CheckboxCell()) {
				@Override
				public Boolean getValue(HashMap<String, Object> map) {

					Object o = map.get(field);

					return (Boolean) o;
				}
			};
		} else if (type == DataTypes.DT_OBJECT) {
		} else if (type == DataTypes.DT_INT) {
		} else if (type == DataTypes.DT_DOUBLE) {
		} else if (type == DataTypes.DT_STRING) {
		} else if (type == DataTypes.DT_DATE) {
		} else if (type == DataTypes.DT_LONG) {
		} else if (type == DataTypes.DT_CURRENCY) {
		} else if (type == DataTypes.DT_CATALOG) {
		} else if (type == DataTypes.DT_CATALOGRECORD) {
		} else if (type == DataTypes.DT_DOCUMENT) {
		} else if (type == DataTypes.DT_DOCUMENTRECORD) {
		} else if (type == DataTypes.DT_SETTING) {
		} else if (type == DataTypes.DT_SETTINGVALUE) {
		} else if (type == DataTypes.DT_DOMAIN) {
		} else if (type == DataTypes.DT_MONEY) {
		} else if (type == DataTypes.DT_SCRIPT) {
		} else {

		}

		if (column != null) {
			table.addColumn(column, name);

			table.setColumnWidth(column, width, Unit.PX);
		}

	}
}
