package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MainPanel extends Composite {
	private HorizontalPanel panel = new HorizontalPanel();
	private LeftPanel leftPanel = new LeftPanel();
	
	public MainPanel() {
		panel.add(leftPanel);
		
		initWidget(panel);
		this.setStyleName("mainPanel");
	}
}
