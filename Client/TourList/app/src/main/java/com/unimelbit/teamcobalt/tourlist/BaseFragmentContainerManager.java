package com.unimelbit.teamcobalt.tourlist;

import android.support.v4.app.Fragment;

import com.unimelbit.teamcobalt.tourlist.CreateTrips.CreateTripFragment;
import com.unimelbit.teamcobalt.tourlist.Home.LoginFragment;
import com.unimelbit.teamcobalt.tourlist.Home.LoginOrRegisterFragment;
import com.unimelbit.teamcobalt.tourlist.Home.ProfileFragment;
import com.unimelbit.teamcobalt.tourlist.Home.RegisterFragment;
import com.unimelbit.teamcobalt.tourlist.Model.Trip;
import com.unimelbit.teamcobalt.tourlist.Model.User;
import com.unimelbit.teamcobalt.tourlist.TripSearch.TripSearchFragment;
import com.unimelbit.teamcobalt.tourlist.TripSearch.TripSearchGetRequest;
import com.unimelbit.teamcobalt.tourlist.TripSearch.TripSearchResultFragment;
import com.unimelbit.teamcobalt.tourlist.ServerRequester.LoadingFragment;
import com.unimelbit.teamcobalt.tourlist.TripDetails.TabbedTripFragment;
import com.unimelbit.teamcobalt.tourlist.TripDetails.TripGetRequest;

/**
 * Created by Sebastian on 14/9/17.
 * A class to help prevent bloating of the BaseActivity
 * Manages the main fragment container of the base activity
 * Any fragment transaction on the main fragment container should be placed in this class.
 * It can then be called using baseActivity.getBaseFragmentContainerManager().gotoDemoMethod()
 */
public class BaseFragmentContainerManager {

    private BaseActivity baseActivity;
    private int containerId;

    BaseFragmentContainerManager(BaseActivity b, int id) {
        baseActivity = b;
        containerId = id;
    }

    public int getContainerId() {
        return containerId;
    }

    /**
     * Takes the user to the register screen
     */
    public void gotoRegisterFragment() {

        RegisterFragment fragment = new RegisterFragment();
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Takes the user to the register screen
     */
    public void gotoLoginFragment() {

        LoginFragment fragment = new LoginFragment();
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Takes the user to the register screen
     */
    public void gotoProfileFragment() {

        ProfileFragment fragment = new ProfileFragment();
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Takes the user to the home screen
     */
    public void gotoLoginOrRegisterFragment() {

        LoginOrRegisterFragment fragment = LoginOrRegisterFragment.newInstance();
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Takes the user to trip details screen using a url
     */
    public void gotoTabbedTripFragment(String tripURL) {
        new TripGetRequest(tripURL, this);
    }

    /**
     * Takes the user to trip details screen using a trip object
     */
    public void gotoTabbedTripFragment(Trip trip) {

        baseActivity.setCurrentTrip(trip);
        TabbedTripFragment fragment = TabbedTripFragment.newInstance();
        gotoFragment(fragment);
    }

    /**
     * Takes the user to the search trips screen
     */
    public void gotoTripSearchFragment() {

        TripSearchFragment fragment = TripSearchFragment.newInstance();
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Takes the user to the start search result fragment and sends text over
     */
    public void gotoTripSearchResultFragment(String text, TripSearchGetRequest request) {

        TripSearchResultFragment fragment = TripSearchResultFragment.newInstance(text);
        fragment.setOnCreatedListener(request);
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Takes the user to the create trips screen
     */
    public void gotoCreateFragment() {

        CreateTripFragment fragment = new CreateTripFragment();
        gotoFragmentUsingBackstack(fragment, null);
    }

    /**
     * Start loading screen
     */
    public void gotoLoadingFragment(String loadingMsg) {

        LoadingFragment fragment = LoadingFragment.newInstance(loadingMsg);
        gotoFragment(fragment);
    }

    /**
     * Takes the user to the error screen
     */
    public void gotoErrorFragment(String errMsg) {

        ErrorFragment fragment = ErrorFragment.newInstance(errMsg);
        gotoFragment(fragment);
    }

    /**
     * Clears the main fragment container
     */
    public void clearFragmentContainer() {

        Fragment fragment = getCurrentFragment();

        if (fragment != null) {
            baseActivity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    /**
     *
     * @return the current fragment in the main fragment container
     */
    public Fragment getCurrentFragment() {
        return baseActivity.getSupportFragmentManager().findFragmentById(containerId);
    }

    /**
     * Does a fragment transaction replace the current fragment, adds it to the backstack
     * @param fragment the fragment to replace the current fragment
     */
    private void gotoFragmentUsingBackstack(Fragment fragment, String backStackTag) {
        baseActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId,fragment)
                .addToBackStack(backStackTag)
                .commit();
    }

    /**
     * Does a fragment transaction replace the current fragment, doesn't add it to backstack
     * @param fragment the fragment to replace the current fragment
     */
    private void gotoFragment(Fragment fragment) {

        baseActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }
}