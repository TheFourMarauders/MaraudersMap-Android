package com.maraudersapp.android.drawer;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maraudersapp.android.R;
import com.maraudersapp.android.datamodel.UserInfo;
import com.maraudersapp.android.mapdrawing.PollingManager;
import com.maraudersapp.android.remote.RemoteCallback;
import com.maraudersapp.android.remote.ServerComm;
import com.maraudersapp.android.remote.ServerCommManager;
import com.maraudersapp.android.storage.SharedPrefsUserAccessor;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 10/27/2015.
 */
public class FriendsDrawerView extends DrawerView {

    private List<DrawerItem> drawerItems;

    public FriendsDrawerView(ServerComm remote, SharedPrefsUserAccessor storage,
                             final DrawerManager drawerManager, PollingManager pm, final Context ctx, Set<UserInfo> users) {
        super(remote, storage, drawerManager, pm, ctx);

        List<DrawerItem> items = new ArrayList<>();
        // TODO back arrow
        for (final UserInfo user : users) {
            final String username = user.getUsername();
            items.add(new DrawerItem(new PrimaryDrawerItem().withName(user.getFirstName() + " " + user.getLastName())) {
                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Specific friends clicked");
                    drawerManager.onBackPressed();
                    drawerManager.onBackPressed();
                    drawerManager.setBarHeader(user.getFirstName() + " " + user.getLastName() + "'s History");
                    pollingManager.changePoller(pollingManager.newFriendPoller(username, ctx));
                }
            });
        }
        drawerItems = items;
    }

    @Override
    public ArrayList<IDrawerItem> getAllItems() {
        return addItemsToList(drawerItems);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        drawerItems.get(position - 1).handleClick(view, drawerItem);
        return true;
    }
}