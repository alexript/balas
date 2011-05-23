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

import javax.script.ScriptEngine;
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
	private ScriptEngine vm = null;

	/**
	 * Instantiates a new vM.
	 */
	public VM() {
		if (vm == null) {
			try {
				vm = new CajuScriptEngine();
			} catch (Exception e) {
				Log.error(e.getMessage());
				vm = null;
			}
			initContext();
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
				obj = vm.eval(str);
			} catch (ScriptException e) {
				Log.error(e.getMessage());
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
			String script = "$java.lang\n";
			script += "$com.allen_sauer.gwt.log.client.Log";
			eval(script);

		} else {
			Log.error("VM IS NULL. NOT INIT");
		}
	}

}
