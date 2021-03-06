package com.unimelbit.teamcobalt.tourlist.GPSLocation;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.unimelbit.teamcobalt.tourlist.AppServicesFactory;
import com.unimelbit.teamcobalt.tourlist.AugmentedReality.ARActivity;
import com.unimelbit.teamcobalt.tourlist.BaseActivity;
import com.unimelbit.teamcobalt.tourlist.Tracking.UserTracker;

/**
 * Created by Hong Lin on 5/10/2017.
 */

/**
 * Google GPS tools used to provide locations consistently by AR
 */

public class ARGoogleGpsProvider extends GoogleGpsProvider {

    private final int ACCURACY = 7;

    private final int TIME_TO_WAIT = 1000;

    private ARActivity arActivity;

    public ARGoogleGpsProvider(Context c) {
        super(c);

        arActivity = (ARActivity) c;
    }


    public void callback(){

        //Location to be sent to the view
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //Loop through the results
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data

                    double latitude = location.getLatitude();

                    double longitude = location.getLongitude();

                    if (location != null && arActivity.getArchitectView() != null) {

                        // Send information if inaccurate, or just end every second

                        if (location.hasAltitude() && location.hasAccuracy() && location.getAccuracy() < ACCURACY) {

                            arActivity.getArchitectView().setLocation(latitude,
                                    longitude, location.getAltitude(), location.getAccuracy());

                        } else {

                            arActivity.getArchitectView().setLocation(latitude,
                                    longitude, location.hasAccuracy() ? location.getAccuracy() : TIME_TO_WAIT);

                        }


                        postToFireBase(longitude, latitude);

                    }
                }
            };
        };



    }



}
