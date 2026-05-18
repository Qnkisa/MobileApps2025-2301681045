package com.example.zerog

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Single Espresso UI test that exercises the full happy-path flow:
 *   1. Input a name and weight in the Simulator screen
 *   2. Calculate and save the result to the database
 *   3. Navigate to the Log (History) screen
 *   4. Assert the saved entry is visible in the list
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun inputWeight_calculate_save_navigateToHistory_assertEntryVisible() {
        // 1. Type item name
        onView(withId(R.id.etItemName))
            .perform(typeText("Moon Rock"), closeSoftKeyboard())

        // 2. Type Earth weight
        onView(withId(R.id.etEarthWeight))
            .perform(typeText("100"), closeSoftKeyboard())

        // 3. Click Calculate — result card appears, Save/Share become enabled
        onView(withId(R.id.btnCalculate)).perform(click())

        // 4. Save to Log — triggers Room insert via ViewModel coroutine
        onView(withId(R.id.btnSave)).perform(click())

        // 5. Allow the coroutine + Room write to complete
        Thread.sleep(600)

        // 6. Switch to History tab via BottomNavigationView
        onView(withId(R.id.navigation_history)).perform(click())

        // 7. Assert the saved item name is visible somewhere in the RecyclerView
        onView(withText("Moon Rock")).check(matches(isDisplayed()))
    }
}
