package com.example.appdevproject

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appdevproject.audioList.AudioAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import androidx.test.espresso.IdlingResource

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {
    @get:Rule var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private lateinit var mIdlingResource: IdlingResource

    @Before
    fun startMainActivity() {
        Espresso.onView(withId(R.id.rv_audio_list))
            .perform(
                RecyclerViewActions
                    .scrollToPosition<AudioAdapter.AudioViewHolder>(0)
            )
    }

    @Test
    // Check if the first items have the correct title
    // I want the first three items to always be AA, EE and OO
    fun testFirstItemsTitle() {
        Espresso
            .onView(RecyclerViewMatcher(R.id.rv_audio_list)
                .atPositionOnView(0, R.id.audio_title))
            .check(matches(withText("AA")))

        Espresso
            .onView(RecyclerViewMatcher(R.id.rv_audio_list)
                .atPositionOnView(1, R.id.audio_title))
            .check(matches(withText("EE")))

        Espresso
            .onView(RecyclerViewMatcher(R.id.rv_audio_list)
            .atPositionOnView(2, R.id.audio_title))
            .check(matches(withText("OO")))
    }

    @Test
    // Make sure the stream button contains the correct text
    fun testStreamButtonText() {
        Espresso
            .onView(withId(R.id.open_stream_but))
            .check(matches(withText(R.string.visit_stream)))
    }
}

class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (this.resources != null) {
                    idDescription = try {
                        this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format(
                            "%s (resource name not found)",
                            recyclerViewId
                        )
                    }

                }

                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {
                this.resources = view.resources

                if (childView == null) {
                    val recyclerView =
                        view.rootView.findViewById<View>(recyclerViewId) as RecyclerView
                    if (recyclerView.id == recyclerViewId) {
                        childView =
                            recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    view === targetView
                }

            }
        }
    }
}
