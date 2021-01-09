package es.uniovi.eii.cows;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.util.TestUtil;
import es.uniovi.eii.cows.view.LaunchActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.uniovi.eii.cows.util.MatcherManager.firstItem;
import static es.uniovi.eii.cows.util.MatcherManager.getText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SavedActivityTest {
    @Rule
    public ActivityScenarioRule<LaunchActivity> mActivityTestRule = new ActivityScenarioRule<>(LaunchActivity.class);

    @Test
    public void savedActivityTest() {
        TestUtil.waitForElement(withId(R.id.idTitle), 2000);
        // Get title of first news item
        String titleNews0 = getText(firstItem(withId(R.id.idTitle)));
        // Click first SAVE button
        onView(firstItem(withId(R.id.idSave))).perform(click());
        // Click drawer
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        // Click saved news on drawer
        onView(allOf(withId(R.id.nav_saved), isDisplayed())).perform(click());
        // Assert that the news item is saved
        TestUtil.waitForElement(withId(R.id.idTitle), 2000);
        onView(withText(titleNews0)).check(matches(isDisplayed()));
        // Delete saved item
        onView(withId(R.id.idRecycler_saved)).perform(actionOnItem(hasDescendant(withText(titleNews0)), click()));
        TestUtil.waitForElement(withId(R.id.idSave_news), 2000);
        onView(withId(R.id.idSave_news)).perform(click());
    }
}
