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
	 * Cancel.
	 * 
	 * @return the image resource
	 */
	ImageResource Cancel();

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
	 * Ico close.
	 * 
	 * @return the image resource
	 */
	ImageResource icoClose();

	/**
	 * Ico copyright.
	 * 
	 * @return the image resource
	 */
	ImageResource icoCopyright();

	/**
	 * Ico database.
	 * 
	 * @return the image resource
	 */
	ImageResource icoDatabase();

	/**
	 * Ico exclamation.
	 * 
	 * @return the image resource
	 */
	ImageResource icoExclamation();

	/**
	 * Ico inc pay.
	 * 
	 * @return the image resource
	 */
	ImageResource icoIncPay();

	/**
	 * Ico info.
	 * 
	 * @return the image resource
	 */
	ImageResource icoInfo();

	/**
	 * Ico partners.
	 * 
	 * @return the image resource
	 */
	ImageResource icoPartners();

	/**
	 * Ico paymethod.
	 * 
	 * @return the image resource
	 */
	ImageResource icoPaymethod();

	/**
	 * Ico question.
	 * 
	 * @return the image resource
	 */
	ImageResource icoQuestion();

	/**
	 * Ico refresh.
	 * 
	 * @return the image resource
	 */
	ImageResource icoRefresh();

	/**
	 * Ico user.
	 * 
	 * @return the image resource
	 */
	ImageResource icoUser();

	/**
	 * Manager panel.
	 * 
	 * @return the image resource
	 */
	ImageResource managerPanel();

	/**
	 * Ok.
	 * 
	 * @return the image resource
	 */
	ImageResource Ok();

	/**
	 * Progress.
	 * 
	 * @return the image resource
	 */
	ImageResource progress();

	/**
	 * Reload.
	 * 
	 * @return the image resource
	 */
	ImageResource reload();

	/**
	 * Spinner.
	 * 
	 * @return the image resource
	 */
	ImageResource spinner();

	/**
	 * Trash.
	 * 
	 * @return the image resource
	 */
	ImageResource Trash();

	/**
	 * Use noimage.png, which is a blank 1x1 image.
	 * 
	 * @return the image resource
	 */
	@Override
	@Source("noimage.gif")
	ImageResource treeLeaf();

	/**
	 * Write.
	 * 
	 * @return the image resource
	 */
	ImageResource Write();
}
