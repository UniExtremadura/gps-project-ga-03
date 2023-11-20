package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uex.aseegps.ga03.tuonce.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CrearLigaPersonalizada.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearLigaPersonalizada : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_liga_personalizada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén el NavController
        val navController = findNavController()

        // Configura el OnClickListener para el botón de retroceso
        view.findViewById<View>(R.id.btnAtras).setOnClickListener {
            // Navega hacia atrás cuando se hace clic en el botón
            navController.navigateUp()
        }
    }
}