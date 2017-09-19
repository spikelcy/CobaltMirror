package com.unimelbit.teamcobalt.tourlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.unimelbit.teamcobalt.tourlist.AugmentedReality.PermissionManager;
import com.unimelbit.teamcobalt.tourlist.CreateTrips.CreateTripFragment;
import com.unimelbit.teamcobalt.tourlist.Home.HomeFragment;
import com.unimelbit.teamcobalt.tourlist.Home.LoginOrRegisterFragment;
import com.unimelbit.teamcobalt.tourlist.Home.LoginFragment;
import com.unimelbit.teamcobalt.tourlist.Home.ProfileFragment;
import com.unimelbit.teamcobalt.tourlist.Home.RegisterFragment;
import com.unimelbit.teamcobalt.tourlist.Model.Trip;
import com.unimelbit.teamcobalt.tourlist.Model.User;

import com.unimelbit.teamcobalt.tourlist.TripDetails.TabbedTripFragment;
import com.unimelbit.teamcobalt.tourlist.TripSearch.TripSearchFragment;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String DEMOTRIP_NAME = "DemoTrip";
    public static final String DEMOTRIP_URL = "https://cobaltwebserver.herokuapp.com/api/trips/DemoTrip";
    public static JSONObject PUT_OBJECT;

    // current trip and user
    private static Trip currentTrip;
    private static User currentUser;
    private static Boolean locationSharing;
    private static final String LOC_SHARING_ON_MSG = "Location sharing is ON";
    private static final String LOC_SHARING_OFF_MSG = "Location sharing is OFF";

    // Permission manager
    private PermissionManager permission;

    // Manager of main fragment
    private static BaseFragmentContainerManager mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        currentTrip = null;
        locationSharing = false;
        mainContainer = new BaseFragmentContainerManager(this, R.id.fragment_container);

        // Start nav drawer
        initNavDrawer();

        // open home screen, no login
        mainContainer.gotoLoginOrRegisterFragment();

        // Permission check when initiating app
        permission = new PermissionManager() {};
        permission.checkAndRequestPermissions(this);
    }

    public static void setPutObject(JSONObject putObject) {
        PUT_OBJECT = putObject;
    }

    /**
     * Initializes the navigation drawer
     */
    private void initNavDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Set toolbar as action bar
        setSupportActionBar(toolbar);

        // Attach toggle to the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Assorted getters/setter
    public BaseFragmentContainerManager getMainContainerManager() {
        return mainContainer;
    }
    public void setCurrentTrip(Trip trip) {
        currentTrip = trip;
    }
    public Trip getCurrentTrip() {
        return currentTrip;
    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setlocationSharing(boolean share) {
        locationSharing = share;
    }
    public void toggleLocationSharing() {
        locationSharing = !locationSharing;

        if(locationSharing) {
            Toast.makeText(this,LOC_SHARING_ON_MSG,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,LOC_SHARING_OFF_MSG,
                    Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isLocationSharingOn() {
        return locationSharing;
    }


    /**
     * Closes nav drawer when back button pressed
     */
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            Fragment f = getMainContainerManager().getCurrentFragment();

            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1 || f instanceof LoginOrRegisterFragment || f instanceof HomeFragment) {
                finish();
            } else {


                if (f instanceof BackButtonInterface){

                    Fragment fragmentInstance = new HomeFragment();

                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragmentInstance)
                            .addToBackStack(null)
                            .commit();

                }

                else if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    /**
     * Links menu items in the nav drawer to the methods defining their functionality
     * @param item The menu item tapped by the user
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment f = getSupportFragmentManager().findFragmentById(mainContainer.getContainerId());

        if (id == R.id.nav_Profile &&
                !(f instanceof LoginOrRegisterFragment) &&
                !(f instanceof RegisterFragment) &&
                !(f instanceof LoginFragment) &&
                !(f instanceof ProfileFragment)
                ) {
            if (currentUser == null) {
                mainContainer.gotoLoginOrRegisterFragment();
            } else {
                mainContainer.gotoProfileFragment();
            }

        } else if (id == R.id.nav_search && !(f instanceof TripSearchFragment)) {
            mainContainer.gotoTripSearchFragment();

        } else if (id == R.id.nav_create && !(f instanceof CreateTripFragment)) {
            mainContainer.gotoCreateFragment();

        } else if (id == R.id.nav_current) {
            if (currentTrip != null) {
                mainContainer.gotoTabbedTripFragment(currentTrip);
            } else {
                mainContainer.gotoTabbedTripFragment(DEMOTRIP_NAME);
            }
        }else if (id == R.id.nav_logout){

            attemptLogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * Check for permissions and see if they are granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
    }



    public BaseFragmentContainerManager getMainContainer(){

        return mainContainer;

    }


    public void attemptLogOut(){

        Fragment f = getMainContainerManager().getCurrentFragment();

        if(f instanceof LoginFragment || f instanceof LoginOrRegisterFragment){

            Toast.makeText(this, "Cannot logout without logging in", Toast.LENGTH_LONG).show();

        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //Dialogue to display
            String message = "Are you sure you wish to logout?";

            //Direct user to location settings if they press OK, otherwise dismiss the display box
            builder.setMessage(message)
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {

                                    logOut();

                                    d.dismiss();
                                }
                            })

                    //Do no nothing if user presses 'Cancel' and close dialogue
                    .setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    d.cancel();
                                }
                            });
            builder.create().show();
        }



    }

    public void logOut(){

        Fragment fragmentInstance = new LoginOrRegisterFragment();

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentInstance)
                .addToBackStack(null)
                .commit();

        Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show();


    }


}
