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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uex.aseegps.ga03.tuonce.R
import androidx.test.espresso.matcher.BoundedMatcher
import android.widget.ImageView
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Matchers.not

@LargeTest
@RunWith(AndroidJUnit4::class)
class JoinComprobarSeleccionEscudos {

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
    fun joinComprobarSeleccionEscudos() {
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
        appCompatEditText.perform(replaceText("testComprobarEscudos"), closeSoftKeyboard())

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

        val imageView = onView(
            childAtPosition(
                allOf(
                    withId(R.id.llEscudos),
                    childAtPosition(
                        withClassName(`is`("android.widget.HorizontalScrollView")),
                        0
                    )
                ),
                0
            )
        )
        imageView.perform(scrollTo(), click())
        // Verifica que el borde verde se ha aplicado al escudo seleccionado
        imageView.check(matches(hasBackground()))

        val imageView2 = onView(
            childAtPosition(
                allOf(
                    withId(R.id.llEscudos),
                    childAtPosition(
                        withClassName(`is`("android.widget.HorizontalScrollView")),
                        0
                    )
                ),
                1
            )
        )
        imageView2.perform(scrollTo(), click())
        // Verifica que el borde verde se ha aplicado al escudo seleccionado
        imageView2.check(matches(hasBackground()))
        // Verifica que el escudo 0 no tiene el borde verde
        imageView.check(matches(not(hasBackground())))

        val textView = onView(
            allOf(
                withId(R.id.tvSeleccionaEscudo), withText("Selecciona escudo"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Selecciona escudo")))

        val linearLayout = onView(
            allOf(
                withId(R.id.llEscudos),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))
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
