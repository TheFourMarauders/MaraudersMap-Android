package com.maraudersapp.android.mapdrawing;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maraudersapp.android.datamodel.LocationInfo;
import com.maraudersapp.android.remote.RemoteCallback;
import com.maraudersapp.android.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 10/29/2015.
 */
public class GroupPoller extends Poller {

    private final String groupId;
    private List<Marker> currentMarkers;

    GroupPoller(Handler handler, GoogleMap googleMap, Context ctx, String groupId) {
        super(handler, googleMap, ctx);
        this.groupId = groupId;
        currentMarkers = new ArrayList<>();
    }

    @Override
    public void run() {
        remote.getGroupLocations(groupId,
                new Date(TimeUtil.getCurrentTimeInMillis() - PollingManager.LOCATION_SPAN),
                new Date(TimeUtil.getCurrentTimeInMillis()),
                new RemoteCallback<Map<String, List<LocationInfo>>>() {
                    @Override
                    public void onSuccess(Map<String, List<LocationInfo>> response) {
                        if (!response.isEmpty()) {
                            removeAllMarkings();
                            float hue = response.size() != 1 ? 359.0f / response.size() : DEFAULT_HUE;
                            float hStep = hue;
                            for (Map.Entry<String, List<LocationInfo>> user : response.entrySet()) {
                                float opacity = 1.0f / response.size();
                                float oStep = opacity;
                                for (LocationInfo locInfo : user.getValue()) {
                                    currentMarkers.add(gMap.addMarker(new MarkerOptions().position(
                                            new LatLng(locInfo.getLatitude(), locInfo.getLongitude()))
                                            .alpha(opacity).icon(BitmapDescriptorFactory.defaultMarker(hue))));
                                    opacity += oStep;
                                }
                                hue += hStep;
                            }
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {

                    }
                });
        handler.postDelayed(this, PollingManager.POLL_INTERVAL);
    }

    @Override
    void removeAllMarkings() {
        for (Marker marker : currentMarkers) {
            marker.remove();
        }
        currentMarkers.clear();
    }
}