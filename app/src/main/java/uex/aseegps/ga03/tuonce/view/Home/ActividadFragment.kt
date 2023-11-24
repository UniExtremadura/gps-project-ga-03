package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.adapter.ActividadAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentActividadBinding
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User




class ActividadFragment : Fragment() {
    private var _binding: FragmentActividadBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: TuOnceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentActividadBinding.inflate(inflater, container, false)
        binding.rvActividad.layoutManager = LinearLayoutManager(context)
        cargarActividadesDesdeBD()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        cargarActividadesDesdeBD()
    }

    private fun cargarActividadesDesdeBD() {
        lifecycleScope.launch(Dispatchers.IO) {

            val usuario = User(
                userId = null,
                image = R.drawable.ic_launcher_background,
                name = "Falgas",
                password = "contraseña123",
                points = 100,
                conectado = 1
            )

            val futbolista1 = Futbolista(
                futbolistaId = null,
                nombreJugador = "Lionel Messi",
                años = "34",
                posicion = "Delantero",
                varor = 100000000,
                minutoJugados = 90,
                goles = 30,
                asistencias = 15,
                balonAlArea = 50,
                parada = 0,
                tarjetaAmarilla = 1,
                tarjetaRoja = 0,
                media = 90,
                puntosAportados = 50,
                faltacometidas = 10,
                equipoId = 1
            )

            val futbolista2 = Futbolista(
                futbolistaId = null,
                nombreJugador = "Cristiano Ronaldo",
                años = "36",
                posicion = "Delantero",
                varor = 90000000,
                minutoJugados = 90,
                goles = 25,
                asistencias = 10,
                balonAlArea = 40,
                parada = 0,
                tarjetaAmarilla = 2,
                tarjetaRoja = 0,
                media = 89,
                puntosAportados = 45,
                faltacometidas = 8,
                equipoId = 2
            )

            val ligaPrueba: Liga = Liga(
                ligaId = null,
                name = "Liga Increíble",
                partidos = 15,
                userId = usuario.userId
            )

            val listaActividades = listOf(
                Actividad(null, AccionActividad.COMPRAR_FUTBOLISTA ,usuario, futbolista1, null, null),
                Actividad(null, AccionActividad.COMPRAR_FUTBOLISTA ,usuario, futbolista2, null, null),
                Actividad(null, AccionActividad.VENDER_FUTBOLISTA ,usuario, futbolista1, null, null),
                Actividad(null, AccionActividad.INICIAR_LIGA ,usuario, null, ligaPrueba, null),
                Actividad(null, AccionActividad.ACABAR_LIGA ,usuario, null, ligaPrueba, null),
                Actividad(null, AccionActividad.INICIAR_JORNADA ,usuario, null, null, 4),
                Actividad(null, AccionActividad.INICIAR_JORNADA ,usuario, null, null, 4)
            )


            withContext(Dispatchers.Main) {
                val adapter = ActividadAdapter(listaActividades)
                binding.rvActividad.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}