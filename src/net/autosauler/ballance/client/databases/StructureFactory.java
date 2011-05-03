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

package net.autosauler.ballance.client.databases;

import java.util.HashMap;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.shared.Description;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author alexript
 * 
 */
public class StructureFactory {
	private static final HashMap<String, Description> d = new HashMap<String, Description>();

	public static Description getDescription(String name) {
		if (d.containsKey(name)) {
			return d.get(name);
		}
		return null;
	}

	private static void load(final String name) {
		MainPanel.setCommInfo(true);
		Services.structure.getStructureDescription(name,
				new AsyncCallback<Description>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(Description result) {
						MainPanel.setCommInfo(false);
						if (result != null) {
							d.put(name, result);
						} else {
							new AlertDialog(
									"Can't load structure description for "
											+ name).show();
						}
					}
				});
	}

	public static void loadData() {
		load("catalog.paymethod");
		load("catalog.tarifs");
		load("catalog.partners");
		load("table.goods");
		load("table.goodsaddpay");
	}
}
