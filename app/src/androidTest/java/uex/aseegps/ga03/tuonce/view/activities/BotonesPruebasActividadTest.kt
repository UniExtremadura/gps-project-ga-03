package uex.aseegps.ga03.tuonce.view.activities


import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uex.aseegps.ga03.tuonce.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class BotonesPruebasActividadTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)
    @Test
    fun botonesPruebasActividadTest() {
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

        val appCompatEditText = onView(
            allOf(
                withId(R.id.etUsername),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Prueba"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.etPasswordOne),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.etPasswordTwo),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btJoin), withText("Registrarse"),
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
        appCompatButton2.perform(click())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.btLogin), withText("Iniciar Sesión"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.equipoFragment), withContentDescription("Equipo"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.plantillaBt), withText("Plantilla"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val button = onView(
            allOf(
                withId(R.id.plantillaBt), withText("PLANTILLA"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.btnPortero), withText("PORTERO"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.btnDefensa), withText("DEFENSA"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))

        val button4 = onView(
            allOf(
                withId(R.id.btnCentrocampista), withText("CENTRO CAMPISTA"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button4.check(matches(isDisplayed()))

        val button5 = onView(
            allOf(
                withId(R.id.btnDelantero), withText("DELANTERO"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button5.check(matches(isDisplayed()))

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.btnPortero), withText("Portero"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.btnDefensa), withText("Defensa"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.btnCentrocampista), withText("Centro Campista"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        val appCompatButton8 = onView(
            allOf(
                withId(R.id.btnDelantero), withText("Delantero"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton8.perform(click())

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
