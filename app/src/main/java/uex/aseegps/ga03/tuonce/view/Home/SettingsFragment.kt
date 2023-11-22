package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import uex.aseegps.ga03.tuonce.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}