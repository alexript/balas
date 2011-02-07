package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LeftPanel extends Composite {
	private VerticalPanel panel = new VerticalPanel();
	private AuthPanel authPanel = new AuthPanel("Ballance login");
	
	
	public LeftPanel() {
		panel.add(authPanel);
	
		initWidget(panel);
		this.setStyleName("leftPanel");

	}
}
