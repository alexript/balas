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

import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A factory for creating DataType objects.
 * 
 * @author alexript
 */
public class DataTypeFactory {
	// TODO: create simple widgets for view

	public static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy/MM/dd");
	public static final DateTimeFormat dateFormat = DateTimeFormat
			.getFormat("yyyy/MM/dd");

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
	public static ColumnConfig addCell(Field field, final Object helper) {
		ColumnConfig column = new ColumnConfig();
		final String fieldname = field.getFieldname();
		String colname = field.getName().getName(
				LocaleInfo.getCurrentLocale().getLocaleName());
		int width = field.getColumnwidth();
		final int type = field.getType();

		column.setId(fieldname);
		column.setHeader(colname);
		column.setWidth(width);
		column.setRowHeader(true);

		if (type == DataTypes.DT_BOOLEAN) {

			GridCellRenderer<BaseModelData> gridActive = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {

					Boolean isactive = (Boolean) model.get(fieldname);
					if (isactive) {
						return AbstractImagePrototype.create(Images.menu.Ok())
								.createImage();

					}
					return AbstractImagePrototype.create(Images.menu.Cancel())
							.createImage();

				}
			};
			column.setAlignment(HorizontalAlignment.CENTER);
			column.setRenderer(gridActive);

		} else if (type == DataTypes.DT_DATE) {
			GridCellRenderer<BaseModelData> gridDate = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
					Long longdate = (Long) model.get(fieldname);
					if (longdate == null) {
						return "---";
					}
					return DataTypeFactory.formatter.format(new Date(longdate));
				}
			};

			column.setRenderer(gridDate);

		} else if (type == DataTypes.DT_CATALOGRECORD) {
			GridCellRenderer<BaseModelData> gridCatalog = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
					Object o = DataTypes
							.fromMapping(type, model.get(fieldname));
					Long v = null;
					try {
						v = (Long) o;
					} catch (java.lang.ClassCastException e) {
						return null;
					}
					return ((CatalogPanel) helper).getName(v);

				}
			};

			column.setRenderer(gridCatalog);

		} else if (type == DataTypes.DT_DOCUMENTRECORD) {
			GridCellRenderer<BaseModelData> gridDocument = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {

					Object o = DataTypes
							.fromMapping(type, model.get(fieldname));
					Long v = null;
					try {
						v = (Long) o;
					} catch (java.lang.ClassCastException e) {
						return null;
					}
					return ((DocumentPanel) helper).getName(v);

				}
			};

			column.setRenderer(gridDocument);

		} else {
			// default renderer
		}

		return column;
	}

	public static ColumnConfig addEditableCell(Field field,
			final Object helper, ITableFieldChangeHandler changehandler) {

		ColumnConfig column = new ColumnConfig();
		final String fieldname = field.getFieldname();
		String colname = field.getName().getName(
				LocaleInfo.getCurrentLocale().getLocaleName());
		int width = field.getColumnwidth();
		Object defval = field.getDefval();
		final int type = field.getType();

		column.setId(fieldname);
		column.setHeader(colname);
		column.setWidth(width);
		column.setRowHeader(true);

		if (type == DataTypes.DT_BOOLEAN) {

			GridCellRenderer<BaseModelData> gridActive = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {

					Boolean isactive = (Boolean) model.get(fieldname);
					if (isactive) {
						return AbstractImagePrototype.create(Images.menu.Ok())
								.createImage();

					}
					return AbstractImagePrototype.create(Images.menu.Cancel())
							.createImage();

				}
			};
			column.setAlignment(HorizontalAlignment.CENTER);
			column.setRenderer(gridActive);

		} else if (type == DataTypes.DT_DATE) {
			GridCellRenderer<BaseModelData> gridDate = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
					Date longdate = (Date) model.get(fieldname);
					if (longdate == null) {
						return new Date();
					}
					return DataTypeFactory.formatter.format(longdate);
				}
			};

			column.setRenderer(gridDate);

		} else if (type == DataTypes.DT_CATALOGRECORD) {
			GridCellRenderer<BaseModelData> gridCatalog = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {

					return ((CatalogPanel) helper).getName((Long) model
							.get(fieldname));

				}
			};

			column.setRenderer(gridCatalog);

		} else if (type == DataTypes.DT_DOCUMENTRECORD) {
			GridCellRenderer<BaseModelData> gridDocument = new GridCellRenderer<BaseModelData>() {

				@Override
				public Object render(BaseModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<BaseModelData> store, Grid<BaseModelData> grid) {

					return ((DocumentPanel) helper).getName((Long) model
							.get(fieldname));

				}
			};

			column.setRenderer(gridDocument);

		} else {
			// default renderer
		}

		HeaderField hf = new HeaderField(fieldname, type, defval, helper);

		if (changehandler != null) {
			hf.setCellChangeHandler(fieldname, changehandler);
		}

		column.setEditor(hf.getCellEditor());

		return column;
	}

	/**
	 * Adds the field.
	 * 
	 * @param name
	 *            the name
	 * @param field
	 *            the field
	 * @param type
	 *            the type
	 * @param defval
	 *            the defval
	 * @param helper
	 *            the helper
	 * @return the header field
	 */
	public static HeaderField addField(final String name, final String field,
			final int type, final Object defval, final Object helper) {

		HeaderField hf = new HeaderField(name, type, defval, helper);
		hf.reset();
		return hf;
	}

}
