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

package net.autosauler.ballance.client.gui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * The Interface BalasResources.
 */
public interface BalasResources extends ClientBundle {

	/** The Constant INSTANCE. */
	public static final BalasResources INSTANCE = GWT
			.create(BalasResources.class);

	/**
	 * Hello pane.
	 * 
	 * @return the text resource
	 */
	@Source("hello.html")
	public TextResource helloPane();

	/**
	 * License pane.
	 * 
	 * @return the text resource
	 */
	@Source("license.html")
	public TextResource licensePane();
}
