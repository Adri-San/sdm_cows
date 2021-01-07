package es.uniovi.eii.cows;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import es.uniovi.eii.cows.view.SavedActivity;

public class TestSavedNews {

    @Rule
    public ActivityScenarioRule<SavedActivity> activityRule = new ActivityScenarioRule<>(SavedActivity.class);

    @Test
    public void testGetSavedNews(){

    }

}
