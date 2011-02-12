package net.autosauler.ballance.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface BalasResources extends ClientBundle {
	public static final BalasResources INSTANCE = GWT.create(BalasResources.class);
	
	@Source("hello.html")
	public TextResource helloPane();
}
