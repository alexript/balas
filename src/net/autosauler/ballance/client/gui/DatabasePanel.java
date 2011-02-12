/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class DatabasePanel.
 */
public class DatabasePanel extends Composite implements ClickHandler, IDialogYesReceiver {
	
	/** The root. */
	private VerticalPanel root = null;
	
	/** The l. */
	private DatabaseMessages l;
	
	/** The btn drop database. */
	private Button btnDropDatabase;
	
	/**
	 * Instantiates a new database panel.
	 */
	public DatabasePanel() {
		l = GWT.create(DatabaseMessages.class);
		root = new VerticalPanel();
		root.add(createDropDatabasePanel());
		initWidget(root);
	}
	
	/**
	 * Creates the drop database panel.
	 *
	 * @return the decorator panel
	 */
	private DecoratorPanel createDropDatabasePanel() {
		HorizontalPanel p = new HorizontalPanel();
		p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		p.setSpacing(6);
		p.add(new Label(l.msgDropDatabaseTitle()));
		
		btnDropDatabase = new Button(l.btnExecute());
		btnDropDatabase.addClickHandler(this);
		p.add(btnDropDatabase);
		DecoratorPanel panel = new DecoratorPanel ();
		panel.setWidget(p);
		return panel;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		if(event.getSource().equals(btnDropDatabase)) {
			new QuestionDialog(l.qstDropDatabase(), this, "dropdb").show();
		}
		
	}

	/* (non-Javadoc)
	 * @see net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick(java.lang.String)
	 */
	@Override
	public void onDialogYesButtonClick(String tag) {
		if(tag.equals("dropdb")) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if(role.isAdmin()) {
				MainPanel.setCommInfo(true);
				
				MainPanel.setCommInfo(false);
			}
		}
		
	}
}
