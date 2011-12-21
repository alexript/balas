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
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Services;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class CurrencySelector.
 * 
 * @author alexript
 */
public class CurrencySelector extends SimpleComboBox<String> {

	private static List<String> names = null;

	/**
	 * Instantiates a new currency selector.
	 * 
	 * @param currentcurrency
	 *            the currentcurrency
	 */
	public CurrencySelector(String currentcurrency) {
		super();
		setEmptyText("Select a currency...");

		setTypeAhead(true);
		setTriggerAction(TriggerAction.ALL);
		// Image reload = new Image(Images.menu.reload());
		//
		// reload.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// String oldvalue = getValue();
		// reloadBox(oldvalue);
		//
		// }
		// });

		reloadBox(currentcurrency);

	}

	/**
	 * Adds the change handler.
	 * 
	 * @param handler
	 *            the handler
	 */
	public void addChangeHandler(Listener<FieldEvent> handler) {
		addListener(Events.Change, handler);
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */

	public String getStrValue() {
		String val = getValue().getValue();
		return val;
	}

	/**
	 * Reload box.
	 * 
	 * @param selectedcurrency
	 *            the selectedcurrency
	 */
	private void reloadBox(final String selectedcurrency) {
		removeAll();
		if (names == null) {

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
								names = new ArrayList<String>(result);
								add(names);
								select(selectedcurrency);
							}
							MainPanel.setCommInfo(false);

						}
					});
		} else {
			add(names);
		}

	}

	@Override
	public void reset() {
		super.reset();
		select(0);
	}

	/**
	 * Select.
	 * 
	 * @param currency
	 *            the currency
	 */
	public void select(String currency) {

		int total = getStore().getCount();
		for (int i = 0; i < total; i++) {

			SimpleComboValue<String> val = getStore().getAt(i);

			if (val.get("value").equals(currency)) {

				setValue(val);
				return;
			}
		}
		select(0);
	}
}
