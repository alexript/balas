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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextArea;

/**
 * The Class ScriptArea.
 * 
 * @author alexript
 */
public class ScriptArea extends TextArea {

	/** The editormode. */
	private final String editormode;
	private boolean codemirrormode;

	/**
	 * Instantiates a new script area.
	 * 
	 * @param mode
	 *            the mode
	 */
	public ScriptArea(String mode) {
		super();
		super.setText("");
		codemirrormode = false;
		if (mode == null) {
			editormode = "lua";
		} else {
			editormode = mode;
		}
	}

	/**
	 * Gets the editor value.
	 * 
	 * @return the editor value
	 */
	private native String getEditorValue() /*-{
		return codemirror.getValue();
	}-*/;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.ValueBoxBase#getText()
	 */
	@Override
	public String getText() {
		// Attention: after getText() there are no more codemirror!!!
		String s = "";
		if (codemirrormode) {
			s = getEditorValue();
			returnTextArea();
			codemirrormode = false;
		} else {
			s = super.getText();
		}
		return s;
	}

	/**
	 * Inits the code mirror.
	 * 
	 * @param element
	 *            the element
	 * @param edmode
	 *            the edmode
	 */
	// TODO: syntax highlight: http://codemirror.net/manual.html
	private native void initCodeMirror(Element element, String edmode) /*-{
		codemirror = new $wnd.CodeMirror.fromTextArea(element, {
			mode : edmode,
			lineNumbers : true,
			matchBrackets : true
		});
	}-*/;

	/**
	 * Return text area.
	 */
	private native void returnTextArea() /*-{
		codemirror.toTextArea();
	}-*/;

	/**
	 * Sets the cM text.
	 * 
	 * @param text
	 *            the new cM text
	 */
	private native void setCMText(String text) /*-{
		codemirror.setValue(text);
		// TODO: fix empty editor window on setText (it is wisible only on state change)
		codemirror.refresh();
		codemirror.setCursor(0, 0);
	}-*/;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.ValueBoxBase#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		if (!codemirrormode) {
			super.setText(text);

			initCodeMirror(getElement(), editormode);
			codemirrormode = true;
		} else {
			setCMText(text);
		}
	}
}
