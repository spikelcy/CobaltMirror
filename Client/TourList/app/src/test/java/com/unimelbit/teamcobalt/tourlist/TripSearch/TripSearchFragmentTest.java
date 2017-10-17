package com.unimelbit.teamcobalt.tourlist.TripSearch;

import android.support.v4.app.Fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by awhite on 17/10/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class TripSearchFragmentTest {

    /**
     * Check androidTest folder for tests of TripSearchFragment.
     * The class has no application logic to test, it creates an
     * Fragment and View that allows the user to search for a trip.
     */

    @Test
    public void newInstance() throws Exception {
        Fragment fragment = TripSearchFragment.newInstance();
        assertNotNull(fragment);
    }

}