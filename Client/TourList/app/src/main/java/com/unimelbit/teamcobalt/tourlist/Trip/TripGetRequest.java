package com.unimelbit.teamcobalt.tourlist.Trip;


import android.util.Log;

import com.unimelbit.teamcobalt.tourlist.BaseFragmentContainerManager;
import com.unimelbit.teamcobalt.tourlist.ServerRequester.GetRequest;
import com.unimelbit.teamcobalt.tourlist.ServerRequester.GetRequester;

/**
 * Created by Sebastian on 13/9/17.
 * Initiates a get request to retrieve a trip from the server, and takes the user to the trip
 * details screen once that trip result is retrieved
 */
public class TripGetRequest implements GetRequest {

    private static String LOADING_MSG = "Loading trip...";

    private String url;
    private BaseFragmentContainerManager containerManager;

    public TripGetRequest(String url, BaseFragmentContainerManager containerManager) {
        this.url = url;
        this.containerManager = containerManager;

        containerManager.gotoLoadingScreen(LOADING_MSG);
        new GetRequester(this).execute(url);
    }

    @Override
    public void processResult(String result) {
        try {
            TripDetails trip = new TripDetails(result, url);
            containerManager.gotoTabbedTripFragment(trip);
        } catch (Exception e) {
            requestFailed("Something failed for url: " + url + " and result: " + result, e);
        }
    }

    @Override
    public void requestFailed(String msg,Exception e) {
        Log.e("Trip get req failure",msg);
        e.printStackTrace();
        containerManager.clearFragmentContainer();
    }
}
