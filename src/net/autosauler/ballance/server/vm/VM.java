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
	private String mydomain;
	private Catalogs catalogs;
	private CurrencyWrapper currency;
	private Constants constants;
	private Evaluator evaluator;
	private Documents documents;

	/**
	 * Instantiates a new vM.
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

	public Object call(String funcname, Object... args) throws ScriptException,
			NoSuchMethodException {
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
	 */
	public Object eval(String str) throws ScriptException {

		Object obj = null;
		if (vm != null) {

			obj = vm.eval("caju.syntax: CajuBasic\n" + str);

		}
		return obj;
	}

	/**
	 * Inits the context.
	 */
	private void initContext() {
		if (vm != null) {
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

			try {
				eval(sb.toString());
			} catch (ScriptException e) {
				Log.error(e.getMessage());
			}

		} else {
			Log.error("VM IS NULL. NOT INIT");
		}
	}

}
