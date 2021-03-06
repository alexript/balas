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

package net.autosauler.ballance.server.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.model.DocumentTablePart;

/**
 * @author alexript
 * 
 */
public class DoctableWrapper {
	private final DocumentTablePart impl;
	private final String docname;

	public DoctableWrapper(final DocumentTablePart doctab, String docname) {
		impl = doctab;
		this.docname = docname;

	}

	public void addRecord(Hashtable<String, Object> values) {
		impl.addRecord(values);

	}

	public List<DocumentWrapper> findContainers(String fieldname,
			DocumentWrapper document) {
		List<DocumentWrapper> lst = new ArrayList<DocumentWrapper>();

		if ((docname != null) && !docname.isEmpty()) {
			Set<Long> ids = impl.findDocuments(fieldname,
					(Long) document.get("number"));
			for (Long docid : ids) {
				DocumentWrapper doc = new DocumentWrapper(impl.getDomain(),
						docname, docid);
				lst.add(doc);
			}
		}
		return lst;
	}

	public Array getRecords() {
		Set<HashMap<String, Object>> s = impl.getRecords();
		Array cajuarray = new Array(s);

		return cajuarray;
	}
}
