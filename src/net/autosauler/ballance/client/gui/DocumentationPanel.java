package net.autosauler.ballance.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.client.model.DocumentationModel;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;

public class DocumentationPanel extends ContentPanel implements IPaneWithMenu,
		IReloadMsgReceiver {

	private static DocumentationPanel impl = null;

	public static DocumentationPanel get() {
		if (impl == null) {
			impl = new DocumentationPanel();
		}
		return impl;
	}

	private Grid<DocumentationModel> grid;
	private ListStore<DocumentationModel> store;

	private GridSelectionModel<DocumentationModel> sm;

	/**
	 * Instantiates a new users panel.
	 */
	private DocumentationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);

		buildPane();

	}

	private void buildPane() {
		sm = new GridSelectionModel<DocumentationModel>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		sm.addSelectionChangedListener(new SelectionChangedListener<DocumentationModel>() {

			@Override
			public void selectionChanged(
					SelectionChangedEvent<DocumentationModel> se) {

				DocumentationModel record = se.getSelectedItem();

				if ((record != null) && (record.get("name") != null)) {
					new DocumentationEditor((String) record.get("name"),
							DocumentationPanel.this);
					sm.deselectAll();
				}

			}
		});

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("Documentation name");
		column.setWidth(150);
		column.setRowHeader(true);
		columns.add(column);

		ColumnModel cm = new ColumnModel(columns);

		store = new ListStore<DocumentationModel>();
		reloadList();

		grid = new Grid<DocumentationModel>(store, cm);
		grid.setSelectionModel(sm);
		grid.setAutoExpandColumn("name");
		grid.setBorders(true);

		add(grid);
	}

	@Override
	public List<MenuItem> getHelpItems() {
		return null;
	}

	@Override
	public MenuBar getPaneMenu() {
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu();

		menu.add(new MenuItem("Create new", new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				sm.deselectAll();
				new DocumentationEditor("", DocumentationPanel.this);

			}
		}));

		menu.add(new MenuItem(M.structure.menuReload(),
				new SelectionListener<MenuEvent>() { // reload users list

					@Override
					public void componentSelected(MenuEvent ce) {
						loadList();

					}
				}));

		menubar.add(new MenuBarItem(M.menu.itemDocumentation(), menu));

		return menubar;
	}

	protected void loadList() {
		store.removeAll();
		DocumentationModel.load(store);
		sm.deselectAll();
	}

	@Override
	public void reloadList() {
		loadList();

	}
}
