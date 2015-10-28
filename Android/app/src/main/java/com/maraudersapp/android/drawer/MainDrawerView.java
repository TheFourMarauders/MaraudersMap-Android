package com.maraudersapp.android.drawer;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.maraudersapp.android.LoginActivity;
import com.maraudersapp.android.R;
import com.maraudersapp.android.datamodel.GroupInfo;
import com.maraudersapp.android.datamodel.UserInfo;
import com.maraudersapp.android.remote.RemoteCallback;
import com.maraudersapp.android.remote.ServerComm;
import com.maraudersapp.android.storage.SharedPrefsUserAccessor;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Michael on 10/27/2015.
 */
public class MainDrawerView extends DrawerView {

    private final DrawerItem[] drawerItems = {
            // Yourself
            new DrawerItem(new PrimaryDrawerItem().withName(R.string.yourself_item_name)
                    .withIdentifier(1).withSelectable(false)) {

                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Yourself clicked");
                }

            },

            // Friends
            new DrawerItem(new PrimaryDrawerItem().withName(R.string.friends_item_name)
                    .withIdentifier(2).withSelectable(false)) {

                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Friends clicked");
                    remote.getFriendsFor(
                            storage.getUsername(),
                            new RemoteCallback<Set<UserInfo>>() {
                                @Override
                                public void onSuccess(Set<UserInfo> response) {
                                    Log.i(DRAWER_TAG, "Friend response received");
                                    DrawerView newView = new FriendsDrawerView(remote, storage,
                                            drawerManager, response);
                                    Log.i(DRAWER_TAG, response.toString());
                                    drawerManager.switchView(newView);
                                }

                                @Override
                                public void onFailure(int errorCode, String message) {
                                    // TODO error view
                                }
                            }
                    );
                }

            },

            // Groups
            new DrawerItem(new PrimaryDrawerItem().withName(R.string.groups_item_name)
                    .withIdentifier(3).withSelectable(false)) {

                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Groups clicked");
                    remote.getGroupsFor(
                            storage.getUsername(),
                            new RemoteCallback<Set<GroupInfo>>() {
                                @Override
                                public void onSuccess(Set<GroupInfo> response) {
                                    DrawerView newView = new GroupsDrawerView(remote, storage,
                                            drawerManager, response);
                                    drawerManager.switchView(newView);
                                }

                                @Override
                                public void onFailure(int errorCode, String message) {
                                    // TODO error view
                                }
                            }
                    );
                }

            },

            // Divider
            new DrawerItem(new DividerDrawerItem().withEnabled(false)) {
                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {}
            },

            // Incognito
            new DrawerItem(new ToggleDrawerItem().withName(R.string.incognito_item_name)
                    .withIdentifier(4).withSelectable(false)) {

                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Incognito clicked");
                }

            },

            // Settings
            new DrawerItem(new PrimaryDrawerItem().withName(R.string.settings_item_name)
                    .withIdentifier(5).withSelectable(false)) {

                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Settings clicked");
                }

            },

            // Logout
            new DrawerItem(new PrimaryDrawerItem().withName(R.string.logout_item_name)
                    .withIdentifier(6).withSelectable(false)) {

                @Override
                public void handleClick(View view, IDrawerItem drawerItem) {
                    Log.i(DRAWER_TAG, "Logout clicked");
                    Intent i = new Intent(view.getContext(), LoginActivity.class);
                    i.putExtra("nullify", true);
                    view.getContext().startActivity(i);
                }

            },
    };

    public MainDrawerView(ServerComm remote, SharedPrefsUserAccessor storage, DrawerManager drawerManager) {
        super(remote, storage, drawerManager);
    }

    @Override
    public ArrayList<IDrawerItem> getAllItems() {
        return addItemsToList(Arrays.asList(drawerItems));
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        drawerItems[position - 1].handleClick(view, drawerItem);
        return true;
    }

}