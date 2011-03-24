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

import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.client.CatalogService;
import net.autosauler.ballance.client.CatalogServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PartnersPanel.
 * 
 * @author alexript
 */
public class PartnersPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The impl. */
	private static PartnersPanel impl = null;

	/** The service. */
	private static CatalogServiceAsync service;

	/** The images. */
	private static MenuImages images;

	/** The l. */
	private static CatalogMessages l;

	/** The progress. */
	private static Image progress;

	/**
	 * Gets the.
	 * 
	 * @return the partners panel
	 */
	public static PartnersPanel get() {
		if (impl == null) {
			service = GWT.create(CatalogService.class);
			images = GWT.create(MenuImages.class);
			l = GWT.create(CatalogMessages.class);
			progress = new Image(images.progress());
			impl = new PartnersPanel();
		}
		return impl;
	}

	/** The root. */
	private final AbsolutePanel root;

	/** The list. */
	private final VerticalPanel list;

	/** The editor. */
	private final VerticalPanel editor;

	/**
	 * Instantiates a new users panel.
	 */
	private PartnersPanel() {

		root = new AbsolutePanel();
		root.setWidth("100%");
		root.setHeight("500px");

		list = new VerticalPanel();
		editor = new VerticalPanel();
		editor.setVisible(false);
		editor.add(new Label("Editor"));
		HorizontalPanel p = new HorizontalPanel();
		p.add(new Label("Name"));
		p.add(new TextBox());
		editor.add(p);
		Button b = new Button("Save");
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reloadList();

			}
		});
		editor.add(b);

		root.add(list, 0, 0);
		root.add(editor, 0, 0);

		initWidget(root);

		reloadList();
	}

	/**
	 * Creates the new partner.
	 */
	private void createNewPartner() {
		effectHide(list.getElement());
		effectShow(editor.getElement());
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

		menu.addItem(l.menuCreate(), new Command() { // reload users list
					@Override
					public void execute() {
						createNewPartner();
					}
				});

		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String)
	 */
	@Override
	public void onDialogYesButtonClick(String tag) {
		// TODO Auto-generated method stub

	}

	/**
	 * Reload list.
	 */
	private void reloadList() {
		list.clear();
		list.add(progress);

		MainPanel.setCommInfo(true);
		service.getAllPartners(new AsyncCallback<Set<Long>>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang
			 * .Throwable)
			 */
			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught.getMessage()).show();

			}

			@Override
			public void onSuccess(Set<Long> result) {
				list.clear();
				list.add(new Label("Partners list"));
				MainPanel.setCommInfo(false);
				Iterator<Long> i = result.iterator();
				while (i.hasNext()) {
					Long number = i.next();
					list.add(new Label(number.toString()));
				}
				if (!list.isVisible() && editor.isVisible()) {
					effectHide(editor.getElement());
					effectShow(list.getElement());
				}
			}
		});
	}

}
