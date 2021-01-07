package es.uniovi.eii.cows.view;


import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.util.TestUtil;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private UiDevice mUiDevice;

    private String accountName;
    private String password;

    @Rule
    public ActivityTestRule<LaunchActivity> mActivityTestRule = new ActivityTestRule<>(LaunchActivity.class);

    @Before
    public void before() {

        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        accountName = "actimelizategrupo@gmail.com";
        password = "COvidneWS2020";
    }

    @Test
    public void loginTest() throws Exception {

        TestUtil.getInstance().login(accountName, password);
    }

    @Test
    public void logoutTest() throws Exception {

        TestUtil.getInstance().login(accountName, password);

        TestUtil.getInstance().logout();
    }

}
