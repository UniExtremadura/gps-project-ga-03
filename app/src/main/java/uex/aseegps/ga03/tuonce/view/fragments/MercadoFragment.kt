package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentMercadoBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers.clasificarJugadores
import uex.aseegps.ga03.tuonce.view.adapters.MercadoAdapter
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.MercadoViewModel


class MercadoFragment : Fragment() {
    private lateinit var binding: FragmentMercadoBinding

    private lateinit var adapter: MercadoAdapter

    private val viewModel : MercadoViewModel by viewModels { MercadoViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMercadoBinding.inflate(inflater, container, false)
        setUpListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
        }

        setUpRecyclerView()
        subscribeUi(adapter)

        binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())
        binding.RvFutbolista.adapter = adapter
    }

    private fun subscribeUi(adapter: MercadoAdapter) {
        viewModel.futbolistas.observe(viewLifecycleOwner) { futbolistasActualesMercado ->
            adapter.updateData(futbolistasActualesMercado.filter{
                it.equipoId == null
            })
        }
        viewModel.equipoUsuario.observe(viewLifecycleOwner) {}
    }

    private fun setUpRecyclerView() {
        adapter = MercadoAdapter(
            lista = emptyList(),
            contexto = requireContext(),
            onClick = {
                comprarFutbolistaListener(it)
            })
    }



    private fun setUpListeners() {
        binding.ordenarPorPuntuacion.setOnClickListener {
            ordenarPorPuntuacion()
        }
    }

    private fun ordenarPorPuntuacion()
    {
        lifecycleScope.launch {
            val jugadoresOrdenados = clasificarJugadores(viewModel.obtenerJugadoresOrdenadosPorPuntuacion())
            binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())
            binding.RvFutbolista.adapter = MercadoAdapter(
                lista = jugadoresOrdenados,
                contexto = requireContext(),
                onClick = {
                    comprarFutbolistaListener(it)
                }
            )
        }
    }


    private fun comprarFutbolistaListener(it : Futbolista)
    {
        lifecycleScope.launch {
            if (!viewModel.comprarFutbolista(it)){
                Toast.makeText(
                    requireContext(),
                    "No tienes suficiente dinero",
                    Toast.LENGTH_SHORT
                ).show()
            }
            salirDelMercado()
        }
    }
    private suspend fun salirDelMercado(){
        withContext(Dispatchers.Main) {
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.mercadoFragment, true).build()
            findNavController().navigate(
                R.id.action_mercadoFragment_to_equipoFragment,
                null,
                navOptions
            )
        }
    }
}
