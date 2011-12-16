package net.autosauler.ballance.client.model;

import java.util.ArrayList;
import java.util.List;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.shared.User;
import net.autosauler.ballance.shared.UserList;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UsersModel extends BaseModelData {

	private static final long serialVersionUID = -6755204091778520551L;

	public static void load(boolean fromtrash, final ListStore<UsersModel> store) {
		final List<UsersModel> users = new ArrayList<UsersModel>();
		MainPanel.setCommInfo(true);

		if (fromtrash) {
			Services.users.getTrashedUsers(new AsyncCallback<UserList>() {

				@Override
				public void onFailure(Throwable caught) {
					MainPanel.setCommInfo(false);
					store.add(users);
					new AlertDialog(caught).show();
				}

				@Override
				public void onSuccess(UserList result) {
					MainPanel.setCommInfo(false);

					List<User> lst = result.getList();
					for (User user : lst) {
						users.add(new UsersModel(user));
					}
					store.add(users);

				}
			});
		} else {
			Services.users.getUsers(new AsyncCallback<UserList>() {

				@Override
				public void onFailure(Throwable caught) {
					MainPanel.setCommInfo(false);
					store.add(users);
					new AlertDialog(caught).show();
				}

				@Override
				public void onSuccess(UserList result) {
					MainPanel.setCommInfo(false);
					List<User> lst = result.getList();
					for (User user : lst) {
						users.add(new UsersModel(user));
					}
					store.add(users);

				}
			});
		}

	}

	public UsersModel() {

	}

	public UsersModel(User user) {
		set("id", user.getId());
		set("username", user.getUsername());
		set("roles", user.getUserrole());
		set("createdate", user.getCreatedate());
		set("active", user.isActive());
	}

}
