package com.unimelbit.teamcobalt.tourlist.TripDetails;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.unimelbit.teamcobalt.tourlist.BaseActivity;
import com.unimelbit.teamcobalt.tourlist.R;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * UI tests for the TripDetailsFragment
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TripDetailsFragmentTest {

    @Rule
    public ActivityTestRule<BaseActivity> mActivityRule = new ActivityTestRule<>(
            BaseActivity.class);

    /*
     *  A user flow that gets to the TripDetailsFragment to test
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
        onView(
                allOf(withId(R.id.searchButtonMain),
                        withText("SEARCH TRIP"),
                        withParent(withId(R.id.linearLayout3)),
                        isDisplayed()))
                .perform(click());

        // Does a random search
        onView(
                allOf(withId(R.id.Random_button),
                        withText("I'm feeling adventurous"),
                        isDisplayed()))
                .perform(click());

        // Selects the first trip returned
        onView(
                firstView(withId(R.id.Go_to_trip)))
                .perform(click());
    }


    /*
     * Checks that all the required fields are displayed
     */
    @Test
    public void TripDetailsFragment() {

        // Checks the trips description is displayed
        onView(
                withId(R.id.trip_details_description))
                .check(matches(isDisplayed()));

        // Checks the rough cost of the trip is displayed
        onView(
                withId(R.id.trip_details_cost))
                .check(matches(isDisplayed()));

        // Checks the number of users allowed in trip is displayed
        onView(
                withId(R.id.trip_details_size))
                .check(matches(isDisplayed()));

        // Checks the trip date is displayed
        onView(
                withId(R.id.trip_details_date))
                .check(matches(isDisplayed()));

        // Checks the creator of the trip is diplayed
        onView(
                withId(R.id.trip_details_owner))
                .check(matches(isDisplayed()));

        // Checks the locations in the trip are displayed
        onView(
                withId(R.id.locations_list_view))
                .check(matches(isDisplayed()));

        // Checks the map is displayed
        onView(
                allOf(withId(R.id.imageView2)))
                .check(matches(isDisplayed()));

    }

    /*
     * A matcher that returns the first view matched
     */
    private <T> Matcher<T> firstView(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {

            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("match first item");
            }
        };
    }
}