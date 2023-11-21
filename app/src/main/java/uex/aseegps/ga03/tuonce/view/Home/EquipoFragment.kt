package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        escribirNombreEquipoEditable()
        setUpListeners()

        return binding.root
    }

    private fun setUpListeners() {

    }

    private fun escribirNombreEquipoEditable(){
        lifecycleScope.launch {
            val usuarioConectado: User? = recuperarUsuario()
            val equipo : Equipo? = recuperarEquipo(usuarioConectado)
            val jugadores : List<Futbolista>? = recuperarJugadores(equipo)

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
            jugadores?.let {
                for ((index, jugador) in it.withIndex()) {
                    val textView = textViewIds[index]
                    textView.text = jugador.nombreJugador
                }
            }

            val nombreEquipo = equipo?.name
            binding.etEquipo.text = Editable.Factory.getInstance().newEditable(nombreEquipo)
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
