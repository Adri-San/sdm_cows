package es.uniovi.eii.cows;

import android.content.Intent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.util.TestUtil;
import es.uniovi.eii.cows.view.LaunchActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static es.uniovi.eii.cows.util.MatcherManager.chooser;
import static es.uniovi.eii.cows.util.MatcherManager.firstItem;
import static es.uniovi.eii.cows.util.MatcherManager.getText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalToIgnoringCase;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewsActivityTest {
    @Rule
    public ActivityScenarioRule<LaunchActivity> mActivityTestRule = new ActivityScenarioRule<>(LaunchActivity.class);

    @Before
    public void before(){ Intents.init(); }

    @After
    public void after(){ Intents.release(); }

    @Test
    public void testShowDescription() {
        ViewInteraction recyclerView = onView(withId(R.id.idRecycler_main));
        // Wait until news are loaded
        TestUtil.waitForElement(withId(R.id.idTitle), 2000);
        // Get title of first news item
        String titleNews0 = getText(firstItem(withId(R.id.idTitle)));
        // Click on first news item
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        // Assert that the news item shows the description
        TestUtil.waitForElement(withId(R.id.idTitle_news), 2000);
        assertThat(getText(withId(R.id.idTitle_news)), equalToIgnoringCase(titleNews0));
        onView(withId(R.id.idDescription_news)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_action_button_news)).check(matches(isDisplayed()));
    }

    @Test
    public void testShowCompleteNewsItem() {
        ViewInteraction recyclerView = onView(withId(R.id.idRecycler_main));
        // Wait until news are loaded
        TestUtil.waitForElement(withId(R.id.idTitle), 2000);
        // Click on first news item
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        // Click on the FAB to show complete news item
        TestUtil.waitForElement(withId(R.id.floating_action_button_news), 2000);
        onView(withId(R.id.floating_action_button_news)).check(matches(isDisplayed())).perform(click());
        // Check Intent
        intended(hasAction(Intent.ACTION_VIEW));
    }

    @Test
    public void testShareNewsItem() {
        ViewInteraction recyclerView = onView(withId(R.id.idRecycler_main));
        // Wait until news are loaded
        TestUtil.waitForElement(withId(R.id.idTitle), 3000);
        // Click on first news item
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        // Get news item title
        TestUtil.waitForElement(withId(R.id.idTitle_news), 2000);
        String titleNewsItem = getText(withId(R.id.idTitle_news));
        // Click on Share button
        TestUtil.waitForElement(withId(R.id.idShare_news), 2000);
        onView(withId(R.id.idShare_news)).check(matches(isDisplayed())).perform(click());
        // Check Intent
        intended(chooser(allOf(
                hasAction(Intent.ACTION_SEND),
                hasExtra(Intent.EXTRA_SUBJECT, titleNewsItem),
                hasType("text/plain")
        )));
    }
}
