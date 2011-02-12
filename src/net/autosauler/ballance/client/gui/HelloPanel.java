package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class HelloPanel extends Composite {
	public HelloPanel() {
		
		HTML w = new HTML(BalasResources.INSTANCE.helloPane().getText());
		initWidget(w);
	}
}
