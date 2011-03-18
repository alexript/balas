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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

/**
 * The Interface RolesImages.
 * 
 * @author alexript
 */
public interface RolesImages extends Tree.Resources {

	/**
	 * Checks if is admin.
	 * 
	 * @return the image resource
	 */
	ImageResource isAdmin();

	/**
	 * Checks if is documents.
	 * 
	 * @return the image resource
	 */
	ImageResource isDocuments();

	/**
	 * Checks if is finances.
	 * 
	 * @return the image resource
	 */
	ImageResource isFinances();

	/**
	 * Checks if is guest.
	 * 
	 * @return the image resource
	 */
	ImageResource isGuest();

	/**
	 * Checks if is manager.
	 * 
	 * @return the image resource
	 */
	ImageResource isManager();
}
