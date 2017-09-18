package com.unimelbit.teamcobalt.tourlist.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sebastian on 14/9/17.
 * Simple class to hold trip locations, can be constructed using JSON from the server
 */
public class Location implements Parcelable{

    public static final String LOC_DEFAULT_PARCEL_KEY = "LOC_DEFAULT_PARCEL_KEY";
    public static final String JSON_TITLE = "title";
    public static final String JSON_DESC = "Description";
    public static final String JSON_LAT = "latitude";
    public static final String JSON_LON = "longitude";
    public static final String JSON_ALT = "altitude";

    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double altitude;

    Location(
            String title,
            String description,
            Double latitude,
            Double longitude,
            Double altitude
            ) {

        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;

    }

    Location(Parcel parcel) {
        title = parcel.readString();
        description = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
        altitude = parcel.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(altitude);
    }

    public static ArrayList<Location> newLocationArrayFromJSON(JSONArray jsonArray) {

        ArrayList<Location> locations = new ArrayList<>();

        try {

            for (int i=0; i<jsonArray.length();i++){

                JSONObject jsonLocation = jsonArray.getJSONObject(i);
                String title = "";
                String description = "";
                Double latitude = 0.0;
                Double longitude = 0.0;
                Double altitude = 0.0;

                try {
                    title = jsonLocation.getString(JSON_TITLE);
                } catch (JSONException e) {}
                try {
                    description = jsonLocation.getString(JSON_DESC);
                } catch (JSONException e) {}
                try {
                    latitude = jsonLocation.getDouble(JSON_LAT);
                } catch (JSONException e) {}
                try {
                    longitude = jsonLocation.getDouble(JSON_LON);
                } catch (JSONException e) {}
                try {
                    altitude = jsonLocation.getDouble(JSON_ALT);
                } catch (JSONException e) {}

                locations.add(new Location(title, description, latitude, longitude, altitude));
            }

        } catch(JSONException e) {}

        return locations;
    }

    public HashMap<String, String> toMap() {

        HashMap<String, String> map = new HashMap<>();

        map.put(JSON_TITLE, title);
        map.put(JSON_DESC, description);
        map.put(JSON_ALT, altitude.toString());
        map.put(JSON_LAT, latitude.toString());
        map.put(JSON_LON, longitude.toString());

        return map;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }

        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }
    };
}