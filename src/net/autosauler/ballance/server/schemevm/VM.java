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

package net.autosauler.ballance.server.schemevm;

import com.allen_sauer.gwt.log.client.Log;

/**
 * The Class VM.
 * 
 * @author alexript
 */
public class VM {

	/** The vm. */
	private static Sixx vm = null;

	/** The sixxlib. */
	private static Library sixxlib;

	/**
	 * Instantiates a new vM.
	 */
	public VM() {
		if (vm == null) {
			try {
				vm = new Sixx();
			} catch (Exception e) {
				Log.error(e.getMessage());
				vm = null;
			}
			initContext();
		}
	}

	/**
	 * Adds the mehod.
	 * 
	 * @param name
	 *            the name
	 * @param cls
	 *            the cls
	 * @param method
	 *            the method
	 * @param paramtypes
	 *            the paramtypes
	 */
	@SuppressWarnings("rawtypes")
	private void addMehod(String name, Class cls, String method,
			String[] paramtypes) {
		Log.trace("addMethod to vm: " + name);
		if (vm != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("(define ");
			sb.append(name);
			sb.append(" (method \"");
			sb.append(cls.getCanonicalName());
			sb.append("\" \"");
			sb.append(method);
			sb.append("\"");
			if ((paramtypes != null) && (paramtypes.length > 0)) {
				for (int i = 0; i < paramtypes.length; i++) {
					String type = paramtypes[i];
					sb.append(" \"");
					sb.append(type);
					sb.append("\"");
				}
			}
			sb.append("))");

			eval(sb.toString());
		} else {
			Log.error("VM is null");
		}
	}

	/**
	 * Eval.
	 * 
	 * @param str
	 *            the str
	 * @return the object
	 */
	public Object eval(String str) {

		Object obj = null;
		if (vm != null) {
			try {
				// obj = vm.eval(str, vm.r6rs);
				Log.trace("Try eval: " + str);
				Object form = (new Reader(new java.io.StringReader(
						"(begin (unspecified) " + str + ")"))).read();
				Log.trace("Form: " + form.toString());
				obj = vm.eval(form, sixxlib);
				if (obj != null) {
					Log.trace("Result: " + obj.toString());
				} else {
					Log.trace("Result: null.");
				}
				Log.trace(sixxlib.symbolTable.toString());
				Log.trace(sixxlib.slots.toString());

			} catch (Exception e) {
				Log.error("Eval exception: " + e.getMessage());
				Log.error("Code: " + str);
				Log.trace(e.getStackTrace().toString());
				obj = null;
			}
		}
		return obj;
	}

	/**
	 * Inits the context.
	 */
	private void initContext() {
		if (vm != null) {
			Log.trace("Init vm");
			sixxlib = vm.getLib("sixx");
			addMehod("log.error", Log.class, "error",
					new String[] { "java.lang.String" });
			addMehod("log.trace", Log.class, "trace",
					new String[] { "java.lang.String" });
			// eval("(log.error \"test\")");

		} else {
			Log.error("VM IS NULL. NOT INIT");
		}
	}

}
