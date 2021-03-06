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

import java.util.List;

import net.autosauler.ballance.client.gui.resources.BalasResources;

import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 * The Class HelloPanel.
 */
public class HelloPanel extends Composite implements IPaneWithMenu {

	/** The impl. */
	private static HelloPanel impl = null;

	/**
	 * Gets the.
	 * 
	 * @return the hello panel
	 */
	public static HelloPanel get() {
		if (impl == null) {
			impl = new HelloPanel();
		}
		return impl;
	}

	/**
	 * Instantiates a new hello panel.
	 */
	private HelloPanel() {

		HTML w = new HTML(BalasResources.INSTANCE.helloPane().getText());
		initWidget(w);
	}

	@Override
	public List<MenuItem> getHelpItems() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public com.extjs.gxt.ui.client.widget.menu.MenuBar getPaneMenu() {

		return null;
	}
}
