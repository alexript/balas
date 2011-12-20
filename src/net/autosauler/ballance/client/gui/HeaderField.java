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

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * The Class HeaderField.
 * 
 * @author alexript
 */
public class HeaderField implements Listener<FieldEvent> {

	/** The w. */
	private Field<?> w;

	/** The t. */
	private final int t;

	/** The defval. */
	private final Object defval;

	/** The mytag. */
	private String mytag;

	/** The h. */
	private IFieldChangeHandler h;

	/**
	 * Instantiates a new header field.
	 * 
	 * @param name
	 *            the name
	 * @param widget
	 *            the widget
	 * @param type
	 *            the type
	 * @param defval
	 *            the defval
	 */
	public HeaderField(String name, int type, Object defval, Object helper) {
		super();
		h = null;
		this.defval = defval;
		t = type;
		createField(name, helper);

	}

	private void createField(String name, Object helper) {

		if (t == DataTypes.DT_SCRIPT) {
			w = new TextArea();

		} else if (t == DataTypes.DT_STRING) {
			w = new TextField<String>();

		} else if (t == DataTypes.DT_CURRENCY) {
			w = new CurrencySelector((String) defval);
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			// w = ((CatalogPanel) helper).getSelectBox((Long) defval);
		} else if (t == DataTypes.DT_DATE) {
			w = new DateField();
			((DateField) w).setFormatValue(true);

		} else if (t == DataTypes.DT_MONEY) {
			w = new TextField<Double>();

		} else if (t == DataTypes.DT_DOUBLE) {
			w = new TextField<Double>();

		} else if (t == DataTypes.DT_INT) {
			w = new TextField<Integer>();

		} else if (t == DataTypes.DT_LONG) {
			w = new TextField<Long>();

		} else if (t == DataTypes.DT_BOOLEAN) {
			w = new CheckBox();
		}

		if (w == null) {
			w = new TextField<String>();
			// TODO: create widgets for all datatypes
		}

		w.setFieldLabel(name);

	}

	public Field<?> getField() {
		return w;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public Object getValue() {
		Object o = null;
		if (t == DataTypes.DT_SCRIPT) {
			o = ((TextArea) w).getValue();
		} else if (t == DataTypes.DT_STRING) {
			o = ((TextField<String>) w).getValue();
		} else if (t == DataTypes.DT_MONEY) {
			o = ((TextField<Double>) w).getValue();
			// String v = ((TextBox) w).getText();
			// v = v.trim().replace(',', '.');
			// o = Double.parseDouble(v);
		} else if (t == DataTypes.DT_DOUBLE) {
			o = ((TextField<Double>) w).getValue();
			// String v = ((TextBox) w).getText();
			// v = v.trim().replace(',', '.');
			// o = Double.parseDouble(v);
		} else if (t == DataTypes.DT_CURRENCY) {
			o = ((CurrencySelector) w).getStrValue();
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			// o = ((CatalogSelector) w).getValue();
		} else if (t == DataTypes.DT_DATE) {
			o = ((DateField) w).getValue();
		} else if (t == DataTypes.DT_INT) {
			o = ((TextField<Integer>) w).getValue();
			// String v = ((TextBox) w).getText();
			// o = Integer.parseInt(v);
		} else if (t == DataTypes.DT_LONG) {
			o = ((TextField<Long>) w).getValue();
			// String v = ((TextBox) w).getText();
			// o = Long.parseLong(v);
		} else if (t == DataTypes.DT_BOOLEAN) {
			o = ((CheckBox) w).getValue();
		}
		return DataTypes.toMapping(t, o);
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public String getValueAsString() {
		Object o = null;
		if (t == DataTypes.DT_SCRIPT) {
			o = ((TextArea) w).getValue();
		} else if (t == DataTypes.DT_STRING) {
			o = ((TextField<String>) w).getValue();
		} else if (t == DataTypes.DT_MONEY) {
			o = ((TextField<Double>) w).getValue();
			// String v = ((TextBox) w).getText();
			// v = v.trim().replace(',', '.');
			// o = Double.parseDouble(v);
		} else if (t == DataTypes.DT_DOUBLE) {
			o = ((TextField<Double>) w).getValue();
			// String v = ((TextBox) w).getText();
			// v = v.trim().replace(',', '.');
			// o = Double.parseDouble(v);
		} else if (t == DataTypes.DT_CURRENCY) {
			o = ((CurrencySelector) w).getStrValue();
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			// o = ((CatalogSelector) w).getValue();
		} else if (t == DataTypes.DT_DATE) {
			o = ((DateField) w).getValue();
		} else if (t == DataTypes.DT_INT) {
			o = ((TextField<Integer>) w).getValue();
			// String v = ((TextBox) w).getText();
			// o = Integer.parseInt(v);
		} else if (t == DataTypes.DT_LONG) {
			o = ((TextField<Long>) w).getValue();
			// String v = ((TextBox) w).getText();
			// o = Long.parseLong(v);
		} else if (t == DataTypes.DT_BOOLEAN) {
			o = ((CheckBox) w).getValue();
		}
		return DataTypes.toString(t, o);
	}

	@Override
	public void handleEvent(FieldEvent be) {
		if (h != null) {
			h.handleFieldChange(mytag, getValueAsString());
		}

	}

	/**
	 * Reset.
	 */
	public void reset() {
		setValue(defval, false);
	}

	/**
	 * Sets the change handler.
	 * 
	 * @param tag
	 *            the tag
	 * @param handler
	 *            the handler
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setChangeHandler(String tag, IFieldChangeHandler handler) {
		mytag = tag;
		h = handler;

		if (t == DataTypes.DT_STRING) {
			((TextField<String>) w).addListener(Events.Change, this);

		} else if (t == DataTypes.DT_MONEY) {
			((TextField<Double>) w).addListener(Events.Change, this);

		} else if (t == DataTypes.DT_DOUBLE) {
			((TextField<Double>) w).addListener(Events.Change, this);

		} else if (t == DataTypes.DT_CURRENCY) {
			((CurrencySelector) w).addChangeHandler(this);
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			// ((CatalogSelector) w).addChangeHandler(this);
		} else if (t == DataTypes.DT_DATE) {
			((DateField) w).addListener(Events.Change, this);
		} else if (t == DataTypes.DT_INT) {
			((TextField<Integer>) w).addListener(Events.Change, this);

		} else if (t == DataTypes.DT_LONG) {
			((TextField<Long>) w).addListener(Events.Change, this);

		} else if (t == DataTypes.DT_BOOLEAN) {
			((CheckBox) w).addListener(Events.Change, this);
		}
	}

	/**
	 * Sets the value.
	 * 
	 * @param val
	 *            the new value
	 * @param castfrommap
	 *            the castfrommap
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object val, boolean castfrommap) {
		Object mval = val;
		if (castfrommap) {
			mval = DataTypes.fromMapping(t, val);
		}
		if (t == DataTypes.DT_SCRIPT) {
			((TextArea) w).setValue((String) mval);
		} else if (t == DataTypes.DT_STRING) {
			((TextField<String>) w).setValue((String) mval);
		} else if (t == DataTypes.DT_MONEY) {
			if (mval == null) {
				mval = 0.0d;
			}
			((TextField<Double>) w).setValue((Double) mval);
		} else if (t == DataTypes.DT_DOUBLE) {
			((TextField<Double>) w).setValue((Double) mval);
		} else if (t == DataTypes.DT_CURRENCY) {
			((CurrencySelector) w).select((String) mval);
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			// ((CatalogSelector) w).select((Long) mval);
		} else if (t == DataTypes.DT_DATE) {
			((DateField) w).setValue((Date) mval);
		} else if (t == DataTypes.DT_INT) {
			((TextField<Integer>) w).setValue((Integer) mval);
		} else if (t == DataTypes.DT_LONG) {
			((TextField<Long>) w).setValue((Long) mval);
		} else if (t == DataTypes.DT_BOOLEAN) {
			((CheckBox) w).setValue((Boolean) mval);
		}
	}

	/**
	 * Sets the value.
	 * 
	 * @param val
	 *            the new value
	 * @param castfrommap
	 *            the castfrommap
	 */
	@SuppressWarnings("unchecked")
	public void setValue(String val, boolean castfrommap) {
		Object mval = val;
		if (castfrommap) {
			mval = DataTypes.fromString(t, val);
		}
		if (t == DataTypes.DT_SCRIPT) {
			((TextArea) w).setValue((String) mval);
		} else if (t == DataTypes.DT_STRING) {
			((TextField<String>) w).setValue((String) mval);
		} else if (t == DataTypes.DT_MONEY) {
			((TextField<Double>) w).setValue((Double) mval);
		} else if (t == DataTypes.DT_DOUBLE) {
			((TextField<Double>) w).setValue((Double) mval);
		} else if (t == DataTypes.DT_CURRENCY) {
			((CurrencySelector) w).select((String) mval);
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			// ((CatalogSelector) w).select((Long) mval);
		} else if (t == DataTypes.DT_DATE) {
			((DateField) w).setValue((Date) mval);
		} else if (t == DataTypes.DT_INT) {
			((TextField<Integer>) w).setValue((Integer) mval);
		} else if (t == DataTypes.DT_LONG) {
			((TextField<Long>) w).setValue((Long) mval);
		} else if (t == DataTypes.DT_BOOLEAN) {
			((CheckBox) w).setValue((Boolean) mval);
		}
	}

}
