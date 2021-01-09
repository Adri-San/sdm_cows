package es.uniovi.eii.cows.util;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Matchers created to use in Espresso tests
 */
public class MatcherManager {
    /**
     * Created by Espresso Recorder.
     * Find child at position in parent
     * @param parentMatcher parent
     * @param position of the element
     * @return element that matches
     */
    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * Get text of a TextView
     * @param matcher element
     * @return String with the text of the element that matches
     */
    public static String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    /**
     *  Find first matching item
     * @param matcher element
     * @param <T> generic
     * @return element that matches
     */
    public static <T> Matcher<T> firstItem(final Matcher<T> matcher) {
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
                description.appendText("should return first matching item");
            }
        };
    }

    /**
     * Waits for a specific view during {millis} milliseconds
     * @param viewMatcher element
     * @param millis milliseconds
     * @return if the element is displayed
     * @throws InterruptedException exception
     */
    public static boolean viewExists(final Matcher<View> viewMatcher, final long millis) throws InterruptedException {
        final Boolean[] found = new Boolean[1];

        final CountDownLatch latch = new CountDownLatch(1);
        ViewAction action = new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewMatcher.toString() + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(child)) {
                            Log.d("Synchronizer", "perform: found match");
                            found[0] = true;
                            latch.countDown();
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                found[0] = false;
                latch.countDown();
            }
        };
        onView(isRoot()).perform(action);

        latch.await();
        return found[0];
    }

    /**
     * Matcher used for chooser intents (Share intent)
     * @param matcher matcher used to find data in the intent
     * @return element found
     */
    public static Matcher<Intent> chooser(Matcher<Intent> matcher) {
        return allOf(hasAction(Intent.ACTION_CHOOSER), hasExtra(is(Intent.EXTRA_INTENT), matcher));
    }
}
