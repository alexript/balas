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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * The Class HeaderField.
 * 
 * @author alexript
 */
public class HeaderField extends HorizontalPanel {

	/** The w. */
	private final Widget w;

	/** The t. */
	private final int t;

	private final Object defval;

	/**
	 * Instantiates a new header field.
	 * 
	 * @param name
	 *            the name
	 * @param widget
	 *            the widget
	 * @param type
	 *            the type
	 */
	public HeaderField(String name, Widget widget, int type, Object defval) {
		super();
		w = widget;
		t = type;
		this.defval = defval;
		setWidth("600px");
		setSpacing(5);
		Label lblname = new Label(name);
		this.add(lblname);
		setCellWidth(lblname, "100px");
		this.add(w);
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		Object o = null;
		if (t == DataTypes.DT_SCRIPT) {
			o = ((TextArea) w).getText();
		} else if (t == DataTypes.DT_STRING) {
			o = ((TextBox) w).getText();
		} else if (t == DataTypes.DT_MONEY) {
			String v = ((TextBox) w).getText();
			v = v.trim().replace(',', '.');
			o = Double.parseDouble(v);
		} else if (t == DataTypes.DT_CURRENCY) {
			o = ((CurrencySelector) w).getValue();
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			o = ((CatalogSelector) w).getValue();
		} else if (t == DataTypes.DT_DATE) {
			o = ((DateBox) w).getValue();
		} else if (t == DataTypes.DT_INT) {
			String v = ((TextBox) w).getText();
			o = Integer.parseInt(v);
		}
		return DataTypes.toMapping(t, o);
	}

	public void reset() {
		setValue(defval);
	}

	/**
	 * Sets the value.
	 * 
	 * @param val
	 *            the new value
	 */
	public void setValue(Object val) {
		Object mval = DataTypes.fromMapping(t, val);
		if (t == DataTypes.DT_SCRIPT) {
			((TextArea) w).setText((String) mval);
		} else if (t == DataTypes.DT_STRING) {
			((TextBox) w).setText((String) mval);
		} else if (t == DataTypes.DT_MONEY) {
			((TextBox) w).setText(((Double) mval).toString());
		} else if (t == DataTypes.DT_CURRENCY) {
			((CurrencySelector) w).select((String) mval);
		} else if (t == DataTypes.DT_CATALOGRECORD) {
			((CatalogSelector) w).select((Long) mval);
		} else if (t == DataTypes.DT_DATE) {
			((DateBox) w).setValue((Date) mval);
		} else if (t == DataTypes.DT_INT) {
			((TextBox) w).setText(((Integer) mval).toString());
		}
	}

}
