package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.adapter.PlantillaAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentPlantillaBinding
import uex.aseegps.ga03.tuonce.utils.SortPlayers.clasificarJugadores
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

class PlantillaFragment : Fragment() {

    private lateinit var db: TuOnceDatabase

    private var _binding: FragmentPlantillaBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlantillaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlantillaBinding.inflate(inflater, container, false)
        setUpListeners()
        return binding.root
    }

    private fun setUpListeners() {
        binding.alineacionBt.setOnClickListener {
            findNavController().navigate(R.id.action_plantillaFragment_to_equipoFragment)
        }
        binding.buttonOrdenarPuntos.setOnClickListener {
            var futbolistasDelEquipo = mutableListOf<Futbolista>()
            lifecycleScope.launch {
                val futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()
                val equipo: Equipo? = recuperarEquipo(recuperarUsuario())
                futbolistas?.forEach {
                    if (it.equipoId == equipo?.equipoId) {
                        futbolistasDelEquipo.add(it)
                    }
                }
                val futbolistasDelEquipoOrdenados = clasificarJugadores(futbolistasDelEquipo)

                // mostrar los futbolistas del equipo por consola
                futbolistasDelEquipoOrdenados.forEach {
                    println(it)
                }

                adapter.updateData(futbolistasDelEquipoOrdenados)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        }

    private fun setUpRecyclerView() {
        var futbolistasDelEquipo = mutableListOf<Futbolista>()
        val context = this.context
        lifecycleScope?.launch {
            var futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()

            val usuarioConectado = recuperarUsuario()
            val equipo: Equipo? = recuperarEquipo(usuarioConectado)
            futbolistas?.forEach {
                if (it.equipoId == equipo?.equipoId) {
                    futbolistasDelEquipo.add(it)
                }
            }
            val futbolistasDelEquipoOrdenados = futbolistasDelEquipo.sortedBy { it.estaEnel11 }
            adapter = PlantillaAdapter(
                lista = futbolistasDelEquipoOrdenados,
                contexto = context,
                viewLifecycleOwner.lifecycleScope,
                onClick = {
                    lifecycleScope.launch {

                        var futbolistaVendido : Futbolista? = db?.futbolistaDao()?.findByName(it.nombreJugador.toString())
                        val equipoId = futbolistaVendido?.equipoId
                        futbolistaVendido?.equipoId = null
                        db?.futbolistaDao()?.update(futbolistaVendido)

                        val equipo : Equipo? = db?.equipoDao()?.findById(equipoId)
                        equipo?.presupuesto = equipo?.presupuesto!! + futbolistaVendido?.varor!!

                        db?.equipoDao()?.update(equipo)

                        val actividadVenta = Actividad(
                            actividadId = null,
                            accion = AccionActividad.VENDER_FUTBOLISTA,
                            usuarioActividad = usuarioConectado?.userId,
                            futbolistaActividad = futbolistaVendido.futbolistaId,
                            ligaActividad = null,
                            jornadaActividad = null
                        )
                        db?.actividadDao()?.insertar(actividadVenta)

                        val navController = findNavController()
                        navController.navigate(R.id.action_plantillaFragment_to_equipoFragment)
                    }
                }
            )
            with(binding) {
                rvFutbolistasList.layoutManager = LinearLayoutManager(context)
                rvFutbolistasList.adapter = adapter
            }
            binding.tvEncimaRecyclerView.text = "Pulsa en la imagen de cada jugador para ver sus estadísticas"
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
}