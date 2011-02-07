package net.autosauler.ballance.client.gui;

import java.util.Date;

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The Class LogoutDialog.
 */
public class LogoutDialog extends DialogBox {
	
	/** The localizator. */
	private AuthMessages l = null;
	
	private final static long ONE_HOUR = 1000 * 60 * 60; 
	
	/**
	 * Instantiates a new logout dialog.
	 */
	public LogoutDialog() {
		l = GWT.create(AuthMessages.class);
		
		setText(l.qtnLogout());
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
		Button yes = new Button(l.btnYes());
		
		yes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainPanel.setCommInfo(true);
				Ballance_autosauler_net.authService.logoff(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						LogoutDialog.this.hide();
					}

					@Override
					public void onSuccess(Void result) {
						Ballance_autosauler_net.setLoggedInState(false);
						Cookies.setCookie("session", "", new Date(System.currentTimeMillis() + ONE_HOUR));
						Ballance_autosauler_net.sessionId.setSessionId("");

						LeftPanel.authPanel.logoffAction();
						MainPanel.setCommInfo(false);
						LogoutDialog.this.hide();
					}
				});
				
			}
			
		});
		
		Button no = new Button(l.btnNo());
		no.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LogoutDialog.this.hide();
				
			}
			
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(yes);
		buttons.add(no);
		buttons.setSpacing(5);
		
		setWidget(buttons);
		
		no.setFocus(true);
		
		setPopupPosition((Ballance_autosauler_net.mainpanel.getOffsetWidth()/2-this.getOffsetWidth()), 300);
	}
}
