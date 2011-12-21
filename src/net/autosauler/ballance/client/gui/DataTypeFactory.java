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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Services;
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
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A factory for creating DataType objects.
 * 
 * @author alexript
 */
public class DataTypeFactory {
	// TODO: create simple widgets for view
	// TODO: rewrite SelectionCell for catalogrecord

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

					return ((CatalogPanel) helper).getName((Long) DataTypes
							.fromMapping(type, model.get(fieldname)));

				}
			};

			column.setRenderer(gridCatalog);

		} else if (type == DataTypes.DT_DOCUMENTRECORD) {
			// TODO: add cell (docname, number, date)

		} else {
			// default renderer
		}

		return column;
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
	 * @param tag
	 * @param changehandler
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addEditableCell(final CellTable table,
			final String name, final String field, final int type,
			final int width, final Object defval, final Object helper,
			final ITableFieldChangeHandler changehandler, final String tag) {
		Column column = null;

		if (type == DataTypes.DT_BOOLEAN) {
			column = new Column<HashMap<String, Object>, Boolean>(
					new CheckboxCell()) {
				@Override
				public Boolean getValue(HashMap<String, Object> map) {

					return (Boolean) DataTypes
							.fromMapping(type, map.get(field));
				}
			};

			((Column<HashMap<String, Object>, Boolean>) column)
					.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, Boolean>() {

						@Override
						public void update(int index,
								HashMap<String, Object> object, Boolean value) {
							object.put(field, value);
							if (changehandler != null) {
								changehandler.handleFieldChange(tag, object);
							}

						}
					});

		} else if (type == DataTypes.DT_DATE) {
			column = new Column<HashMap<String, Object>, Date>(
					new DatePickerCell(dateFormat)) {
				@Override
				public Date getValue(HashMap<String, Object> map) {

					return (Date) DataTypes.fromMapping(type, map.get(field));
				}

			};

			column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, Date>() {

				@Override
				public void update(int index, HashMap<String, Object> object,
						Date value) {
					object.put(field, DataTypes.toMapping(type, value));
					if (changehandler != null) {
						changehandler.handleFieldChange(tag, object);
					}

				}
			});

		} else if (type == DataTypes.DT_CURRENCY) {
			final int lastcolumn = table.getColumnCount();
			MainPanel.setCommInfo(true);

			Services.currency
					.getUsedCurrencyes(new AsyncCallback<Set<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							MainPanel.setCommInfo(false);
							new AlertDialog(caught).show();
						}

						@Override
						public void onSuccess(Set<String> result) {
							if (result != null) {

								List<String> currlist = new ArrayList<String>(
										result);

								Column column = new Column<HashMap<String, Object>, String>(
										new SelectionCell(currlist)) {
									@Override
									public String getValue(
											HashMap<String, Object> map) {

										return (DataTypes.fromMapping(type,
												map.get(field))).toString();
									}

								};

								column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

									@Override
									public void update(int index,
											HashMap<String, Object> object,
											String value) {
										object.put(field, DataTypes.toMapping(
												type, value));
										if (changehandler != null) {
											changehandler.handleFieldChange(
													tag, object);
										}

									}
								});
								if (table.getColumnCount() != lastcolumn) {
									table.insertColumn(lastcolumn, column, name);
								} else {
									table.addColumn(column, name);
								}
								table.setColumnWidth(column, width, Unit.PX);

							}

							MainPanel.setCommInfo(false);

						}
					});

		} else if (type == DataTypes.DT_CATALOGRECORD) {

			final int lastcolumn = table.getColumnCount();
			MainPanel.setCommInfo(true);

			Services.catalogs.getRecordsForSelection(
					((CatalogPanel) helper).getCatalogname(),
					new AsyncCallback<HashMap<String, Long>>() {

						@Override
						public void onFailure(Throwable caught) {
							MainPanel.setCommInfo(false);
							new AlertDialog(caught).show();
						}

						@Override
						public void onSuccess(final HashMap<String, Long> result) {
							// TODO: there must be easier way...
							if (result != null) {
								final HashMap<Long, String> mylist = new HashMap<Long, String>();
								mylist.put(0L, "[...]");
								Set<String> names = result.keySet();
								Iterator<String> i = names.iterator();
								while (i.hasNext()) {
									String name = i.next();
									mylist.put(result.get(name), name);
								}
								List<String> reclist = new ArrayList<String>();
								reclist.add("[...]");
								reclist.addAll(names);

								Column column = new Column<HashMap<String, Object>, String>(
										new SelectionCell(reclist)) {
									@Override
									public String getValue(
											HashMap<String, Object> map) {

										return mylist.get((DataTypes
												.fromMapping(type,
														map.get(field))));
									}

								};

								column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

									@Override
									public void update(int index,
											HashMap<String, Object> object,
											String value) {
										if (!result.containsKey(value)) {
											object.put(field, DataTypes
													.toMapping(type, 0L));
										} else {
											object.put(field, DataTypes
													.toMapping(type,
															result.get(value)));
										}
										if (changehandler != null) {
											changehandler.handleFieldChange(
													tag, object);
										}
									}
								});

								// TODO: fix columns order for many async
								// creations
								if (table.getColumnCount() != lastcolumn) {
									table.insertColumn(lastcolumn, column, name);
								} else {
									table.addColumn(column, name);
								}
								table.setColumnWidth(column, width, Unit.PX);

							}

							MainPanel.setCommInfo(false);

						}
					});

			// TODO: add this types (sometime in the future)
			// } else if (type == DataTypes.DT_DOCUMENTRECORD) {
			// } else if (type == DataTypes.DT_SETTINGVALUE) {
		} else if (type == DataTypes.DT_INT) {
			column = new Column<HashMap<String, Object>, String>(
					new TextInputCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {

					return (DataTypes.fromMapping(type, map.get(field)))
							.toString();
				}

			};

			column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

				@Override
				public void update(int index, HashMap<String, Object> object,
						String value) {
					object.put(field,
							DataTypes.toMapping(type, Integer.parseInt(value)));
					if (changehandler != null) {
						changehandler.handleFieldChange(tag, object);
					}

				}
			});
		} else if (type == DataTypes.DT_LONG) {
			column = new Column<HashMap<String, Object>, String>(
					new TextInputCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {

					return (DataTypes.fromMapping(type, map.get(field)))
							.toString();
				}

			};

			column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

				@Override
				public void update(int index, HashMap<String, Object> object,
						String value) {
					object.put(field,
							DataTypes.toMapping(type, Long.parseLong(value)));
					if (changehandler != null) {
						changehandler.handleFieldChange(tag, object);
					}

				}
			});
		} else if (type == DataTypes.DT_MONEY) {
			column = new Column<HashMap<String, Object>, String>(
					new TextInputCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {
					Object val = map.get(field);
					String ret = "---";
					if (val != null) {
						ret = (DataTypes.fromMapping(type, val)).toString();
					}
					return ret;
				}

			};

			column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

				@Override
				public void update(int index, HashMap<String, Object> object,
						String value) {
					Double d = Double.parseDouble(value.replace(',', '.'));
					object.put(field, DataTypes.toMapping(type, d));
					if (changehandler != null) {
						changehandler.handleFieldChange(tag, object);
					}

				}
			});
		} else if (type == DataTypes.DT_DOUBLE) {
			column = new Column<HashMap<String, Object>, String>(
					new TextInputCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {

					return (DataTypes.fromMapping(type, map.get(field)))
							.toString();
				}

			};

			column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

				@Override
				public void update(int index, HashMap<String, Object> object,
						String value) {
					Double d = Double.parseDouble(value.replace(',', '.'));
					object.put(field, DataTypes.toMapping(type, d));
					if (changehandler != null) {
						changehandler.handleFieldChange(tag, object);
					}

				}
			});
		} else {
			column = new Column<HashMap<String, Object>, String>(
					new TextInputCell()) {
				@Override
				public String getValue(HashMap<String, Object> map) {
					String ret = "";
					Object val = map.get(field);
					if (val != null) {
						ret = (DataTypes.fromMapping(type, val)).toString();
					}
					return ret;
				}

			};

			column.setFieldUpdater(new FieldUpdater<HashMap<String, Object>, String>() {

				@Override
				public void update(int index, HashMap<String, Object> object,
						String value) {
					object.put(field, DataTypes.toMapping(type, value));
					if (changehandler != null) {
						changehandler.handleFieldChange(tag, object);
					}

				}
			});
		}

		if (column != null) {
			table.addColumn(column, name);

			table.setColumnWidth(column, width, Unit.PX);
		}

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
			// TODO: add cell (docname, number, date)

		} else {
			// default renderer
		}

		HeaderField hf = new HeaderField(fieldname, type, defval, helper);

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
