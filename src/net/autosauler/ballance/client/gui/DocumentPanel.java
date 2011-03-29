/*******************************************************************************
 * Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.client.DocumentServiceAsync;
import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DocumentPanel.
 * 
 * @author alexript
 */
public abstract class DocumentPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The documentname. */
	private final String documentname;

	/** The service. */
	private static DocumentServiceAsync service = null;

	/** The tabimage. */
	private final Image tabimage;

	/** The l. */
	private static DocumentMessages l = null;

	/** The progress. */
	private static Image progress = null;

	/** The images. */
	protected static MenuImages images = GWT.create(MenuImages.class);

	/** The Constant DATEFORMATTER. */
	private static final String DATEFORMATTER = "yyyy/MM/dd HH:mm";

	/** The formatter. */
	private static SimpleDateFormat formatter = null;

	/** The root. */
	private AbsolutePanel root;

	/**
	 * Instantiates a new document panel.
	 * 
	 * @param docname
	 *            the docname
	 * @param image
	 *            the image
	 */
	public DocumentPanel(String docname, Image image) {
		documentname = docname;
		tabimage = image;
		if (service == null) {
			service = GWT.create(DocumentService.class);
			l = GWT.create(DocumentMessages.class);
			progress = new Image(images.progress());
			formatter = new SimpleDateFormat(DATEFORMATTER);
		}
	}

	/**
	 * Can activate.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canActivate(UserRole role);

	/**
	 * Can create.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canCreate(UserRole role);

	/**
	 * Can edit.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canEdit(UserRole role);

	abstract void cleanEditForm();

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		// TODO:
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {
		// TODO:
	}

	/**
	 * Effect hide.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectHide(Element element) /*-{
		new $wnd.Effect.DropOut(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Effect show.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectShow(Element element) /*-{
		new $wnd.Effect.SlideDown(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Gets the list form.
	 * 
	 * @return the list form
	 */
	public DocumentPanel getListForm() {
		root = new AbsolutePanel();
		root.setWidth("100%");
		root.setHeight("500px");

		createListForm();

		if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())
				|| canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			createEditorForm();
		}
		initWidget(root);
		reloadList();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public Widget getPaneMenu() {
		MenuBar menu = new MenuBar();

		menu.addItem(l.menuReload(), new Command() { // reload users list
					@Override
					public void execute() {
						reloadList();
					}
				});

		if (canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			menu.addItem(l.menuCreate(), new Command() { // reload users list
						@Override
						public void execute() {
							cleanEditForm();
							openEditor();
						}
					});
		}
		return menu;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void onDialogYesButtonClick(String tag, Object tag2) {
		// TODO Auto-generated method stub

	}

	private void openEditor() {
		// TODO:
	}

	/**
	 * Reload list.
	 */
	private void reloadList() {
		// TODO:
	}

}
