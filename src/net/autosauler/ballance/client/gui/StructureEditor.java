package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Services;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class StructureEditor extends Window {

	/** The receiver. */
	private final IReloadMsgReceiver receiver;

	/** The scriptname. */
	private String structurename;

	/** The text. */
	private String text;

	private TextField<String> name;

	/** The editor. */
	private ScriptArea editor;

	/**
	 * Instantiates a new script editor.
	 * 
	 * @param structurename
	 *            the scriptname
	 * @param reloadreceiver
	 *            the reloadreceiver
	 */
	public StructureEditor(final String structurename,
			IReloadMsgReceiver reloadreceiver) {
		super();
		receiver = reloadreceiver;
		this.structurename = structurename;

		initGui();
		MainPanel.setCommInfo(true);
		Services.structure.get(structurename, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught).show();

			}

			@Override
			public void onSuccess(String result) {
				MainPanel.setCommInfo(false);
				if (result != null) {
					name.setValue(structurename);
					text = result;
					editor.setText(text);
					StructureEditor.this.show();
				} else {
					new AlertDialog("Can't load script " + structurename)
							.show();
				}

			}
		});
	};

	/**
	 * Inits the gui.
	 */
	private void initGui() {
		setHeading("Structure editor");
		setAnimCollapse(true);
		setModal(true);
		setBlinkModal(true);
		setLayout(new FitLayout());
		setSize(658, 419);
		setResizable(false);

		setClosable(false);

		FormPanel form = new FormPanel();
		FormData formData = new FormData("98%");
		form.setLabelWidth(150);
		form.setHeaderVisible(false);

		name = new TextField<String>();
		name.setFieldLabel("Structure name");
		name.setEmptyText("Enter structure name");
		editor = new ScriptArea("xml");

		Button btnSave = new Button("Save");

		btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

				structurename = name.getValue();
				if ((structurename != null) && !structurename.isEmpty()) {
					MainPanel.setCommInfo(true);
					text = editor.getText();
					Services.structure.save(structurename, text,
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									MainPanel.setCommInfo(false);
									new AlertDialog(caught).show();

								}

								@Override
								public void onSuccess(Void v) {
									MainPanel.setCommInfo(false);

									StructureEditor.this.hide();
									if (receiver != null) {
										receiver.reloadList();
									}

								}
							});
				} else {
					new AlertDialog("Empty structure name").show();
				}
			}

		});

		Button btnCancel = new Button("Cancel");

		btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				StructureEditor.this.hide();
				if (receiver != null) {
					receiver.reloadList();
				}
			}

		});

		form.add(name, formData);
		form.add(editor, formData);
		add(form);
		addButton(btnSave);
		addButton(btnCancel);
		show();
	}
}
