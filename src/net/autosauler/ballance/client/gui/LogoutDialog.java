package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LogoutDialog extends DialogBox {
	public LogoutDialog() {
		setText("Do You want to logout?");
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
		Button yes = new Button("Yes");
		yes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LeftPanel.authPanel.logoffAction();
				LogoutDialog.this.hide();
			}
			
		});
		
		Button no = new Button("No");
		no.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LogoutDialog.this.hide();
				
			}
			
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(yes);
		buttons.add(no);
		
		setWidget(buttons);
		setPopupPosition((Ballance_autosauler_net.mainpanel.getOffsetWidth()/2-this.getOffsetWidth()), 300);
	}
}
