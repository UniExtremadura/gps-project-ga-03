package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import uex.aseegps.ga03.tuonce.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val faqPreference: Preference? = findPreference("preguntas_frecuentes")

        faqPreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            // Navega al fragmento PreguntasFrecuentesFragment cuando se hace clic en el Preference
            navigateToPreguntasFrecuentesFragment()
            true
        }
    }

    private fun navigateToPreguntasFrecuentesFragment() {
        // Obtén el controlador de navegación
        val navController = findNavController()

        // Utiliza el controlador para navegar al fragmento PreguntasFrecuentesFragment
        navController.navigate(R.id.action_settingsFragment_to_preguntasFrecuentesFragment)


    }

}