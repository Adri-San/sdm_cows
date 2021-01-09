package es.uniovi.eii.cows.view;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.uniovi.eii.cows.MatcherManager.firstItem;
import static es.uniovi.eii.cows.MatcherManager.getText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SavedActivityTest {
    @Rule
    public ActivityScenarioRule<LaunchActivity> mActivityTestRule = new ActivityScenarioRule<>(LaunchActivity.class);

    @Test
    public void savedActivityTest() {
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        // Get title of first news item
        String titleNews0 = getText(firstItem(withId(R.id.idTitle)));
        // Click first SAVE button
        onView(firstItem(withId(R.id.idSave))).perform(click());
        // Click drawer
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        // Click saved news on drawer
        onView(allOf(withId(R.id.nav_saved), isDisplayed())).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        // Assert that the news item is saved
        onView(withText(titleNews0)).check(matches(isDisplayed()));
    }
}
