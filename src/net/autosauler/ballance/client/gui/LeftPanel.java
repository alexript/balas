package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class LeftPanel.
 */
public class LeftPanel extends Composite {
	
	/** The panel. */
	private VerticalPanel panel = new VerticalPanel();
	
	/** The auth form panel. */
	public static AuthPanel authPanel = new AuthPanel("Ballance login");
	
	
	/**
	 * Instantiates a new left panel.
	 */
	public LeftPanel() {
		panel.add(authPanel);
	
		initWidget(panel);
		this.setStyleName("leftPanel");

	}
}
