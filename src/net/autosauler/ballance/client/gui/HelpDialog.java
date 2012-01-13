package net.autosauler.ballance.client.gui;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.LocaleInfo;

public class HelpDialog extends Window {
	/**
	 * Button constant that displays a single OK button.
	 */
	public static final String OK = "ok";

	/**
	 * Button constant that displays a single CLOSE button.
	 */
	public static final String CLOSE = "close";

	/**
	 * The OK button text (defaults to 'OK');
	 */
	public String okText = GXT.MESSAGES.messageBox_ok();

	private final HtmlContainer hc;

	/**
	 * The Close button text (defaults to 'Close').
	 */
	public String closeText = GXT.MESSAGES.messageBox_close();

	private boolean hideOnButtonClick = false;
	private String buttons;

	private final SelectionListener<ButtonEvent> l = new SelectionListener<ButtonEvent>() {
		@Override
		public void componentSelected(ButtonEvent ce) {
			onButtonPressed(ce.getButton());
		}
	};

	public HelpDialog(String title) {
		setButtons(OK);
		setHeading("Help: " + title);
		setSize(500, 400);
		setHideOnButtonClick(true);
		setModal(true);
		setResizable(true);
		setBlinkModal(true);

		hc = new HtmlContainer();
		this.add(hc);
		setScrollMode(Scroll.AUTO);
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

	public void loadHelpText(String url) {
		MainPanel.setCommInfo(true);
		try {
			new RequestBuilder(RequestBuilder.GET, url).sendRequest("",
					new RequestCallback() {
						@Override
						public void onError(Request res, Throwable throwable) {
							MainPanel.setCommInfo(false);
							Log.error(throwable.getMessage());
						}

						@Override
						public void onResponseReceived(Request request,
								Response response) {
							MainPanel.setCommInfo(false);
							String text = response.getText();
							setHelpText(text);

						}
					});
		} catch (RequestException e) {
			MainPanel.setCommInfo(false);
			Log.error(e.getMessage());
		}

	}

	public void loadHelpText(String folderurl, String filename) {
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		String url = folderurl + "/" + locale + "/" + filename;
		MainPanel.setCommInfo(true);
		try {
			new RequestBuilder(RequestBuilder.GET, url).sendRequest("",
					new RequestCallback() {
						@Override
						public void onError(Request res, Throwable throwable) {
							MainPanel.setCommInfo(false);
							Log.error(throwable.getMessage());
						}

						@Override
						public void onResponseReceived(Request request,
								Response response) {
							MainPanel.setCommInfo(false);
							String text = response.getText();
							setHelpText(text);

						}
					});
		} catch (RequestException e) {
			MainPanel.setCommInfo(false);
			Log.error(e.getMessage());
		}

	}

	public void loadStructureHelp(String structname) {
		// TODO Auto-generated method stub
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();

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
				|| hideOnButtonClick) {
			hide(button);
		} else if (button == getButtonBar().getItemByItemId(OK)) {
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

	public void setHelpText(String htmltext) {
		hc.setHtml(htmltext);
		show();
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
