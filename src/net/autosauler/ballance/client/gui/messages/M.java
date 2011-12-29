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

package net.autosauler.ballance.client.gui.messages;

import com.google.gwt.core.client.GWT;

/**
 * The Class M.
 * 
 * @author alexript
 */
public class M {

	/** The Constant auth. */
	public static final AuthMessages auth = GWT.create(AuthMessages.class);

	/** The Constant catalog. */
	public static final CatalogMessages catalog = GWT
			.create(CatalogMessages.class);

	/** The Constant comm. */
	public static final CommMessages comm = GWT.create(CommMessages.class);

	/** The Constant database. */
	public static final DatabaseMessages database = GWT
			.create(DatabaseMessages.class);

	/** The Constant dialog. */
	public static final DialogMessages dialog = GWT
			.create(DialogMessages.class);

	/** The Constant document. */
	public static final DocumentMessages document = GWT
			.create(DocumentMessages.class);

	/** The Constant table. */
	public static final DocumentTableMessages table = GWT
			.create(DocumentTableMessages.class);

	/** The Constant menu. */
	public static final MenuMessages menu = GWT.create(MenuMessages.class);

	/** The Constant tools. */
	public static final ToolsMessages tools = GWT.create(ToolsMessages.class);

	/** The Constant users. */
	public static final UsersMessages users = GWT.create(UsersMessages.class);

	public static final StructureMessages structure = GWT
			.create(StructureMessages.class);
}
