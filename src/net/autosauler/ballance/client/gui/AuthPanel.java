package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class AuthPanel.
 */
public class AuthPanel extends Composite implements ClickHandler {

	/** The auth panel. */
	private VerticalPanel authPanel = new VerticalPanel();
	
	/** The login text. */
	private TextBox loginText = null;
	
	/** The password text. */
	private PasswordTextBox passwordText = null;
	
	/** The ok button. */
	private Button okButton = null;
	
	/** The cancel button. */
	private Button cancelButton = null;
	
	/** The formname. */
	private String formname = null;
	
	/**
	 * Instantiates a new auth panel.
	 *
	 * @param title the title
	 */
	public AuthPanel(String title) {
		formname = title;
		authPanel.setWidth("200px");
		authPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		if(Ballance_autosauler_net.isLoggedIn()) {
			constructHelloPane();
		} else {
			constructAuthForm();
		}
		
		initWidget(authPanel);
		this.setStyleName("authPanel");

	}
	
	/**
	 * Logoff action.
	 */
	public void logoffAction() {
		constructAuthForm();
	}
	
	/**
	 * Login action.
	 */
	public void loginAction() {
		constructHelloPane();
	}
	
	/**
	 * Construct hello pane.
	 */
	private void constructHelloPane() {
		authPanel.clear();
		loginText = null;
		passwordText = null;
		okButton = null;
		cancelButton = null;
		Label helloLabel = new Label();
		helloLabel.setText("Hello, User!!!");
		authPanel.add(helloLabel);
	}
	
	/**
	 * Construct auth form.
	 */
	private void constructAuthForm() {
		authPanel.clear();
		
		if(formname!=null && !formname.isEmpty()) {
			Label title = new Label();
			title.setText(formname);
			title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			authPanel.add(title);
		}
		
		// login line
		HorizontalPanel loginPanel = new HorizontalPanel();
		loginPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		loginPanel.setSpacing(3);
		
		Label loginLabel = new Label();
		loginLabel.setText("Login:");
		loginLabel.setWidth("70px");
		loginPanel.add(loginLabel);
		
		loginText = new TextBox();
		loginText.setText("");
		loginPanel.add(loginText);
		authPanel.add(loginPanel);
		
		// password line
		HorizontalPanel passwordPanel = new HorizontalPanel();
		passwordPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		passwordPanel.setSpacing(3);
		
		Label passwordLabel = new Label();
		passwordLabel.setText("Password:");
		passwordLabel.setWidth("70px");
		passwordPanel.add(passwordLabel);
		
		passwordText = new PasswordTextBox();
		passwordText.setText("");
		passwordPanel.add(passwordText);
		
		authPanel.add(passwordPanel);
		
		// buttons line
		
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setSpacing(3);
		
		okButton = new Button();
		okButton.setText("Ok");
		okButton.addClickHandler(this);
		buttonsPanel.add(okButton);
		
		cancelButton = new Button();
		cancelButton.setText("Cancel");
		cancelButton.addClickHandler(this);
		buttonsPanel.add(cancelButton);
		
		authPanel.add(buttonsPanel);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		if(event.getSource().equals(okButton)) { // let's auth
			
		} else if (event.getSource().equals(cancelButton)) { // clean form
			
			if(passwordText!=null) {
				passwordText.setText("");
			}
			if(loginText!=null) {
				loginText.setText("");
				loginText.setFocus(true);
			}
		}

	}

}
