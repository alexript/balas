package net.autosauler.ballance.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.DataTypeFactory;
import net.autosauler.ballance.client.gui.MainPanel;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentModel extends BaseModelData {

	private static final long serialVersionUID = 6199977210462780219L;

	public static void load(final ListStore<DocumentModel> store,
			final String documentname, boolean withunactive) {
		final List<DocumentModel> records = new ArrayList<DocumentModel>();
		MainPanel.setCommInfo(true);
		Services.documents.getAll(documentname, withunactive,
				new AsyncCallback<Set<Long>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(Set<Long> result) {
						Services.documents
								.get(documentname,
										result,
										new AsyncCallback<Set<HashMap<String, Object>>>() {

											@Override
											public void onFailure(
													Throwable caught) {
												MainPanel.setCommInfo(false);
												new AlertDialog(caught).show();
											}

											@Override
											public void onSuccess(
													Set<HashMap<String, Object>> result) {

												MainPanel.setCommInfo(false);
												for (HashMap<String, Object> document : result) {
													records.add(new DocumentModel(
															document));
												}
												store.add(records);
											}

										});
					}
				});

	}

	public DocumentModel(HashMap<String, Object> document) {
		Set<String> keys = document.keySet();
		for (String key : keys) {
			set(key, document.get(key));
		}

		set("documentnamevalue",
				((Long) get("number")).toString()
						+ " ["
						+ DataTypeFactory.formatter.format(new Date(
								(Long) get("createdate"))) + "]");
	}

}
