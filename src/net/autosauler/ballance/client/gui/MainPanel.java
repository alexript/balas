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
	
	/** The comminfo. */
	private static CommInfoPanel comminfo = null;
	
	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		panel.add(leftPanel);
		
		initWidget(panel);
		this.setStyleName("mainPanel");
	}
	
	/**
	 * Sets the comm info.
	 *
	 * @param f the new comm info
	 */
	public static void setCommInfo(boolean f) {
		if(f) {
			comminfo = new CommInfoPanel();
			comminfo.show();
		} else {
			comminfo.hide();
		}
	}
}
