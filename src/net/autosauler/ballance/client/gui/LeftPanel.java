package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class LeftPanel.
 */
public class LeftPanel extends Composite {

	/** The panel. */
	private VerticalPanel panel = new VerticalPanel();

	/** The auth form panel. */
	public static AuthPanel authPanel = null;

	private LeftMenu menu = null;
	
	/**
	 * Instantiates a new left panel.
	 */
	public LeftPanel() {
		if (authPanel == null) {
			authPanel = new AuthPanel("BalAS");
			panel.add(authPanel);
			panel.setCellHeight(authPanel, "130px");
		}
		
		menu = new LeftMenu();
		authPanel.setMenu(menu);
		panel.add(menu);
		panel.setCellHorizontalAlignment(menu, HasHorizontalAlignment.ALIGN_CENTER);

		
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
		this.setStyleName("leftPanel");

	}
}
