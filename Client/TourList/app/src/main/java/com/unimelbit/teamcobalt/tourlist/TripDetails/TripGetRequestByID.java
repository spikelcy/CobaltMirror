package com.unimelbit.teamcobalt.tourlist.TripDetails;


import android.util.Log;

import com.unimelbit.teamcobalt.tourlist.BaseFragmentContainerManager;
import com.unimelbit.teamcobalt.tourlist.Model.Trip;
import com.unimelbit.teamcobalt.tourlist.ServerRequester.GetRequest;
import com.unimelbit.teamcobalt.tourlist.ServerRequester.GetRequester;

import java.util.ArrayList;

/**
 * Created by Sebastian on 13/9/17.
 * Initiates a get request to retrieve a trip from the server, and takes the user to the trip
 * details screen once that trip result is retrieved
 */
public class TripGetRequestByID implements GetRequest {

    public static final String DEFAULT_URL = "https://cobaltwebserver.herokuapp.com/api/trips/findbyid/";
    private static final String LOADING_MSG = "Loading trip...";

    private String url;
    private BaseFragmentContainerManager containerManager;

    public TripGetRequestByID(String query, BaseFragmentContainerManager containerManager) {

        this.url = DEFAULT_URL + query;
        this.containerManager = containerManager;

        // Start loading fragment
        containerManager.gotoLoadingFragment(LOADING_MSG);

        // Start get request
        new GetRequester(this).execute(url);
    }

    @Override
    public void processResult(String result) {

        try {
            ArrayList<Trip> trip = Trip.newTripArrayFromJSON(result, url);
            containerManager.gotoTabbedTripFragment(trip.get(0));
        } catch (Exception e) {
            requestFailed("Something failed for url: " + url + " and result: " + result, e);
        }
    }

    @Override
    public void requestFailed(String msg,Exception e) {

        Log.e("TripGetRequest failed",msg);
        e.printStackTrace();
        containerManager.gotoErrorFragment("TripGetRequest failed: " + msg);
    }
}