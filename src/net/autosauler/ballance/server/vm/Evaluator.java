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

import com.allen_sauer.gwt.log.client.Log;

/**
 * The Class Evaluator.
 * 
 * @author alexript
 */
public class Evaluator {

	/** The vm. */
	private final VM vm;

	/**
	 * Instantiates a new evaluator.
	 * 
	 * @param container
	 *            the container
	 */
	public Evaluator(VM container) {
		vm = container;
	}

	/**
	 * Eval.
	 * 
	 * @param somecode
	 *            the somecode
	 * @return the object
	 */
	public Object eval(String somecode) {
		Object result = null;
		try {
			result = vm.eval(somecode);
		} catch (ScriptException e) {
			Log.error(e.getMessage());
			result = null;
		}
		return result;
	}
}
