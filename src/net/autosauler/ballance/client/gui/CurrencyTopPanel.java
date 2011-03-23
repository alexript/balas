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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.client.DatabaseService;
import net.autosauler.ballance.client.DatabaseServiceAsync;
import net.autosauler.ballance.client.databases.CurrencyValuesStorage;
import net.autosauler.ballance.client.databases.ICurrencyValuesReceiver;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class CurrencyTopPanel.
 * 
 * @author alexript
 */
public class CurrencyTopPanel extends Composite implements
		ICurrencyValuesReceiver {

	/** The currencypanel. */
	private final HorizontalPanel currencypanel;

	/** The currvalues. */
	private final HorizontalPanel currvalues;

	/** The progress image. */
	private final Image progress;

	/** The reload button icon. */
	private final Image reload;

	private final DatabaseServiceAsync service = GWT
			.create(DatabaseService.class);

	private static String showcurrency = "EUR,USD";

	private static final String settingname = "currency.toppanel.list";

	/**
	 * Instantiates a new currency top panel.
	 */
	public CurrencyTopPanel() {
		MenuImages images = GWT.create(MenuImages.class);

		reload = new Image(images.reload());
		reload.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				effectFade(currvalues.getElement());
				startCurrencyReload();

			}
		});

		currencypanel = new HorizontalPanel();
		currvalues = new HorizontalPanel();

		currvalues.setSpacing(3);
		currvalues.setVisible(false);
		progress = new Image(images.progress());

		currencypanel.add(currvalues);
		currencypanel.add(progress);
		currencypanel.setWidth("200px");
		initWidget(currencypanel);
		startCurrencyReload();

	}

	/**
	 * Creates the currency value label.
	 * 
	 * @param value
	 *            the value
	 * @return the label
	 */
	private Label createCurrencyValueLabel(Double value) {
		Label label = new Label(value.toString());
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.databases.ICurrencyValuesReceiver#
	 * doCurrencyValue(java.lang.String, java.util.Date, java.lang.Double)
	 */
	@Override
	public void doCurrencyValue(String mnemo, Date date, Double value) {

		currvalues.clear();
		currvalues.add(new Label(mnemo + ":"));
		currvalues.add(createCurrencyValueLabel(value));
		currvalues.add(reload);
		effectFade(progress.getElement());
		effectAppear(currvalues.getElement());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.databases.ICurrencyValuesReceiver#
	 * doCurrencyValues(java.util.Date, java.util.HashMap)
	 */
	@Override
	public void doCurrencyValues(Date date, HashMap<String, Double> values) {

		currvalues.clear();
		Set<String> keys = values.keySet();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			String mnemo = i.next();
			currvalues.add(new Label(mnemo + ":"));
			currvalues.add(createCurrencyValueLabel(values.get(mnemo)));
		}
		currvalues.add(reload);
		effectFade(progress.getElement());
		effectAppear(currvalues.getElement());

	}

	/**
	 * Effect appear.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectAppear(Element element) /*-{
		new $wnd.Effect.Appear(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Effect fade.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectFade(Element element) /*-{
		new $wnd.Effect.Fade(element, {

			queue : 'end'
		});
	}-*/;

	/**
	 * Start currency reload.
	 */
	private void startCurrencyReload() {

		effectAppear(progress.getElement());
		currvalues.clear();

		CurrencyValuesStorage.clean();

		String[] values = showcurrency.split(",");

		Set<String> set = new HashSet<String>();

		if (values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				set.add(values[i]);
			}
		} else {
			set.add("EUR");
			set.add("USD");
		}
		CurrencyValuesStorage.get(this, set);
		service.getSettings(new AsyncCallback<HashMap<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// ignore communication error

			}

			@Override
			public void onSuccess(HashMap<String, String> result) {
				if (result.containsKey(settingname)) {
					showcurrency = result.get(settingname);
				}
			}
		});
	}
}
