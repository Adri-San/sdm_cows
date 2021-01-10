package es.uniovi.eii.cows.util;

import android.view.View;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import es.uniovi.eii.cows.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.uniovi.eii.cows.util.MatcherManager.childAtPosition;
import static es.uniovi.eii.cows.util.MatcherManager.viewExists;
import static org.hamcrest.Matchers.allOf;

public class TestUtil {

    private static TestUtil instance = new TestUtil(); //Singleton

    private UiDevice mUiDevice;
    private String accountName;
    private String password;

    private TestUtil(){
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        accountName = "actimelizategrupo@gmail.com";
        password = "COvidneWS2020";
    }

    public static TestUtil getInstance() {
        return instance;
    }

    public void login() {
        try {
            login(accountName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Method that performs the login process in the application. If user account
     * is not in the system, it will be registered
     *
     * @param accountName   email of the account
     * @param password      password of the account
     * @throws UiObjectNotFoundException
     */
    public void login(String accountName, String password) throws Exception {

        if(isAlreadyLoggedIn()) //if it is already logged in, it's over
            return;

        //click on "Sign in" button
        ViewInteraction materialButton = onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btnAuth), withText("Sign In"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));

        materialButton.perform(click());

        try {
            useExistentAccount(accountName);        //account is in the system
        } catch (UiObjectNotFoundException e) {
            addAccount(accountName, password);      //account needs to be registered
        }

    }

    /**
     * Method that performs the logout process in the application.
     *
     */
    public void logout(){

        if(!isAlreadyLoggedIn()) //if it's not logged in, it's over
            return;

        ViewInteraction navDrawer = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.app_bar),
                                        childAtPosition(
                                                withId(R.id.appBarLayout),
                                                0)),
                                1),
                        isDisplayed()));
        navDrawer.perform(click());

        ViewInteraction logoutOption = onView(
                allOf(withId(R.id.nav_logout),
                        childAtPosition(
                                allOf(withId(R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                4),
                        isDisplayed()));
        logoutOption.perform(click());
    }

    /**
     * Auxiliary method that checks if the user is already logged in the
     * application checking the title of the main page (COWS).
     *
     * @return true if it is already logged in, otherwise, false
     */
    private boolean isAlreadyLoggedIn() {
        return mUiDevice.wait(Until.hasObject(By.text("COWS")), 3000L);
    }

    /**
     * Method that performs the logging process with an existent account in the system.
     *
     * @param accountName   email of the account
     * @throws UiObjectNotFoundException
     */
    private void useExistentAccount(String accountName) throws UiObjectNotFoundException {
        UiObject mText = mUiDevice.findObject(new UiSelector().text(accountName)); //clicks on the provided account option
        mText.click();
    }

    /**
     * Method that adds a non-registered account to the system
     *
     * @param accountName   email of the account
     * @param password      password of the account
     * @throws UiObjectNotFoundException
     */
    private void addAccount(String accountName, String password) throws Exception {

        //if there are other accounts registered in the system, we will need
        // to click "Add another account" option in the Google pop-up
        if(mUiDevice.wait(Until.hasObject(By.text("Add another account")), 7000L)){
            UiObject mText = mUiDevice.findObject(new UiSelector().text("Add another account"));
            mText.click();
        }

        //Typing email
        UiObject emailInput = mUiDevice.findObject(new UiSelector().enabled(true).index(2));
        emailInput.click();
        wait(500);
        emailInput.legacySetText(accountName);
        wait(500);

        //Closing keyboard
        if(isKeyboardOpened())
            mUiDevice.pressBack();

        //Typing password
        UiObject nextButton = mUiDevice.findObject(new UiSelector().textContains("Next"));
        nextButton.click();

        if (!waitUntilText("info", 3000L)){
            UiObject passwordInput = mUiDevice.findObject(new UiSelector().enabled(true).index(2));
            passwordInput.legacySetText(password);
            wait(500);
        }

        UiObject passwordInput = mUiDevice.findObject(new UiSelector().enabled(true).index(2));
        passwordInput.legacySetText(password);
        wait(500);

        //Closing keyboard
        if(isKeyboardOpened())
            mUiDevice.pressBack();

        //Clicking "Next" button
        nextButton = mUiDevice.findObject(new UiSelector().textContains("Next"));
        nextButton.click();
        wait(1000);

        //Scrolling down to make button visible for small phones
        UiObject page = mUiDevice.findObject(new UiSelector().textContains("privacy"));
        page.swipeUp(100);

        //Clicking Agreement button
        if(waitUntilClass("android.widget.Button", 2000L)){
            UiObject agreeButton = mUiDevice.findObject(new UiSelector().className("android.widget.Button").index(0));
            agreeButton.click();
        }

        wait(1000);

        //Clicking More Button
        if(!waitUntilText("info", 3000L) && waitUntilClass("android.widget.Button", 2000L)){
            UiObject moreButton = mUiDevice.findObject(new UiSelector().className("android.widget.Button").index(0));
            moreButton.click();
        }

        wait(1000);

        //Clicking Accept Button
        if(!waitUntilText("info", 3000L) && waitUntilClass("android.widget.Button", 2000L)){
            UiObject acceptButton = mUiDevice.findObject(new UiSelector().className("android.widget.Button").index(0));
            acceptButton.click();
        }

        wait(7000);
    }

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

    private boolean waitUntilClass(String className, Long timeout){
        return mUiDevice.wait(Until.hasObject(By.clazz(className)), timeout);
    }

    private boolean waitUntilText(String text, Long timeout){
        return mUiDevice.wait(Until.hasObject(By.text(text)), timeout);
    }

    /**
     * Auxiliary method that checks if keyboard is opened.
     *
     * @return true if it is opened, otherwise, false
     */
    public boolean isKeyboardOpened(){
        for(AccessibilityWindowInfo window: InstrumentationRegistry.getInstrumentation().getUiAutomation().getWindows()){
            if(window.getType()==AccessibilityWindowInfo.TYPE_INPUT_METHOD){
                return true;
            }
        }
        return false;
    }
}
