package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * The Class CommInfoPanel.
 */
public class CommInfoPanel extends PopupPanel {
	
	/** The l. */
	private CommMessages l = null;
	
	/**
	 * Instantiates a new comm info panel.
	 */
	public CommInfoPanel() {
		super(false);
		l = GWT.create(CommMessages.class);
		Label msg = new Label(l.commInProgress());
		setWidget(msg);
		this.setPopupPosition(Ballance_autosauler_net.mainpanel.getOffsetWidth() - this.getOffsetWidth()-200, Ballance_autosauler_net.mainpanel.getOffsetHeight() - this.getOffsetHeight()-200);
	}
}
