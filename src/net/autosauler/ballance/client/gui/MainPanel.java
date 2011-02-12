package net.autosauler.ballance.client.gui;

import java.util.HashMap;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainPanel.
 */
public class MainPanel extends Composite implements ValueChangeHandler<String> {

	/** The panel. */
	private HorizontalPanel panel = new HorizontalPanel();

	/** The left column panel. */
	private LeftPanel leftPanel = new LeftPanel();
	private static DecoratedTabPanel mainpane = new DecoratedTabPanel();

	private static HashMap<String, Integer> tabsIndexes = new HashMap<String, Integer>();
	
	/** The comminfo. */
	private static CommInfoPanel comminfo = null;

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		panel.add(leftPanel);
		panel.setCellWidth(leftPanel, "244px");
		mainpane.setWidth("100%");
		mainpane.setHeight("100%");
		panel.add(mainpane);
		initWidget(panel);
		this.setStyleName("mainPanel");

		String initToken = History.getToken();
		if (initToken.length() == 0) {
			History.newItem("start");
		}
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();

	}

	/**
	 * Sets the comm info.
	 * 
	 * @param f
	 *            the new comm info
	 */
	public static void setCommInfo(boolean f) {
		if (f) {
			comminfo = new CommInfoPanel();
			comminfo.show();
		} else {
			comminfo.hide();
		}
	}
	
	public static void dropMainPane() {
		mainpane.clear();
		tabsIndexes.clear();
	}

	private Widget constructTabPane(String name) {
		Widget w = null;
		UserRole role = Ballance_autosauler_net.sessionId.getUserrole();

		if (name.equals("start")) {
			w = new HTML("Foo");
			mainpane.add(w, "foo");

		} else if (name.equals("dbpane") && role.isAdmin()) {
			w = new HTML("Database");
			mainpane.add(w, "db");

		} else if (name.equals("editusers") && role.isAdmin()) {
			w = new HTML("Edit users");
			mainpane.add(w, "users");
		} else {
			Window.alert("Error #404");
		}

		
		return w;
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String eventvalue = event.getValue();
		if (eventvalue != null && !eventvalue.isEmpty()) {
			eventvalue = eventvalue.trim().toLowerCase();
			int paneindex = -1;
			if(tabsIndexes.containsKey(eventvalue)) {
				paneindex = tabsIndexes.get(eventvalue);
			}

			if(paneindex <0 ) {
				Widget w = constructTabPane(eventvalue);
				if(w!=null) {
					paneindex = mainpane.getWidgetIndex(w);
					tabsIndexes.put(eventvalue, paneindex);
				}
			}
			mainpane.selectTab(paneindex);
		}

	}
}
