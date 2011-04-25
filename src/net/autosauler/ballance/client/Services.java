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

package net.autosauler.ballance.client;

import com.google.gwt.core.client.GWT;

/**
 * The Class Services.
 * 
 * @author alexript
 */
public class Services {

	/** The Constant auth. */
	public static final AuthServiceAsync auth = GWT.create(AuthService.class);

	/** The Constant catalogs. */
	public static final CatalogServiceAsync catalogs = GWT
			.create(CatalogService.class);

	/** The Constant currency. */
	public static final CurrencyServiceAsync currency = GWT
			.create(CurrencyService.class);

	/** The Constant database. */
	public static final DatabaseServiceAsync database = GWT
			.create(DatabaseService.class);

	/** The Constant documents. */
	public static final DocumentServiceAsync documents = GWT
			.create(DocumentService.class);

	/** The Constant users. */
	public static final UsersServiceAsync users = GWT
			.create(UsersService.class);

}
