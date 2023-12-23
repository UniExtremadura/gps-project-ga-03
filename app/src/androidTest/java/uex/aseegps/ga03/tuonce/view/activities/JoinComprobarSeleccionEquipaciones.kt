package uex.aseegps.ga03.tuonce.view.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uex.aseegps.ga03.tuonce.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class JoinComprobarSeleccionEquipaciones {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    fun hasBackground(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("has a background")
            }

            override fun matchesSafely(view: View): Boolean {
                return view.background != null
            }
        }
    }

    @Test
    fun joinComprobarSeleccionEquipaciones() {
        val appCompatButton = onView(
            allOf(
                withId(R.id.btJoin), withText("Registrate"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tvSeleccionaEquipacion), withText("Selecciona equipación"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Selecciona equipación")))

        val linearLayout = onView(
            allOf(
                withId(R.id.llEquipaciones),
                withParent(
                    allOf(
                        withId(R.id.hsvEquipaciones),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val imageView = onView(
            childAtPosition(
                allOf(
                    withId(R.id.llEquipaciones),
                    childAtPosition(
                        withId(R.id.hsvEquipaciones),
                        0
                    )
                ),
                0
            )
        )
        imageView.perform(scrollTo(), click())
        // Verificar que se ha seleccionado la equipación
        imageView.check(matches(hasBackground()))

        val imageView2 = onView(
            childAtPosition(
                allOf(
                    withId(R.id.llEquipaciones),
                    childAtPosition(
                        withId(R.id.hsvEquipaciones),
                        0
                    )
                ),
                2
            )
        )
        imageView2.perform(scrollTo(), click())
        // Verificar que se ha seleccionado la equipación
        imageView2.check(matches(hasBackground()))
        // Verificar que se ha deseleccionado la equipación anterior
        imageView.check(matches(not(hasBackground())))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
