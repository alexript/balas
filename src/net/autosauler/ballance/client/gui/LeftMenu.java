package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LeftMenu extends Composite {
	private DecoratedStackPanel panel;
	private UserRole role = null;
	private MenuMessages l;
	private MenuImages images;

	public LeftMenu() {
		l = GWT.create(MenuMessages.class);
		images = (MenuImages) GWT.create(MenuImages.class);
		panel = new DecoratedStackPanel();
		panel.setWidth("244px");

		buildContent();
		initWidget(panel);
	}

	public void clear() {
		panel.clear();
		role = null;
	}

	public void buildContent() {
		clear();
		role = Ballance_autosauler_net.sessionId.getUserrole();
		buildAdminPane();
		buildDocumentsPane();
		buildFinancesPane();
		buildManagerPane();
		buildForAllPane();
		buildGuestPane();
	}

	private String getHeaderString(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(0);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.add(new Image(image));
		HTML headerText = new HTML(text);
		headerText.setStyleName("cw-StackPanelHeader");
		hPanel.add(headerText);

		// Return the HTML string for the panel
		return hPanel.getElement().getString();
	}

	private void buildAdminPane() {
		if (role.isAdmin()) {
			VerticalPanel adminpanel = new VerticalPanel();

			Label dbmanage = new Label(l.itemDatabase());
			adminpanel.add(dbmanage);

			Label usersmanage = new Label(l.itemUsers());
			adminpanel.add(usersmanage);

			String adminHeader = getHeaderString(l.adminPanel(),
					images.adminPanel());

			panel.add(adminpanel, adminHeader, true);

		}
	}

	private void buildDocumentsPane() {
		if (role.isAdmin() || role.isDocuments()) {
			VerticalPanel documentspanel = new VerticalPanel();

			String documentsHeader = getHeaderString(l.documentsPanel(),
					images.documentsPanel());
			panel.add(documentspanel, documentsHeader, true);
		}
	}

	private void buildFinancesPane() {
		if (role.isAdmin() || role.isFinances()) {
			VerticalPanel financesspanel = new VerticalPanel();

			String financesHeader = getHeaderString(l.financesPanel(),
					images.financesPanel());
			panel.add(financesspanel, financesHeader, true);
		}
	}

	private void buildManagerPane() {
		if (role.isAdmin() || role.isManager()) {
			VerticalPanel managerpanel = new VerticalPanel();


			String managerHeader = getHeaderString(l.managerPanel(),
					images.managerPanel());
			panel.add(managerpanel, managerHeader, true);
		}
	}

	private void buildForAllPane() {
		if (!role.isGuest()) {
			VerticalPanel allpanel = new VerticalPanel();

			String forAllHeader = getHeaderString(l.forAllPanel(),
					images.forAllPanel());
			panel.add(allpanel, forAllHeader, true);
		}
	}

	private void buildGuestPane() {

		VerticalPanel guestpanel = new VerticalPanel();

		String guestHeader = getHeaderString(l.guestPanel(),
				images.guestPanel());
		panel.add(guestpanel, guestHeader, true);

	}
}
