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

package net.autosauler.ballance.client.gui.images;

import com.google.gwt.core.client.GWT;

/**
 * The Class Images.
 * 
 * @author alexript
 */
public class Images {
	/** The images. */
	public static final MenuImages menu = GWT.create(MenuImages.class);

	/** The Constant roles. */
	public static final RolesImages roles = GWT.create(RolesImages.class);

	/** The Constant table. */
	public static final TablePartImages table = GWT
			.create(TablePartImages.class);
}
