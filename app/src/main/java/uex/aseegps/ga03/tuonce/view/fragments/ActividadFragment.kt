package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import uex.aseegps.ga03.tuonce.view.adapters.ActividadAdapter
import uex.aseegps.ga03.tuonce.databinding.FragmentActividadBinding
import uex.aseegps.ga03.tuonce.view.viewmodels.ActividadViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel

class ActividadFragment : Fragment() {
    private var _binding: FragmentActividadBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ActividadViewModel by viewModels { ActividadViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var adapter: ActividadAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentActividadBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
            adapter.updateDataUser(user)
        }

        setUpRecyclerView()
        subscribeUi(adapter)

        binding.rvActividad.layoutManager = LinearLayoutManager(requireContext())
        binding.rvActividad.adapter = adapter
    }


    private fun subscribeUi(adapter: ActividadAdapter) {
        viewModel.actividades.observe(viewLifecycleOwner) { actividadesTotales ->
            adapter.updateData(actividadesTotales)
        }
    }

    private fun setUpRecyclerView() {
        adapter = ActividadAdapter(emptyList(),
            context,
            lifecycleScope,
            viewModel.user?.name )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}