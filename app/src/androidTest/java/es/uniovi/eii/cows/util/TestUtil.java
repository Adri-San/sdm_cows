package es.uniovi.eii.cows.util;

import android.view.View;

import org.hamcrest.Matcher;
import static es.uniovi.eii.cows.util.MatcherManager.viewExists;

public class TestUtil {

    /**
     * Auxiliary method that waits {millis} milliseconds.
     *
     * @param millis milliseconds that main thread will be waiting
     */
    public static void wait(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for the visibility of an element
     * @param matcher, matcher corresponding to the element
     * @param millis, milliseconds that main thread will be waiting
     */
    public static void waitForElement(Matcher<View> matcher, int millis){
        try {
            viewExists(matcher, millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}