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

package net.autosauler.ballance.shared.datatypes;

import java.util.Date;

/**
 * The Class DataTypes.
 * 
 * @author alexript
 */
public class DataTypes {

	/** The Constant DT_OBJECT. */
	final public static int DT_OBJECT = 0;

	/** The Constant DT_INT. */
	final public static int DT_INT = 1;

	/** The Constant DT_DOUBLE. */
	final public static int DT_DOUBLE = 2;

	/** The Constant DT_STRING. */
	final public static int DT_STRING = 3;

	/** The Constant DT_DATE. */
	final public static int DT_DATE = 4;

	/** The Constant DT_LONG. */
	final public static int DT_LONG = 5;

	/** The Constant DT_CURRENCY. */
	final public static int DT_CURRENCY = 6;

	/** The Constant DT_CATALOG. */
	final public static int DT_CATALOG = 7;

	/** The Constant DT_CATALOGRECORD. */
	final public static int DT_CATALOGRECORD = 8;

	/** The Constant DT_DOCUMENT. */
	final public static int DT_DOCUMENT = 9;

	/** The Constant DT_DOCUMENTRECORD. */
	final public static int DT_DOCUMENTRECORD = 10;

	/** The Constant DT_SETTING. */
	final public static int DT_SETTING = 11;

	/** The Constant DT_SETTINGVALUE. */
	final public static int DT_SETTINGVALUE = 12;

	/** The Constant DT_DOMAIN. */
	final public static int DT_DOMAIN = 13;

	/** The Constant DT_BOOLEAN. */
	final public static int DT_BOOLEAN = 14;

	/** The Constant DT_MONEY. */
	final public static int DT_MONEY = 15;

	/** The Constant DT_SCRIPT. */
	final public static int DT_SCRIPT = 16;

	/**
	 * From mapping.
	 * 
	 * @param type
	 *            the type
	 * @param val
	 *            the val
	 * @return the object
	 */
	public static Object fromMapping(int type, Object val) {
		Object obj = null;
		if (type == DataTypes.DT_DATE) {
			obj = new Date((Long) val);
		} else if (type == DataTypes.DT_DOUBLE) {
			obj = Double.parseDouble((String) val);
		} else if (type == DataTypes.DT_MONEY) {
			obj = Double.parseDouble((String) val);
		} else if (type == DataTypes.DT_INT) {
			obj = Integer.parseInt((String) val);
		} else {
			obj = val;
		}
		return obj;
	}

	/**
	 * From string.
	 * 
	 * @param type
	 *            the type
	 * @param val
	 *            the val
	 * @return the object
	 */
	public static Object fromString(int type, String val) {
		Object obj = null;
		if (type == DataTypes.DT_DATE) {
			if ((val == null) || val.isEmpty()) {
				obj = new Date();
			} else {
				obj = new Date(Long.parseLong(val));
			}
		} else if (type == DataTypes.DT_DOUBLE) {
			if ((val == null) || val.isEmpty()) {
				obj = new Double(0.0D);
			} else {
				obj = Double.parseDouble(val);
			}
		} else if (type == DataTypes.DT_MONEY) {
			if ((val == null) || val.isEmpty()) {
				obj = new Double(0.0D);
			} else {
				obj = Double.parseDouble(val);
			}
		} else if (type == DataTypes.DT_INT) {
			if ((val == null) || val.isEmpty()) {
				obj = new Integer(0);
			} else {
				obj = Integer.parseInt(val);
			}
		} else if ((type == DataTypes.DT_LONG)
				|| (type == DataTypes.DT_CATALOGRECORD)
				|| (type == DataTypes.DT_DOCUMENTRECORD)) {
			if ((val == null) || val.isEmpty()) {
				obj = new Long(0L);
			} else {
				obj = Long.parseLong(val);
			}
		} else if (type == DataTypes.DT_BOOLEAN) {
			if ((val == null) || val.isEmpty()) {
				obj = new Boolean(true);
			} else {
				obj = Boolean.parseBoolean(val);
			}
		} else {
			obj = val;
		}
		return obj;
	}

	/**
	 * From string.
	 * 
	 * @param name
	 *            the name
	 * @return the int
	 */
	public static int fromString(String name) {
		int type = 0;

		if (name.equals("DT_OBJECT")) {
			type = DT_OBJECT;
		} else if (name.equals("DT_INT")) {
			type = DT_INT;
		} else if (name.equals("DT_DOUBLE")) {
			type = DT_DOUBLE;
		} else if (name.equals("DT_STRING")) {
			type = DT_STRING;
		} else if (name.equals("DT_DATE")) {
			type = DT_DATE;
		} else if (name.equals("DT_LONG")) {
			type = DT_LONG;
		} else if (name.equals("DT_CURRENCY")) {
			type = DT_CURRENCY;
		} else if (name.equals("DT_CATALOG")) {
			type = DT_CATALOG;
		} else if (name.equals("DT_CATALOGRECORD")) {
			type = DT_CATALOGRECORD;
		} else if (name.equals("DT_DOCUMENT")) {
			type = DT_DOCUMENT;
		} else if (name.equals("DT_DOCUMENTRECORD")) {
			type = DT_DOCUMENTRECORD;
		} else if (name.equals("DT_SETTING")) {
			type = DT_SETTING;
		} else if (name.equals("DT_SETTINGVALUE")) {
			type = DT_SETTINGVALUE;
		} else if (name.equals("DT_DOMAIN")) {
			type = DT_DOMAIN;
		} else if (name.equals("DT_BOOLEAN")) {
			type = DT_BOOLEAN;
		} else if (name.equals("DT_MONEY")) {
			type = DT_MONEY;
		} else if (name.equals("DT_SCRIPT")) {
			type = DT_SCRIPT;
		}

		return type;
	}

	/**
	 * To mapping.
	 * 
	 * @param type
	 *            the type
	 * @param object
	 *            the object
	 * @return the object
	 */
	public static Object toMapping(int type, Object object) {
		Object obj = null;
		if (type == DataTypes.DT_DATE) {
			obj = ((Date) object).getTime();
		} else if (type == DataTypes.DT_DOUBLE) {
			obj = ((Double) object).toString();
		} else if (type == DataTypes.DT_MONEY) {
			obj = ((Double) object).toString();
		} else if (type == DataTypes.DT_INT) {
			obj = ((Integer) object).toString();
		} else {
			obj = object;
		}
		return obj;

	}

	/**
	 * To string.
	 * 
	 * @param type
	 *            the type
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static String toString(int type, Object object) {
		String obj = null;
		if (object == null) {
			return "";
		}
		if (type == DataTypes.DT_DATE) {
			obj = Long.toString(((Date) object).getTime());
		} else if (type == DataTypes.DT_DOUBLE) {
			obj = ((Double) object).toString();
		} else if (type == DataTypes.DT_MONEY) {
			obj = ((Double) object).toString();
		} else if (type == DataTypes.DT_INT) {
			obj = ((Integer) object).toString();
		} else if ((type == DataTypes.DT_LONG)
				|| (type == DataTypes.DT_CATALOGRECORD)
				|| (type == DataTypes.DT_DOCUMENTRECORD)) {
			obj = ((Long) object).toString();
		} else if (type == DataTypes.DT_BOOLEAN) {
			obj = ((Boolean) object).toString();

		} else {
			obj = object.toString();
		}
		return obj;

	}
}
