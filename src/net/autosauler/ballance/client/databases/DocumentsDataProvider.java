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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.client.DocumentServiceAsync;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * The Class DocumentsDataProvider.
 * 
 * @author alexript
 */
public class DocumentsDataProvider extends
		AsyncDataProvider<HashMap<String, Object>> {

	/** The docname. */
	private final String docname;

	/** The numbers. */
	private Long[] numbers = null;

	/** The service. */
	private static DocumentServiceAsync service = GWT
			.create(DocumentService.class);

	/**
	 * Instantiates a new documents data provider.
	 * 
	 * @param docname
	 *            the docname
	 */
	public DocumentsDataProvider(String docname) {
		super();
		this.docname = docname;
		reload();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData)
	 */
	@Override
	protected void onRangeChanged(HasData<HashMap<String, Object>> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		int length = range.getLength();

		if ((numbers == null) || (numbers.length < 1) || (length == 0)) {
			Log.trace("Empty documents numbers array. Range:[from " + start
					+ ", size " + length + "]");
			updateRowCount(0, true);
			return;
		}
		Set<Long> receivenumbers = new HashSet<Long>();

		for (int i = start; i < start + length; i++) {
			receivenumbers.add(numbers[i]);
		}

		MainPanel.setCommInfo(true);
		service.get(docname, receivenumbers,
				new AsyncCallback<Set<HashMap<String, Object>>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught.getMessage()).show();
					}

					@Override
					public void onSuccess(Set<HashMap<String, Object>> result) {
						List<HashMap<String, Object>> newData = new ArrayList<HashMap<String, Object>>(
								result);
						MainPanel.setCommInfo(false);
						updateRowData(start, newData);
					}

				});

	}

	/**
	 * Reload.
	 */
	public void reload() {
		MainPanel.setCommInfo(true);
		service.getAll(docname, new AsyncCallback<Set<Long>>() {

			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Set<Long> result) {
				numbers = result.toArray(new Long[result.size()]);
				MainPanel.setCommInfo(false);
				DocumentsDataProvider.this.updateRowCount(numbers.length, true);
			}
		});
	}
}
