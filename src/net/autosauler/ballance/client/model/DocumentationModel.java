package net.autosauler.ballance.client.model;

import java.util.List;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentationModel extends BaseModelData {

	private static final long serialVersionUID = 6552232560625267L;

	public static void load(final ListStore<DocumentationModel> store) {

		MainPanel.setCommInfo(true);

		Services.structure.getHelpNames(new AsyncCallback<List<String>>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure
			 * (java.lang .Throwable)
			 */
			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught).show();

			}

			@Override
			public void onSuccess(List<String> result) {
				MainPanel.setCommInfo(false);

				for (String name : result) {
					store.add(new DocumentationModel(name));
				}

			}
		});

	}

	public DocumentationModel(String name) {
		set("name", name);
	}

}
