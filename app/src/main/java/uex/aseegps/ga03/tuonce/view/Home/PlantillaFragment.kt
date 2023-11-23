package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.databinding.FragmentPlantillaBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlantillaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantillaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var db: TuOnceDatabase

    private var _binding: FragmentPlantillaBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlantillaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        }

    private fun setUpRecyclerView() {
        var futbolistasDelEquipo = mutableListOf<Futbolista>()
        var futbolistasDelEquipo2 = mutableListOf<Futbolista>()
        val context = this.context
        lifecycleScope?.launch {
            var futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()
            val equipo: Equipo? = recuperarEquipo(recuperarUsuario())
            futbolistas?.forEach {
                if ((it.equipoId == equipo?.equipoId) and (it.estaEnel11 == 0)) {
                    futbolistasDelEquipo.add(it)
                }
            }
            adapter = PlantillaAdapter(
                lista = futbolistasDelEquipo,
                contexto = context,
                viewLifecycleOwner.lifecycleScope
            )
            with(binding) {
                rvFutbolistasList.layoutManager = LinearLayoutManager(context)
                rvFutbolistasList.adapter = adapter
            }
            binding.tvEncimaRecyclerView.text = "Jugador no alineado"
        }
        lifecycleScope?.launch {
            var futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()
            val equipo: Equipo? = recuperarEquipo(recuperarUsuario())
            futbolistas?.forEach {
                if ((it.equipoId == equipo?.equipoId) and (it.estaEnel11 == 1)) {
                    futbolistasDelEquipo2.add(it)
                }
            }
            adapter = PlantillaAdapter(
                lista = futbolistasDelEquipo2,
                contexto = context,
                viewLifecycleOwner.lifecycleScope
            )
            with(binding) {
                rvFutbolistasList2.layoutManager = LinearLayoutManager(context)
                rvFutbolistasList2.adapter = adapter
            }
            binding.tvEncimaRecyclerView3.text = "Jugadores Alineados"
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlantillaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlantillaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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