package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentEquipoBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.view.adapters.ActividadAdapter

class EquipoFragment : Fragment() {
    private var _binding: FragmentEquipoBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TuOnceDatabase
    private lateinit var repository: Repository

    private var jugadoresEquipo: List<Futbolista>? = emptyList()
    private var equipoUsuario : Equipo? = null


    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = TuOnceDatabase.getInstance(context)!!

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEquipoBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (this.activity?.application as TuOnceApplication).appContainer
        repository = appContainer.repository

        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioInteresado : User? = recuperarUsuario()
            withContext(Dispatchers.Main) {
                repository.setUserid(usuarioInteresado?.userId!!)
            }
        }
        subscribeUi()
    }


    private fun subscribeUi() {
        repository.equipoUsuario.observe(viewLifecycleOwner) { equipo ->
            equipoUsuario = equipo
            repository.setEquipoId(equipoUsuario?.equipoId!!)
            setUpListeners()
            mostrarEquipo()
        }

        repository.futbolistasDelEquipoUsuario.observe(viewLifecycleOwner) { futbolistas ->
            jugadoresEquipo = futbolistas
            mostrarEquipo()
        }
    }

    private fun setUpListeners() {
        with(binding){
            cambiarNombreBt.setOnClickListener{
                lifecycleScope.launch {
                    var equipo: Equipo? = equipoUsuario
                    equipo?.name = binding.etEquipo.text.toString().replace(" ", "")
                    nombreEquipo.text = "Equipo ${equipo?.name?.replace(" ", "")}"
                    repository.actualizarEquipo(equipo)
                }
            }
            plantillaBt.setOnClickListener {
                findNavController().navigate(R.id.action_equipoFragment_to_plantillaFragment)
            }
        }
    }

private fun mostrarEquipo(){
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
    jugadoresEquipo?.forEachIndexed { index, jugador ->
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
    val nombreEquipo = equipoUsuario?.name
    binding.etEquipo.text = Editable.Factory.getInstance().newEditable(nombreEquipo)
    binding.nombreEquipo.text = "Equipo ${equipoUsuario?.name?.replace(" ", "")}"
    binding.presupuestoText.text = "Presupuesto (euros): ${equipoUsuario?.presupuesto}"
}

    private suspend fun recuperarUsuario(): User? {
        return withContext(Dispatchers.IO) {
            db?.userDao()?.obtenerUsuarioConectado()
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
