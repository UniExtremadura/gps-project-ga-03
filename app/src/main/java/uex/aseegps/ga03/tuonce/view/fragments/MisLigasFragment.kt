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
            simularPartidosYActualizar()
        }
        binding.btnTerminarLiga.setOnClickListener {
            terminarLiga()
        }
    }

    private fun simularPartidosYActualizar() {
        lifecycleScope.launch {
            val usuarioConectado = viewModel.user
            val bot1 = viewModel.bot1.value
            val bot2 = viewModel.bot2.value
            val bot3 = viewModel.bot3.value

            val equipoUsuario = viewModel.equipoUsuario.value
            val equipoBot1 = viewModel.equipoBot1.value
            val equipoBot2 = viewModel.equipoBot2.value
            val equipoBot3 = viewModel.equipoBot3.value

            if (equipoUsuario != null && equipoBot1 != null && equipoBot2 != null && equipoBot3 != null) {
                val equipos = listOf(equipoBot1, equipoBot2, equipoBot3)

                val equiposMezclados = equipos.shuffled()

                val equipoBotContraUsuario = equiposMezclados.first()
                val partidoRestante = equiposMezclados.drop(1)


                simularPartido(
                    viewModel.futbolistasDelEquipoUsuario.value!!,
                    viewModel.futbolistasEquipoBot1.value!!
                )
                simularPartido(
                    viewModel.futbolistasEquipoBot2.value!!,
                    viewModel.futbolistasEquipoBot3.value!!
                )

                if (usuarioConectado != null && bot1 != null && bot2 != null && bot3 != null) {
                    calcularPuntuacionUsuario(usuarioConectado)
                    calcularPuntuacionUsuario(bot1)
                    calcularPuntuacionUsuario(bot2)
                    calcularPuntuacionUsuario(bot3)
                }

                // Crear notificacion de que se ha simulado la jornada
                Toast.makeText(requireContext(), "Jornada simulada", Toast.LENGTH_SHORT).show()

                // Actualizar jornada
                jornada += 1

                viewModel.marcarActividadCrearLiga(jornada)
                // Actualizar boton de liga
                mostrarBotonLiga()
            }
        }
    }

    private fun terminarLiga() {
        lifecycleScope.launch {
            val usuarioConectado = viewModel.user
            // recuperar bots
            val bot1 = viewModel.bot1.value
            val bot2 = viewModel.bot2.value
            val bot3 = viewModel.bot3.value
            // recuperar equipos
            val equipo = viewModel.equipoUsuario.value
            val equipoBot1 = viewModel.equipoBot1.value
            val equipoBot2 = viewModel.equipoBot2.value
            val equipoBot3 = viewModel.equipoBot3.value

            // Recuperar futbolistas
            val futbolistasUsuario = viewModel.futbolistasDelEquipoUsuario.value
            val futbolistasBot1 = viewModel.futbolistasEquipoBot1.value
            val futbolistasBot2 = viewModel.futbolistasEquipoBot2.value
            val futbolistasBot3 = viewModel.futbolistasEquipoBot3.value

            // Actualizar puntos de usuario conectado en la base de datos
            usuarioConectado?.points = 0
            viewModel.actualizarPuntos(usuarioConectado?.points)

            // Borrar bots
            for (bot in listOf(bot1, bot2, bot3)) {
                if (bot != null) {
                    viewModel.eliminarUsuario(bot.userId!!)
                }
            }

            // Borrar Liga
            val liga = viewModel.ligaUsuario
            viewModel.marcarActividadTerminarLiga(liga?.value?.name)

            if (liga != null) {
                viewModel.eliminarLiga()
                equipo?.ligaId = null
                viewModel.actualizarEquipo(equipo)
            }

            for (equipo in listOf(equipoBot1, equipoBot2, equipoBot3)) {
                viewModel.eliminarEquipo(equipo!!)
            }


            // Borrar referencias a equipo en futbolistas
            for (futbolista in listOf(futbolistasUsuario, futbolistasBot1, futbolistasBot2, futbolistasBot3)) {
                if (futbolista != null) {
                    for (fut in futbolista) {
                        fut.goles = 0
                        fut.asistencias = 0
                        fut.tarjetaRoja = 0
                        fut.tarjetaAmarilla = 0
                        fut.parada = 0
                        fut.balonAlArea = 0
                        fut.faltacometidas = 0
                        fut.minutoJugados = 0
                        fut.puntosAportados = 0
                        viewModel.actualizarFutbolista(fut)
                    }
                }
            }
            for (futbolista in listOf(futbolistasBot1, futbolistasBot2, futbolistasBot3)) {
                if (futbolista != null) {
                    for (fut in futbolista) {
                        fut.equipoId = null
                        viewModel.actualizarFutbolista(fut)
                    }
                }
            }


            // Restablecer jornada
            jornada = 1

            // Crear notificacion de que se ha terminado la liga
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

                // Mostrar el numero de noticias por consola
                Log.d("MisLigasFragment", "Numero de noticias: ${notices.size}")

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

    private fun simularPartido(equipoLocal: List<Futbolista>, equipoVisitante: List<Futbolista>) {

        // Mensaje de equipos recibidos en consola y cuales son
        Log.d("MisLigasFragment", "4. Equipos recibidos: $equipoLocal y $equipoVisitante")
        var listaLocal = mutableListOf<Futbolista>()
        val listaVisitante = mutableListOf<Futbolista>()
        for (futbolista in equipoLocal) {
            if(futbolista.estaEnel11 == 1){
                listaLocal.add(futbolista)
            }
        }
        for (futbolista in equipoVisitante) {
            if(futbolista.estaEnel11 == 1){
                listaVisitante.add(futbolista)
            }
        }

        for (futbolista in listaLocal) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()
            futbolista.balonAlArea += (0..3).random()
            futbolista.faltacometidas += (0..3).random()
            futbolista.minutoJugados = (70..90).random()

            futbolista.puntosAportados += calcularPuntuacion(futbolista)

            // Mensaje de puntos locales calculados en consola y el valor de los puntos
            Log.d("MisLigasFragment", "4. Puntos locales calculados: ${futbolista.puntosAportados}")
        }

        for (futbolista in listaVisitante) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()
            futbolista.balonAlArea += (0..3).random()
            futbolista.faltacometidas += (0..3).random()
            futbolista.minutoJugados += (5..90).random()

            futbolista.puntosAportados += calcularPuntuacion(futbolista)

            // Mensaje de puntos visitantes calculados escrito en consola y el valor de los puntos
            Log.d("MisLigasFragment", "4. Puntos visitantes calculados: ${futbolista.puntosAportados}")
        }

        val futbolistas = listaLocal + listaVisitante

        for(futbolista in futbolistas) {
            viewModel.actualizarFutbolista(futbolista)
        }

        // Mensaje de puntos de futbolistas actualizados escrito en consola y el valor de los puntos
        Log.d("MisLigasFragment", "4. Puntos de futbolistas actualizados: ${futbolistas}")
    }

    private suspend fun calcularPuntuacionUsuario(usuario: User){
        lifecycleScope.launch {
            val futbolistas = viewModel.futbolistasDelEquipoUsuario.value

            if (futbolistas != null) {
                for (futbolista in futbolistas) {
                    usuario.points += futbolista.puntosAportados
                }
            }

            // Mensaje de puntos de usuario actualizados escrito en consola y el valor de los puntos
            Log.d("MisLigasFragment", "7. Puntos de usuario actualizados: ${usuario.points}")

            val id = usuario.userId
            if (id != null) {
                viewModel.actualizarPuntos(usuario.points)
            }

            // Mensaje de puntos de usuario actualizados en la base de datos escrito en consola y el valor de los puntos
            Log.d("MisLigasFragment", "7. Puntos de usuario actualizados en la base de datos: ${usuario.points}")
        }
    }



}