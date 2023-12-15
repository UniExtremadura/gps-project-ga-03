package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import uex.aseegps.ga03.tuonce.R
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers.calcularPuntuacion
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import uex.aseegps.ga03.tuonce.view.adapters.MisLigasAdapter
import uex.aseegps.ga03.tuonce.databinding.FragmentMisLigasBinding
import uex.aseegps.ga03.tuonce.api.RetrofitServiceFactory
import uex.aseegps.ga03.tuonce.view.adapters.ClasificacionAdapter
import uex.aseegps.ga03.tuonce.view.viewmodels.ClasificacionViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.MisLigasViewModel

class MisLigasFragment : Fragment() {

    private var _binding: FragmentMisLigasBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MisLigasViewModel by viewModels { MisLigasViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var jornada = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
        }

        subscribeUi()
        mostrarBotonLiga()
        fetchArticles()
    }

    private fun subscribeUi() {
        viewModel.ligaUsuario.observe(viewLifecycleOwner) { ligaActiva ->
            if (ligaActiva != null) {
                viewModel.initializeLiga(ligaActiva.ligaId!!)
            }
        }
        viewModel.equipoUsuario.observe(viewLifecycleOwner) {eq -> viewModel.initializeEquipo(eq.equipoId!!)}
        viewModel.futbolistasDelEquipoUsuario.observe(viewLifecycleOwner) {}
        viewModel.ligaUsuario.observe(viewLifecycleOwner) {mostrarBotonLiga()}
        viewModel.usuariosLiga.observe(viewLifecycleOwner) {mostrarBotonLiga()}
        viewModel.bot1.observe(viewLifecycleOwner) {}
        viewModel.bot2.observe(viewLifecycleOwner) {}
        viewModel.bot3.observe(viewLifecycleOwner) {}
        viewModel.equipoBot1.observe(viewLifecycleOwner) {}
        viewModel.equipoBot2.observe(viewLifecycleOwner) {}
        viewModel.equipoBot3.observe(viewLifecycleOwner) {}
        viewModel.futbolistasEquipoBot1.observe(viewLifecycleOwner) {}
        viewModel.futbolistasEquipoBot2.observe(viewLifecycleOwner) {}
        viewModel.futbolistasEquipoBot3.observe(viewLifecycleOwner) {}

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMisLigasBinding.inflate(inflater, container, false)
        binding.rvShowList.layoutManager = LinearLayoutManager(context)
        mostrarBotonLiga()
        setUpListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mostrarBotonLiga()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setUpListeners() {
        binding.btnCrearLiga.setOnClickListener {
            findNavController().navigate(R.id.action_misLigasFragment_to_crearLigaPersonalizada)
        }
        binding.btnSimularPartido.setOnClickListener {
            jornada += 1
            viewModel.simularPartidosYActualizar(jornada)
            Toast.makeText(requireContext(), "Jornada simulada", Toast.LENGTH_SHORT).show()
            mostrarBotonLiga()
        }
        binding.btnTerminarLiga.setOnClickListener {
            viewModel.terminarLiga()
            jornada = 1
            Toast.makeText(requireContext(), "Liga terminada", Toast.LENGTH_SHORT).show()
            mostrarBotonLiga()
        }
    }



    private fun fetchArticles() {
        lifecycleScope.launch {
            try {
                val service = RetrofitServiceFactory.makeRetrofitService()
                val remoteResource = service.listNoticias("sports", "ar", "49aa5dc1188e4486810c0f8cf239bc00")
                val notices = remoteResource.articles

                withContext(Dispatchers.Main) {
                    val adapter = MisLigasAdapter(notices)
                    binding.rvShowList.adapter = adapter
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun mostrarBotonLiga() {
        var liga = viewModel.ligaUsuario.value
        var numPartidos = liga?.partidos ?: 0

        with(binding) {
            if (liga == null) {
                btnCrearLiga.visibility = View.VISIBLE
                btnSimularPartido.visibility = View.GONE
                btnTerminarLiga.visibility = View.GONE
            }
            else if ( jornada <= numPartidos) {
                btnCrearLiga.visibility = View.GONE
                btnSimularPartido.visibility = View.VISIBLE
                btnSimularPartido.text = "Simular jornada $jornada"
                btnTerminarLiga.visibility = View.GONE
            }
            else {
                btnCrearLiga.visibility = View.GONE
                btnSimularPartido.visibility = View.GONE
                btnTerminarLiga.visibility = View.VISIBLE
            }
        }
    }






}