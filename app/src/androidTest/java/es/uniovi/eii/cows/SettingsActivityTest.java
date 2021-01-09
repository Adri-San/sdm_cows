package es.uniovi.eii.cows;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.util.TestUtil;
import es.uniovi.eii.cows.view.LaunchActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {
    @Rule
    public ActivityScenarioRule<LaunchActivity> mActivityTestRule = new ActivityScenarioRule<>(LaunchActivity.class);

    @Test
    public void changeSourcesSelected() {
        TestUtil.waitForElement(withContentDescription(R.string.navigation_drawer_open), 2000);
        // Click drawer
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        // Click settings on drawer
        onView(allOf(withId(R.id.nav_settings), isDisplayed())).perform(click());
        // Deselect La nueva España, El Diario and El país
        TestUtil.wait(1000);
        clickSourcesCheckbox(R.string.eldiario, R.string.elpais, R.string.lne);
        // Return to main view
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        // Assert that the news are from ABC
        TestUtil.waitForElement(withId(R.id.idSource), 2000);
        assertNotNewsFrom(R.string.eldiario, R.string.elpais, R.string.lne);
        // Reset selected sources
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(allOf(withId(R.id.nav_settings), isDisplayed())).perform(click());
        TestUtil.wait(1000);
        clickSourcesCheckbox(R.string.eldiario, R.string.elpais, R.string.lne);
    }

    @Test
    public void noSourcesSelected() {
        TestUtil.waitForElement(withContentDescription(R.string.navigation_drawer_open), 2000);
        // Click drawer
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        // Click settings on drawer
        onView(allOf(withId(R.id.nav_settings), isDisplayed())).perform(click());
        // Deselect all sources
        TestUtil.wait(1000);
        clickSourcesCheckbox(R.string.abc, R.string.eldiario, R.string.elpais, R.string.lne);
        // Return to main view
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        // Assert that there are no news
        TestUtil.waitForElement(withText(R.string.msg_no_news), 2000);
        assertNotNewsFrom(R.string.abc, R.string.eldiario, R.string.elpais, R.string.lne);
        onView(withText(R.string.msg_no_news)).check(matches(isDisplayed()));
        // Reset selected sources
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(allOf(withId(R.id.nav_settings), isDisplayed())).perform(click());
        TestUtil.wait(1000);
        clickSourcesCheckbox(R.string.abc, R.string.eldiario, R.string.elpais, R.string.lne);
    }

    /**
     * Clicks the checkbox sources
     * @param sources, list of resource strings, corresponding to CheckBoxPreference titles
     */
    private void clickSourcesCheckbox(int...sources) {
        for (int source : sources) { onView(withText(source)).perform(click()); }
    }

    /**
     * Check that there are not news from the sources
     * @param sources, list of resource strings, corresponding to CheckBoxPreference titles
     */
    private void assertNotNewsFrom(int...sources){
        for (int source : sources) { onView(withText(source)).check(doesNotExist()); }
    }
}
