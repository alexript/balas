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

package net.autosauler.ballance.client.databases;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author alexript
 * 
 */
public class StructureFactory {
	private static final HashMap<String, Description> d = new HashMap<String, Description>();
	private static final HashMap<Integer, Set<String>> menuitems = new HashMap<Integer, Set<String>>();
	private static final HashMap<String, ImageResource> menuicons = new HashMap<String, ImageResource>();

	// 0 -- catalog
	// 1 -- document
	// 2 -- report
	private static final HashMap<String, Integer> types = new HashMap<String, Integer>();

	private static void addMenuItem(int role, String itemname) {
		UserRole ur = new UserRole(role);
		if (ur.isDocuments()) {
			menuitems.get(UserRole.ROLE_DOCUMENTS).add(itemname);
		}
		if (ur.isFinances()) {
			menuitems.get(UserRole.ROLE_FINANCES).add(itemname);
		}
		if (ur.isManager()) {
			menuitems.get(UserRole.ROLE_MANAGER).add(itemname);
		}

	}

	protected static void fillDescriptions(HashMap<String, Description> result) {
		d.clear();
		d.putAll(result);

		menuicons.clear();
		menuitems.clear();
		types.clear();

		menuitems.put(UserRole.ROLE_DOCUMENTS, new HashSet<String>());
		menuitems.put(UserRole.ROLE_FINANCES, new HashSet<String>());
		menuitems.put(UserRole.ROLE_MANAGER, new HashSet<String>());

		for (String name : result.keySet()) {

			if (name.equals("document.inpay")) {
				menuicons.put(name, Images.menu.icoIncPay());
			} else if (name.equals("document.ingoods")) {
				menuicons.put(name, Images.menu.Travel());
			} else if (name.equals("document.cargo")) {
				menuicons.put(name, Images.menu.icoInGoods());
			} else if (name.equals("catalog.tarifs")) {
				menuicons.put(name, Images.menu.icoTarif());
			} else if (name.equals("catalog.partners")) {
				menuicons.put(name, Images.menu.icoPartners());
			} else if (name.equals("catalog.cars")) {
				menuicons.put(name, Images.menu.icoCar());
			} else if (name.equals("catalog.drivers")) {
				menuicons.put(name, Images.menu.icoMan());
			} else if (name.equals("catalog.paymethod")) {
				menuicons.put(name, Images.menu.icoPaymethod());
			} else if (name.startsWith("document.")) {
				menuicons.put(name, Images.menu.Document());
			} else if (name.startsWith("catalog.")) {
				menuicons.put(name, Images.menu.Cube());
			} else if (name.startsWith("report.")) {
				menuicons.put(name, Images.menu.Poll());
			}

			if (name.startsWith("document.")) {
				// String itemname = name.replace("document.", "");
				Description descr = d.get(name);
				int role = descr.getRole();
				addMenuItem(role, name);
				types.put(name, 1);

			} else if (name.startsWith("catalog.")) {
				// String itemname = name.replace("catalog.", "");
				Description descr = d.get(name);
				int role = descr.getRole();
				addMenuItem(role, name);
				types.put(name, 0);
			} else if (name.startsWith("report.")) {
				// String itemname = name.replace("report.", "");
				Description descr = d.get(name);
				int role = descr.getRole();
				addMenuItem(role, name);
				types.put(name, 2);
			}
		}

	}

	public static Description getDescription(String name) {

		if (d.containsKey(name)) {
			// trace(d.get(name));
			return d.get(name);
		}
		return null;
	}

	public static ImageResource getMenuIcon(String item) {
		if (menuicons.containsKey(item)) {
			return menuicons.get(item);
		}
		Log.error("There are no icon for menu item " + item);
		return null;
	}

	public static Set<String> getMenuItemsFor(int role) {
		return menuitems.get(role);
	}

	public static int getPanelType(String name) {

		return types.get(name);
	}

	private static void loadAll() {
		MainPanel.setCommInfo(true);
		Services.structure
				.getAll(new AsyncCallback<HashMap<String, Description>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(HashMap<String, Description> result) {
						MainPanel.setCommInfo(false);
						if (result != null) {
							fillDescriptions(result);
							Ballance_autosauler_net.drawInterface();

						} else {
							new AlertDialog("Can't load structurs descriptions")
									.show();
						}

					}
				});
	}

	public static void loadData() {
		loadAll();

	}

	private static void trace(Description description) {

		Log.error(d.toString());
	}

	protected static void trace(HashMap<String, Description> d2) {
		for (String name : d2.keySet()) {
			Description d = d2.get(name);
			Log.error("Description of " + name);
			trace(d);
		}

	}
}
