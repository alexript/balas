package net.autosauler.ballance.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.client.model.StructureModel;

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

public class StructurePanel extends ContentPanel implements IPaneWithMenu,
		IReloadMsgReceiver {
	private static StructurePanel impl = null;

	public static StructurePanel get() {
		if (impl == null) {
			impl = new StructurePanel();
		}
		return impl;
	}

	private Grid<StructureModel> grid;
	private ListStore<StructureModel> store;

	private GridSelectionModel<StructureModel> sm;

	/**
	 * Instantiates a new users panel.
	 */
	private StructurePanel() {
		super(new FitLayout());
		setHeaderVisible(false);

		buildPane();

	}

	private void buildPane() {
		sm = new GridSelectionModel<StructureModel>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		sm.addSelectionChangedListener(new SelectionChangedListener<StructureModel>() {

			@Override
			public void selectionChanged(
					SelectionChangedEvent<StructureModel> se) {

				StructureModel record = se.getSelectedItem();

				if ((record != null) && (record.get("name") != null)) {
					new StructureEditor((String) record.get("name"),
							StructurePanel.this);
					sm.deselectAll();
				}

			}
		});

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("Structure name");
		column.setWidth(150);
		column.setRowHeader(true);
		columns.add(column);

		ColumnModel cm = new ColumnModel(columns);

		store = new ListStore<StructureModel>();
		reloadList();

		grid = new Grid<StructureModel>(store, cm);
		grid.setSelectionModel(sm);
		grid.setAutoExpandColumn("name");
		grid.setBorders(true);

		add(grid);
	}

	@Override
	public MenuBar getPaneMenu() {
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu();

		menu.add(new MenuItem(M.structure.menuAddStruct(),
				new SelectionListener<MenuEvent>() { // create new user

					@Override
					public void componentSelected(MenuEvent ce) {
						sm.deselectAll();
						new StructureEditor("", StructurePanel.this);

					}
				}));

		menu.add(new MenuItem(M.structure.menuReload(),
				new SelectionListener<MenuEvent>() { // reload users list

					@Override
					public void componentSelected(MenuEvent ce) {
						loadList();

					}
				}));

		menubar.add(new MenuBarItem(M.menu.itemStructure(), menu));
		return menubar;
	}

	protected void loadList() {
		store.removeAll();
		StructureModel.load(store);
		sm.deselectAll();
	}

	@Override
	public void reloadList() {
		loadList();

	}

}
