package com.example.jobquest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.jobquest", appContext.packageName)
    }


    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityScenario = activityScenarioRule.scenario
        activityScenario.onActivity { activity ->
            // Do any setup you need for your activity here
        }
    }

    @Test
    fun testJobApplicationSubmitButton(){
        // Checks whether the user will be redirected to the Sent Job Applications UI and whether Sent Applications are displayed, when a new Job Application is submitted.
        onView(withId(com.example.jobquest.R.id.submit_button)).perform(click())
        onView(withId(com.example.jobquest.R.id.sentApplicationsRecyclerView)).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

}