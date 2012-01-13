package net.autosauler.ballance.client.gui;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;

public class FileUploadDialog extends Window {
	/**
	 * Button constant that displays a single OK button.
	 */
	public static final String OK = "ok";

	/**
	 * Button constant that displays a single CANCEL button.
	 */
	public static final String CANCEL = "cancel";

	/**
	 * Button constant that displays a single CLOSE button.
	 */
	public static final String CLOSE = "close";

	/**
	 * Button constant that displays a OK and CANCEL button.
	 */
	public static final String OKCANCEL = "okcancel";
	/**
	 * The OK button text (defaults to 'OK');
	 */
	public String okText = GXT.MESSAGES.messageBox_ok();

	/**
	 * The Close button text (defaults to 'Close').
	 */
	public String closeText = GXT.MESSAGES.messageBox_close();

	/**
	 * The Cancel button text (defaults to 'Cancel').
	 */
	public String cancelText = GXT.MESSAGES.messageBox_cancel();

	private boolean hideOnButtonClick = false;
	private String buttons;
	private final FormPanel panel;
	private final FileUploadField file;

	private final SelectionListener<ButtonEvent> l = new SelectionListener<ButtonEvent>() {
		@Override
		public void componentSelected(ButtonEvent ce) {
			onButtonPressed(ce.getButton());
		}
	};

	public FileUploadDialog(String url, String cmd) {
		setButtons(OKCANCEL);
		setHeading("File upload");
		setSize(330, 110);
		setHideOnButtonClick(false);
		setModal(true);
		setResizable(false);
		setBlinkModal(true);

		panel = new FormPanel();
		panel.setHeaderVisible(false);
		panel.setAction(url);
		panel.setEncoding(Encoding.MULTIPART);
		panel.setMethod(Method.POST);
		panel.setFrame(false);

		HiddenField<String> f = new HiddenField<String>();
		f.setName("cmd");
		f.setValue(cmd);
		panel.add(f);

		file = new FileUploadField();
		file.setAllowBlank(false);
		file.setName("f");
		file.setFieldLabel("File");
		panel.add(file);
		this.add(panel);
	}

	/**
	 * Creates the buttons based on button creation constant
	 */
	protected void createButtons() {
		getButtonBar().removeAll();
		setFocusWidget(null);
		if (buttons.indexOf(OK) != -1) {
			Button okBtn = new Button(okText);
			okBtn.setItemId(OK);
			okBtn.addSelectionListener(l);
			setFocusWidget(okBtn);
			addButton(okBtn);
		}

		if (buttons.indexOf(CANCEL) != -1) {
			Button cancelBtn = new Button(cancelText);
			cancelBtn.setItemId(CANCEL);
			cancelBtn.addSelectionListener(l);
			setFocusWidget(cancelBtn);
			addButton(cancelBtn);
		}

		if (buttons.indexOf(CLOSE) != -1) {
			Button closeBtn = new Button(closeText);
			closeBtn.setItemId(CLOSE);
			closeBtn.addSelectionListener(l);
			addButton(closeBtn);
		}

	}

	public Button getButtonById(String string) {
		return (Button) fbar.getItemByItemId(string);
	}

	/**
	 * Returns the button's.
	 * 
	 * @return the buttons the buttons
	 */
	public String getButtons() {
		return buttons;
	}

	/**
	 * Returns true if the dialog will be hidden on any button click.
	 * 
	 * @return the hide on button click state
	 */
	public boolean isHideOnButtonClick() {
		return hideOnButtonClick;
	}

	/**
	 * Called after a button in the button bar is selected. If
	 * {@link #setHideOnButtonClick(boolean)} is true, hides the dialog when any
	 * button is pressed.
	 * 
	 * @param button
	 *            the button
	 */
	protected void onButtonPressed(Button button) {

		if ((button == getButtonBar().getItemByItemId(CLOSE))
				|| (button == getButtonBar().getItemByItemId(CANCEL))
				|| hideOnButtonClick) {
			hide(button);
		} else if (button == getButtonBar().getItemByItemId(OK)) {

			if (!panel.isValid()) {

				return;
			}

			panel.submit();

			hide(button);
		}
	}

	/**
	 * Sets the buttons to display (defaults to OK). Must be one of:
	 * 
	 * <pre>
	 * FileUploadDialog.OK
	 * FileUploadDialog.CANCEL
	 * FileUploadDialog.OKCANCEL
	 * </pre>
	 */
	public void setButtons(String buttons) {
		this.buttons = buttons;
		createButtons();
	}

	/**
	 * True to hide the dialog on any button click.
	 * 
	 * @param hideOnButtonClick
	 *            true to hide
	 */
	public void setHideOnButtonClick(boolean hideOnButtonClick) {
		this.hideOnButtonClick = hideOnButtonClick;
	}
}
