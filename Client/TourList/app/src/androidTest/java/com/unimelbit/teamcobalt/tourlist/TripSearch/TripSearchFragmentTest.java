package com.unimelbit.teamcobalt.tourlist.TripSearch;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.unimelbit.teamcobalt.tourlist.BaseActivity;
import com.unimelbit.teamcobalt.tourlist.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * UI tests for the TripSearchFragment
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TripSearchFragmentTest {

    @Rule
    public ActivityTestRule<BaseActivity> mActivityRule = new ActivityTestRule<>(
            BaseActivity.class);

    /*
     *  A user flow that gets to the TripSearchFragment to test
     */
    @Before
    public void init() throws Exception {

        // Handles the case when the user isn't logged in
        if (BaseActivity.getCurrentUser() == null) {
            onView(withId(R.id.go_to_login_fragment)).perform(click());

            String username = "NewTestUser";
            onView(withId(R.id.login_username_field))
                    .perform(typeText(username), closeSoftKeyboard())
                    .check(matches(withText(username)));

            String password = "password";
            onView(withId(R.id.login_password_field))
                    .perform(typeText(password), closeSoftKeyboard());

            onView(withId(R.id.button_login)).perform(click());
        }

        // Goes to the search trip page
        onView(withId(R.id.searchButtonMain)).perform(click());
    }


    /*
     * Checks that all the required fields are displayed
     */
    @Test
    public void tripSearch()  {

        // Checks the user can enter a search
        onView(
                withId(R.id.Search_input))
                .check(matches(isDisplayed()));

        // Checks user do a random search
        onView(
                withId(R.id.Random_button))
                .check(matches(isDisplayed()));

        // Checks that the user can do a search based on the input
        onView(
                withId(R.id.Search_button))
                .check(matches(isDisplayed()));

    }

}
