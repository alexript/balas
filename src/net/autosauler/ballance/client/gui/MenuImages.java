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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

/**
 * The Interface MenuImages.
 */
public interface MenuImages extends Tree.Resources {

	/**
	 * Admin panel.
	 * 
	 * @return the image resource
	 */
	ImageResource adminPanel();

	/**
	 * Documents panel.
	 * 
	 * @return the image resource
	 */
	ImageResource documentsPanel();

	/**
	 * Finances panel.
	 * 
	 * @return the image resource
	 */
	ImageResource financesPanel();

	/**
	 * Manager panel.
	 * 
	 * @return the image resource
	 */
	ImageResource managerPanel();

	/**
	 * For all panel.
	 * 
	 * @return the image resource
	 */
	ImageResource forAllPanel();

	/**
	 * Guest panel.
	 * 
	 * @return the image resource
	 */
	ImageResource guestPanel();

	/**
	 * Use noimage.png, which is a blank 1x1 image.
	 * 
	 * @return the image resource
	 */
	@Source("noimage.gif")
	ImageResource treeLeaf();
}