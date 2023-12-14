package uex.aseegps.ga03.tuonce.view.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.view.adapters.PlantillaAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentPlantillaBinding
import uex.aseegps.ga03.tuonce.utils.SortPlayers.clasificarJugadores
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.MercadoViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.PlantillaViewModel

class PlantillaFragment : Fragment() {

    private var _binding: FragmentPlantillaBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlantillaAdapter

    private val viewModel : PlantillaViewModel by viewModels { PlantillaViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlantillaBinding.inflate(inflater, container, false)

        return binding.root
    }


    private fun subscribeUi() {
        viewModel.equipoUsuario.observe(viewLifecycleOwner){
            equipo -> viewModel.initializeEquipo()
        }
        viewModel.futbolistasDelEquipoUsuario.observe(viewLifecycleOwner) { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
        }

        setUpListeners()
        subscribeUi()
        setUpRecyclerView(null)
    }


    private fun setUpListeners() {
        binding.alineacionBt.setOnClickListener {
            findNavController().navigate(R.id.action_plantillaFragment_to_equipoFragment)
        }
        binding.buttonOrdenarPuntos.setOnClickListener {
            lifecycleScope.launch {
                viewModel.initialize()
                adapter.updateData( clasificarJugadores(viewModel.futbolistasDelEquipoUsuario.value!!))
                adapter.notifyDataSetChanged()
            }
        }
        binding.btnPortero.setOnClickListener {
            setUpRecyclerView("Portero")
            modficarColorFondo(binding.btnPortero)
        }
        binding.btnDefensa.setOnClickListener {
            setUpRecyclerView("Defensa")
            modficarColorFondo(binding.btnDefensa)
        }
        binding.btnCentrocampista.setOnClickListener {
            setUpRecyclerView("Centrocampista")
            modficarColorFondo(binding.btnCentrocampista)
        }
        binding.btnDelantero.setOnClickListener {
            setUpRecyclerView("Delantero")
            modficarColorFondo(binding.btnDelantero)
        }
        binding.plantillaBt.setOnClickListener {
            setUpRecyclerView(null)
            modficarColorFondo(null)
        }
    }


    private fun setUpRecyclerView(posicion: String?) {
        val context = this.context
        lifecycleScope?.launch {
            val lista_ordenados = viewModel.obtenerFutbolistasDelEquipoOrdenados(posicion)

            adapter = PlantillaAdapter(
                lista = lista_ordenados,
                contexto = context,
                viewLifecycleOwner.lifecycleScope,
                onClick = {
                        vender(it)
                }
            )
            binding.rvFutbolistasList.layoutManager = LinearLayoutManager(context)
            binding.rvFutbolistasList.adapter = adapter
            binding.tvEncimaRecyclerView.text = "Pulsa en un jugador para ver sus estadísticas"
        }
    }

    private fun vender (it : Futbolista){
        lifecycleScope.launch {
            viewModel.venderFutbolistaDelequipo(it, viewModel.equipoUsuario.value,
                viewModel.user)
            navegarAEquipo()
        }
    }


    private fun modficarColorFondo(selectedButton: Button?) {
        binding.btnPortero.setBackgroundColor(Color.WHITE)
        binding.btnDefensa.setBackgroundColor(Color.WHITE)
        binding.btnCentrocampista.setBackgroundColor(Color.WHITE)
        binding.btnDelantero.setBackgroundColor(Color.WHITE)

        selectedButton?.setBackgroundColor(Color.RED)
    }

    private fun navegarAEquipo(){
        val navController = findNavController()
        navController.navigate(R.id.action_plantillaFragment_to_equipoFragment)
    }
}