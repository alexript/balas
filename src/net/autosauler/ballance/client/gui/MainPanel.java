package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The Class MainPanel.
 */
public class MainPanel extends Composite {
	
	/** The panel. */
	private HorizontalPanel panel = new HorizontalPanel();
	
	/** The left column panel. */
	private LeftPanel leftPanel = new LeftPanel();
	
	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		panel.add(leftPanel);
		
		initWidget(panel);
		this.setStyleName("mainPanel");
	}
}
