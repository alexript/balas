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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author alexript
 * 
 */
public class ToolsCell extends AbstractCell<String> {
	private static ToolsMessages l = null;
	private static MenuImages images = null;
	private static Image imgEdit;
	private static Image imgTrash;
	private static HorizontalPanel panel;

	/**
	 * Construct a new ImageCell.
	 */
	public ToolsCell() {

		if (l == null) {
			l = GWT.create(ToolsMessages.class);
		}

		if (images == null) {
			// TODO: events not work. Fuc*ing GWT: only cell-level events...
			// FIXED: remove tools from celltable row and put them under
			// celltable, + selection checkboxes...
			images = GWT.create(MenuImages.class);

			imgEdit = new Image(images.Write());
			imgEdit.setTitle(l.btnEdit());
			imgEdit.setAltText(l.btnEdit());
			imgEdit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit");

				}
			});

			imgTrash = new Image(images.Trash());
			imgTrash.setTitle(l.btnDelete());
			imgTrash.setAltText(l.btnDelete());
			imgTrash.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Trash");

				}
			});
			panel = new HorizontalPanel();
			panel.setSpacing(5);

			panel.add(imgEdit);
			panel.add(imgTrash);

		}
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		if (value != null) {

			sb.appendHtmlConstant(panel.toString());
		}
	}
}
