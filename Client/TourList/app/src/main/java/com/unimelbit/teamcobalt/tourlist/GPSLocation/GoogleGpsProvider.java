package com.unimelbit.teamcobalt.tourlist.GPSLocation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Hong Lin on 1/09/2017.
 */

public abstract class GoogleGpsProvider implements GPSProviderAdaptor{

    //Location provider and other utilities
    protected FusedLocationProviderClient mFusedLocationClient;
    //Location requests
    protected LocationRequest mLocationRequest;
    //Flag to see if location is being updated
    protected Boolean mRequestingLocationUpdates = false;

    protected Context c;

    protected String user;

    private final int MIN_CALLBACK_TIME = 3000, MAX_CALLBACK_TIME = 5000;

    protected LocationCallback mLocationCallback;

    public GoogleGpsProvider(Context c){

        this.c = c;

        mFusedLocationClient = new FusedLocationProviderClient(c);

    }

    /*
    Check if the GPS is on
    */
    public boolean isGPSEnable(Context c){

        final Context con = c;

        LocationManager service = (LocationManager) con.getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            return false;
        }else{
            return true;
        }

    }


    /*
Location request settings.
 */
    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        //Can change interval depending on how much battery you want
        mLocationRequest.setInterval(MAX_CALLBACK_TIME);
        mLocationRequest.setFastestInterval(MIN_CALLBACK_TIME);
        //Accuracy of location
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /*
    Get location requester
     */
    public LocationRequest getLocationRequest(){

        return this.mLocationRequest;

    }


    /*
    Get the location client
     */

    public FusedLocationProviderClient getLocationClient(){

        return this.mFusedLocationClient;

    }

    public Boolean isRequestingLocation(){

        return this.mRequestingLocationUpdates;

    }

    public void setmRequestingLocationUpdates(Boolean b){

        this.mRequestingLocationUpdates = b;

    }

    public abstract void callback();


    public LocationCallback getmLocationCallback() {
        return mLocationCallback;
    }

    public void setUser(String user){

        this.user = user;

    }

    /*
 Starts requesting the location updates
  */
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getLocationClient().requestLocationUpdates(getLocationRequest(),
                getmLocationCallback(),
                null);
    }

    /*
    Stop the location updates
     */
    public void stopLocationUpdates() {
        getLocationClient().removeLocationUpdates(getmLocationCallback());
        setmRequestingLocationUpdates(false);

    }

}