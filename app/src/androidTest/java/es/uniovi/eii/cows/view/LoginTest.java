package es.uniovi.eii.cows.view;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.eii.cows.util.TestUtil;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private UiDevice mUiDevice;

    @Rule
    public ActivityTestRule<LaunchActivity> mActivityTestRule = new ActivityTestRule<>(LaunchActivity.class);

    @Before
    public void before() {

        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    /**
     * Important Note!: There must be a fingerprint of the account that
     * (please, see provided notes for more information).
     *
     * Test Case that is going to be used to check login and registration process.
     * - If the account is not registered yet, this test case is going to register it and check the
     *      registration process.
     * - If the account is already registered, this test case is going to log in the user and check the.
     *
     * @throws Exception
     */
    @Test
    public void loginTest() throws Exception {

        TestUtil.getInstance().login();
    }

    /**
     * Test that is going to verify if a logged user can log out.
     * @throws Exception
     */
    @Test
    public void logoutTest() throws Exception {

        TestUtil.getInstance().login();

        TestUtil.getInstance().logout();
    }

}
