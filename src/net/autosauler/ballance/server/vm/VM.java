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

import javax.script.ScriptException;

import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.cajuscript.CajuScriptEngine;

import com.allen_sauer.gwt.log.client.Log;

/**
 * The Class VM.
 * 
 * @author alexript
 */
public class VM {

	/** The vm. */
	private CajuScriptEngine vm = null;

	/** The mydomain. */
	private String mydomain;

	/** The catalogs. */
	private Catalogs catalogs;

	/** The currency. */
	private CurrencyWrapper currency;

	/** The constants. */
	private Constants constants;

	/** The evaluator. */
	private Evaluator evaluator;

	/** The documents. */
	private Documents documents;

	/**
	 * Instantiates a new vM.
	 * 
	 * @param domain
	 *            the domain
	 */
	public VM(String domain) {
		if (vm == null) {
			try {
				mydomain = domain;
				catalogs = new Catalogs(mydomain);
				currency = new CurrencyWrapper();
				constants = new Constants(mydomain);
				evaluator = new Evaluator(this);
				documents = new Documents(mydomain);

				vm = new CajuScriptEngine();
			} catch (Exception e) {
				Log.error(e.getMessage());
				vm = null;
			}
			initContext();
		}
	}

	/**
	 * Call.
	 * 
	 * @param funcname
	 *            the funcname
	 * @param args
	 *            the args
	 * @return the object
	 * @throws ScriptException
	 *             the script exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	public Object call(String funcname, final Object... args)
			throws ScriptException, NoSuchMethodException {
		Object obj = null;
		if (vm != null) {

			obj = vm.invokeFunction(funcname, args);

		}
		return obj;
	}

	/**
	 * Eval.
	 * 
	 * @param str
	 *            the str
	 * @return the object
	 * @throws ScriptException
	 *             the script exception
	 */
	public Object eval(String str) throws ScriptException {

		Object obj = null;
		if (vm != null) {
			Log.trace(str);
			obj = vm.eval("caju.syntax: CajuBasic\n" + str);

		}
		return obj;
	}

	/**
	 * Inits the context.
	 */
	private void initContext() {
		if (vm != null) {
			vm.put("DT_OBJECT", new Integer(DataTypes.DT_OBJECT));
			vm.put("DT_INT", new Integer(DataTypes.DT_INT));
			vm.put("DT_DOUBLE", new Integer(DataTypes.DT_DOUBLE));
			vm.put("DT_STRING", new Integer(DataTypes.DT_STRING));
			vm.put("DT_DATE", new Integer(DataTypes.DT_DATE));
			vm.put("DT_LONG", new Integer(DataTypes.DT_LONG));
			vm.put("DT_CURRENCY", new Integer(DataTypes.DT_CURRENCY));
			vm.put("DT_CATALOG", new Integer(DataTypes.DT_CATALOG));
			vm.put("DT_CATALOGRECORD", new Integer(DataTypes.DT_CATALOGRECORD));
			vm.put("DT_DOCUMENT", new Integer(DataTypes.DT_DOCUMENT));
			vm.put("DT_DOCUMENTRECORD",
					new Integer(DataTypes.DT_DOCUMENTRECORD));
			vm.put("DT_SETTING", new Integer(DataTypes.DT_SETTING));
			vm.put("DT_SETTINGVALUE", new Integer(DataTypes.DT_SETTINGVALUE));
			vm.put("DT_DOMAIN", new Integer(DataTypes.DT_DOMAIN));
			vm.put("DT_BOOLEAN", new Integer(DataTypes.DT_BOOLEAN));
			vm.put("DT_MONEY", new Integer(DataTypes.DT_MONEY));
			vm.put("DT_SCRIPT", new Integer(DataTypes.DT_SCRIPT));

			vm.put("Script", evaluator);
			vm.put("Constants", constants);
			vm.put("Currency", currency);
			vm.put("Catalogs", catalogs);
			vm.put("Documents", documents);

			StringBuilder sb = new StringBuilder();
			sb.append("import java.lang\n");
			sb.append("import java.util.Date\n");
			sb.append("import com.allen_sauer.gwt.log.client.Log\n");
			sb.append("import net.autosauler.ballance.server.vm.Evaluator\n");
			sb.append("import net.autosauler.ballance.server.vm.Constants\n");
			sb.append("import net.autosauler.ballance.server.vm.CatalogWrapper\n");
			sb.append("import net.autosauler.ballance.server.vm.CurrencyWrapper\n");
			sb.append("import net.autosauler.ballance.server.vm.Documents\n");
			sb.append("import net.autosauler.ballance.server.vm.DocumentWrapper\n");
			sb.append("import net.autosauler.ballance.server.vm.DoctableWrapper\n");
			sb.append("import net.autosauler.ballance.server.vm.ReportForm\n");

			try {
				eval(sb.toString());
			} catch (ScriptException e) {
				Log.error(e.getMessage());
			}

		} else {
			Log.error("VM IS NULL. NOT INIT");
		}
	}

	/**
	 * Put object.
	 * 
	 * @param name
	 *            the name
	 * @param obj
	 *            the obj
	 */
	public void putObject(String name, Object obj) {
		if (vm != null) {
			vm.put(name, obj);
		}
	}

}
