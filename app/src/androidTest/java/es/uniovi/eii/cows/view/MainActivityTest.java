package es.uniovi.eii.cows.view;


import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.util.TestUtil;
import es.uniovi.eii.cows.view.LaunchActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.uniovi.eii.cows.util.MatcherManager.firstItem;
import static es.uniovi.eii.cows.util.MatcherManager.getText;
import static org.hamcrest.Matchers.equalToIgnoringCase;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<LaunchActivity> mActivityTestRule = new ActivityScenarioRule<>(LaunchActivity.class);

    @Before
    public void before(){
        TestUtil.getInstance().login();
    }

    @Test
    public void testClickFirst() {
        TestUtil.waitForElement(withId(R.id.idTitle), 7000);
        ViewInteraction recyclerView = onView(ViewMatchers.withId(R.id.idRecycler_main));
        // Get title of first news item
        String titleNews0 = getText(firstItem(withId(R.id.idTitle)));
        // Click on first news item
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        // Assert that item has the title
        assertThat(getText(withId(R.id.idTitle_news)), equalToIgnoringCase(titleNews0));
    }

    @Test
    public void testScroll() {
        TestUtil.waitForElement(withId(R.id.idTitle), 5000);
        ViewInteraction recyclerView = onView(withId(R.id.idRecycler_main));
        // Get title of first news item
        String titleNews0 = getText(firstItem(withId(R.id.idTitle)));
        // Try to scroll down
        recyclerView.perform(swipeUp());
        // Check that first item is not visible
        onView(withText(titleNews0)).check(doesNotExist());
    }

    @Test
    public void testRefresh() {
        TestUtil.waitForElement(withId(R.id.idTitle), 5000);
        ViewInteraction recyclerView = onView(withId(R.id.idRecycler_main));
        // Try to refresh news
        recyclerView.perform(swipeDown());
    }
}
