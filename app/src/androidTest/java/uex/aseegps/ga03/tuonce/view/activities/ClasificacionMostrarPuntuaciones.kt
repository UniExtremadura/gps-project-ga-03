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
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uex.aseegps.ga03.tuonce.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class ClasificacionMostrarPuntuaciones {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun clasificacionMostrarPuntuaciones() {
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
        appCompatEditText.perform(replaceText("testClasificacion"), closeSoftKeyboard())

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
                withId(R.id.clasificacionFragment), withContentDescription("Clasificación"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tvNombreLiga), withText("No hay liga activa"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("No hay liga activa")))

        val textView2 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Clasificación"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Clasificación")))

        val textView3 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_large_label_view),
                withText("Clasificación"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.clasificacionFragment),
                                withContentDescription("Clasificación")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Clasificación")))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.misLigasFragment), withContentDescription("MisLigas"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.btnCrearLiga), withText("Crear nueva liga"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.editTextNombreLiga),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("ligaTestClasificacion"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.editTextNumJornadas),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("1"), closeSoftKeyboard())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.btnConfirmarLigaP), withText("Confirmar"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.clasificacionFragment), withContentDescription("Clasificación"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val textView4 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Clasificación"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Clasificación")))

        val textView5 = onView(
            allOf(
                withId(R.id.tvNombreLiga), withText("ligaTestClasificacion"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("ligaTestClasificacion")))

        val textView6 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_large_label_view),
                withText("Clasificación"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.clasificacionFragment),
                                withContentDescription("Clasificación")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Clasificación")))

        val recyclerView = onView(
            allOf(
                withId(R.id.rvClasificacion),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val bottomNavigationItemView4 = onView(
            allOf(
                withId(R.id.misLigasFragment), withContentDescription("MisLigas"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView4.perform(click())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.btnSimularPartido), withText("Simular jornada 1"),
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
        appCompatButton6.perform(click())

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.btnTerminarLiga), withText("Terminar liga"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        val bottomNavigationItemView5 = onView(
            allOf(
                withId(R.id.clasificacionFragment), withContentDescription("Clasificación"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView5.perform(click())

        val textView7 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Clasificación"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Clasificación")))

        val textView8 = onView(
            allOf(
                withId(R.id.tvNombreLiga), withText("No hay liga activa"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("No hay liga activa")))

        val textView9 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_large_label_view),
                withText("Clasificación"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.clasificacionFragment),
                                withContentDescription("Clasificación")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView9.check(matches(withText("Clasificación")))
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
