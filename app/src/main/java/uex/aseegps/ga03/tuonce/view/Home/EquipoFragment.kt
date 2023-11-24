package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentEquipoBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

class EquipoFragment : Fragment() {
    private var _binding: FragmentEquipoBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TuOnceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEquipoBinding.inflate(inflater, container, false)

        escribirNombreEquipoEditableDinamico()
        setUpListeners()

        return binding.root
    }

    private fun setUpListeners() {
        with(binding){
            cambiarNombreBt.setOnClickListener{
                lifecycleScope.launch {
                    val usuarioConectado: User? = recuperarUsuario()
                    var equipo: Equipo? = recuperarEquipo(usuarioConectado)
                    equipo?.name = binding.etEquipo.text.toString().replace(" ", "")
                    nombreEquipo.text = "Equipo ${equipo?.name?.replace(" ", "")}"
                    actualizarEquipo(equipo)
                }
            }
            plantillaBt.setOnClickListener {
                findNavController().navigate(R.id.action_equipoFragment_to_plantillaFragment)
            }
        }
    }

// AÑADIR AL 11 EN EL EQUIPO
private fun escribirNombreEquipoEditableDinamico(){
    lifecycleScope.launch {
        val usuarioConectado: User? = recuperarUsuario()
        val equipo: Equipo? = recuperarEquipo(usuarioConectado)
        val jugadores: List<Futbolista>? = recuperarJugadores(equipo)

        // Lista de IDs de TextViews
        val textViewIds = mutableListOf(
            binding.playerSlot1Label,
            binding.playerSlot2Label,
            binding.playerSlot3Label,
            binding.playerSlot4Label,
            binding.playerSlot5Label,
            binding.playerSlot6Label,
            binding.playerSlot7Label,
            binding.playerSlot8Label,
            binding.playerSlot9Label,
            binding.playerSlot10Label,
            binding.playerSlot11Label
        )

        // Cambiar el texto de los TextViews
        var cont = 0
        var defensa = 0
        var centrocampista = 4
        var delantero = 7
        jugadores?.forEachIndexed { index, jugador ->
            if (jugador.estaEnel11 == 1 && cont < 11) {
                if (jugador.posicion == "Portero") {
                    val textView = textViewIds[10]
                    textView.text = jugador.nombreJugador
                }
                if (jugador.posicion == "Defensa") {
                    val textView = textViewIds[defensa]
                    defensa++
                    textView.text = jugador.nombreJugador
                }
                if (jugador.posicion == "Centrocampista") {
                    val textView = textViewIds[centrocampista]
                    centrocampista++
                    textView.text = jugador.nombreJugador
                }
                if (jugador.posicion == "Delantero") {
                    val textView = textViewIds[delantero]
                    delantero++
                    textView.text = jugador.nombreJugador
                }
            }
        }
        val nombreEquipo = equipo?.name
        binding.etEquipo.text = Editable.Factory.getInstance().newEditable(nombreEquipo)
        binding.nombreEquipo.text = "Equipo ${equipo?.name?.replace(" ", "")}"
        binding.presupuestoText.text = "Presupuesto (euros): ${equipo?.presupuesto}"
    }
}

    private suspend fun recuperarUsuario(): User? {
        return withContext(Dispatchers.IO) {
            db?.userDao()?.obtenerUsuarioConectado()
        }
    }
    private suspend fun recuperarEquipo(usuario: User?): Equipo? {
        return withContext(Dispatchers.IO) {
            db?.equipoDao()?.findByUserId(usuario?.userId)
        }
    }

    private suspend fun actualizarEquipo(equipo: Equipo?) {
        return withContext(Dispatchers.IO) {
            db?.equipoDao()?.update(equipo)

        }
    }
    private suspend fun recuperarJugadores(equipo: Equipo?): List<Futbolista>? {
        return withContext(Dispatchers.IO) {
            db?.futbolistaDao()?.findByEquipoId(equipo?.equipoId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // evitar fugas de memoria
    }

    companion object {
        @JvmStatic
        fun newInstance(usuario: String) =
            EquipoFragment().apply {
            }
    }
}
