package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uex.aseegps.ga03.tuonce.view.adapters.ClasificacionAdapter
import uex.aseegps.ga03.tuonce.databinding.FragmentClasificacionBinding
import uex.aseegps.ga03.tuonce.view.viewmodels.ClasificacionViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel

class ClasificacionFragment : Fragment() {

    private var _binding: FragmentClasificacionBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ClasificacionViewModel by viewModels { ClasificacionViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var adapter: ClasificacionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClasificacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpRecyclerView() {
        adapter = ClasificacionAdapter(
                emptyList()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
        }

        setUpRecyclerView()
        subscribeUi(adapter)

        binding.rvClasificacion.layoutManager = LinearLayoutManager(requireContext())
        binding.rvClasificacion.adapter = adapter
        binding.tvNombreLiga.text = "No hay liga activa"
    }

    private fun subscribeUi(adapter: ClasificacionAdapter) {
        viewModel.ligaUsuario.observe(viewLifecycleOwner) { ligaActiva ->
            if (ligaActiva != null) {
                viewModel.initializeLiga(ligaActiva.ligaId!!)
                binding.tvNombreLiga.text = ligaActiva?.name ?: "Liga sin nombre"
            }
            else {
                binding.tvNombreLiga.text = ligaActiva?.name ?: "No hay liga activa"
            }
        }

        viewModel.usuariosLiga.observe(viewLifecycleOwner) { usuarios ->
            var listaUsuarios = usuarios.distinctBy{it.userId }.sortedByDescending { it.points }
            adapter.updateData(listaUsuarios)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
