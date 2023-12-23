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
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uex.aseegps.ga03.tuonce.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavegacionTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun navegacionTest() {
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

        val viewGroup = onView(
            allOf(
                withParent(
                    allOf(
                        withId(android.R.id.content),
                        withParent(withId(androidx.appcompat.R.id.action_bar_root))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

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
        appCompatEditText.perform(replaceText("testNavegacion"), closeSoftKeyboard())

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
        appCompatEditText2.perform(replaceText("12345"), closeSoftKeyboard())

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
        appCompatEditText3.perform(replaceText("12345"), closeSoftKeyboard())

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

        val textView = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_large_label_view),
                withText("MisLigas"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.misLigasFragment),
                                withContentDescription("MisLigas")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("MisLigas")))

        val textView2 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_small_label_view),
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
        textView2.check(matches(withText("Clasificación")))

        val textView3 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_small_label_view),
                withText("Equipo"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.equipoFragment),
                                withContentDescription("Equipo")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Equipo")))

        val textView4 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_small_label_view),
                withText("Mercado"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.mercadoFragment),
                                withContentDescription("Mercado")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Mercado")))

        val textView5 = onView(
            allOf(
                withId(com.google.android.material.R.id.navigation_bar_item_small_label_view),
                withText("Actividad"),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.navigation_bar_item_labels_group),
                        withParent(
                            allOf(
                                withId(R.id.actividadFragment),
                                withContentDescription("Actividad")
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Actividad")))

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

        val textView6 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Clasificación"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Clasificación")))

        val bottomNavigationItemView2 = onView(
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
        bottomNavigationItemView2.perform(click())

        val textView7 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Equipo"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Equipo")))

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
                withId(R.id.alineacionBt), withText("ALINEACIÓN"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

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

        val textView8 = onView(
            allOf(
                withId(R.id.tvEncimaRecyclerView),
                withText("Pulsa en un jugador para ver sus estadísticas"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("Pulsa en un jugador para ver sus estadísticas")))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.nPrincipal),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.mercadoFragment), withContentDescription("Mercado"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val textView9 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Mercado"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView9.check(matches(withText("Mercado")))

        val bottomNavigationItemView4 = onView(
            allOf(
                withId(R.id.actividadFragment), withContentDescription("Actividad"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView4.perform(click())

        val textView10 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Actividad"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView10.check(matches(withText("Actividad")))

        val bottomNavigationItemView5 = onView(
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
        bottomNavigationItemView5.perform(click())

        val textView11 = onView(
            allOf(
                withId(R.id.toolbar_title), withText("Mis Ligas"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView11.check(matches(withText("Mis Ligas")))
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
