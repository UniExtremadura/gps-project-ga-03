package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.databinding.ActivityJoinBinding
import uex.aseegps.ga03.tuonce.databinding.FragmentMisLigasBinding
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentEquipoBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers.calcularPuntuacion

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MisLigasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisLigasFragment : Fragment() {
    // ... [otras variables] ...

    private var _binding: FragmentMisLigasBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TuOnceDatabase
    private var jornada = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMisLigasBinding.inflate(inflater, container, false)
        cargarDatos()
        setUpListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        cargarDatos()  // Recargar datos al regresar al fragmento
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
            // Mostrar todos los usuarios en consola
            Log.d("MisLigasFragment", "1. Todos los usuarios: ${db.userDao().getAllUsers()}")
            val usuarioConectado = recuperarUsuario()
            val equipo = recuperarEquipo(usuarioConectado)
            val ligaid = equipo?.ligaId
            mostrarBotonLiga(ligaid)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val usuarioConectado = recuperarUsuario()
            val bot1 = recuperarUsuarioPorNombre("Bot1")
            val bot2 = recuperarUsuarioPorNombre("Bot2")
            val bot3 = recuperarUsuarioPorNombre("Bot3")

            // Mensaje de bots recuperados en consola
            Log.d("MisLigasFragment", "1. Bots recuperados")

            val equipoUsuario = recuperarEquipo(usuarioConectado)
            val equipoBot1 = recuperarEquipo(bot1)
            val equipoBot2 = recuperarEquipo(bot2)
            val equipoBot3 = recuperarEquipo(bot3)

            // Mensaje de equipos recuperados en consola
            Log.d("MisLigasFragment", "2. Equipos recuperados")

            if (equipoUsuario != null && equipoBot1 != null && equipoBot2 != null && equipoBot3 != null) {
                val equipos = listOf(equipoBot1, equipoBot2, equipoBot3)

                val equiposMezclados = equipos.shuffled()

                val equipoBotContraUsuario = equiposMezclados.first()
                val partidoRestante = equiposMezclados.drop(1)

                // Mensaje de equipos mezclados en consola
                Log.d("MisLigasFragment", "3. Equipos mezclados")

                simularPartido(
                    recuperarFutbolistas(equipoUsuario),
                    recuperarFutbolistas(equipoBotContraUsuario)
                )
                simularPartido(
                    recuperarFutbolistas(partidoRestante[0]),
                    recuperarFutbolistas(partidoRestante[1])
                )

                // Mensaje de partidos simulados en consola
                Log.d("MisLigasFragment", "Partidos simulados")

                if (usuarioConectado != null && bot1 != null && bot2 != null && bot3 != null) {
                    calcularPuntuacionUsuario(usuarioConectado)
                    calcularPuntuacionUsuario(bot1)
                    calcularPuntuacionUsuario(bot2)
                    calcularPuntuacionUsuario(bot3)
                }

                // Mensaje de puntuacion calculada en consola
                Log.d("MisLigasFragment", "Puntuacion calculada")

                // Crear notificacion de que se ha simulado el partido
                Toast.makeText(requireContext(), "Partido simulado", Toast.LENGTH_SHORT).show()

                // Actualizar jornada
                jornada += 1

                // Actualizar boton de liga
                cargarDatos()
            }
        }
    }

    private fun terminarLiga() {
        lifecycleScope.launch {
            val usuarioConectado = recuperarUsuario()
            // recuperar bots
            val bot1 = recuperarUsuarioPorNombre("Bot1")
            val bot2 = recuperarUsuarioPorNombre("Bot2")
            val bot3 = recuperarUsuarioPorNombre("Bot3")
            // recuperar equipos
            val equipo = recuperarEquipo(usuarioConectado)
            val equipoBot1 = recuperarEquipo(bot1)
            val equipoBot2 = recuperarEquipo(bot2)
            val equipoBot3 = recuperarEquipo(bot3)

            // Recuperar futbolistas
            val futbolistasUsuario = recuperarFutbolistas(equipo)
            val futbolistasBot1 = recuperarFutbolistas(equipoBot1)
            val futbolistasBot2 = recuperarFutbolistas(equipoBot2)
            val futbolistasBot3 = recuperarFutbolistas(equipoBot3)

            // Actualizar puntos de usuario conectado en la base de datos
            usuarioConectado?.points = 0
            db.userDao().updatePoints(usuarioConectado!!.userId!!, usuarioConectado.points)

            // Borrar bots
            for (bot in listOf(bot1, bot2, bot3)) {
                if (bot != null) {
                    db.userDao().delete(bot.userId!!)
                }
            }

            // Borrar Liga
            val ligaid = equipo?.ligaId
            val liga = ligaid?.let { db.ligaDao().obtenerLigaPorId(it) }
            if (liga != null) {
                db.ligaDao().eliminarLiga(liga.ligaId!!)
            }

            // Borrar referencias a liga en equipos
            for (equipo in listOf(equipo, equipoBot1, equipoBot2, equipoBot3)) {
                if (equipo != null) {
                    equipo.ligaId = null
                    db.equipoDao().update(equipo)
                }
            }

            // Borrar referencias a equipo en futbolistas
            for (futbolista in listOf(futbolistasBot1, futbolistasBot2, futbolistasBot3)) {
                for (fut in futbolista) {
                    fut.equipoId = null
                    fut.goles = 0
                    fut.asistencias = 0
                    fut.tarjetaRoja = 0
                    fut.tarjetaAmarilla = 0
                    fut.parada = 0
                    fut.balonAlArea = 0
                    fut.faltacometidas = 0
                    fut.minutoJugados = 0
                    fut.puntosAportados = 0
                    db.futbolistaDao().update(fut)
                }
            }

            // Restablecer jornada
            jornada = 1

            // Crear notificacion de que se ha terminado la liga
            Toast.makeText(requireContext(), "Liga terminada", Toast.LENGTH_SHORT).show()

            cargarDatos()
        }
    }

    private suspend fun recuperarUsuario(): User? {
        return withContext(Dispatchers.Main) {
            db.userDao().obtenerUsuarioConectado()
        }
    }

    private suspend fun recuperarUsuarioPorNombre(nombre: String): User? {
        return withContext(Dispatchers.Main) {
            db.userDao().findByName(nombre)
        }
    }

    private suspend fun recuperarEquipo(usuario: User?): Equipo? {
        return withContext(Dispatchers.Main) {
            usuario?.userId?.let { db.equipoDao().findByUserId(it) }
        }
    }

    private suspend fun recuperarFutbolistas(equipo: Equipo?): List<Futbolista> {
        return withContext(Dispatchers.Main) {
            equipo?.equipoId?.let { equipoId ->
                db?.futbolistaDao()?.findByEquipoId(equipoId) ?: emptyList()
            } ?: emptyList()
        }
    }

    private suspend fun mostrarBotonLiga(liga: Long?) {
        var liga = db.ligaDao().obtenerLigaPorId(liga ?: 0)
        var numPartidos = liga?.partidos ?: 0

        //Mostrar por consola el id de la liga y el numero de partidos
        Log.d("MisLigasFragment", "Liga: $liga y numero de partidos: $numPartidos")

        //Mostrar por consola el numero de jornada
        Log.d("MisLigasFragment", "Jornada: $jornada")

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

    private suspend fun simularPartido(equipoLocal: List<Futbolista>, equipoVisitante: List<Futbolista>) {

        // Mensaje de equipos recibidos en consola y cuales son
        Log.d("MisLigasFragment", "4. Equipos recibidos: $equipoLocal y $equipoVisitante")

        val futbolistasLocal = equipoLocal.shuffled().take(5)
        val futbolistasVisitante = equipoVisitante.shuffled().take(5)

        // Mensaje de seleccionados 5 futbolistas de cada equipo en consola y cuales son
        Log.d("MisLigasFragment", "4. Seleccionados 5 futbolistas de cada equipo: $futbolistasLocal y $futbolistasVisitante")

        for (futbolista in futbolistasLocal) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()
            futbolista.balonAlArea += (0..3).random()
            futbolista.faltacometidas += (0..3).random()
            futbolista.minutoJugados += (5..90).random()

            futbolista.puntosAportados += calcularPuntuacion(futbolista)

            // Mensaje de puntos locales calculados en consola y el valor de los puntos
            Log.d("MisLigasFragment", "4. Puntos locales calculados: ${futbolista.puntosAportados}")
        }

        for (futbolista in futbolistasVisitante) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()

            futbolista.puntosAportados += calcularPuntuacion(futbolista)

            // Mensaje de puntos visitantes calculados escrito en consola y el valor de los puntos
            Log.d("MisLigasFragment", "4. Puntos visitantes calculados: ${futbolista.puntosAportados}")
        }

        val futbolistas = futbolistasLocal + futbolistasVisitante

        for(futbolista in futbolistas) {
                db?.futbolistaDao()?.update(futbolista)
        }

        // Mensaje de puntos de futbolistas actualizados escrito en consola y el valor de los puntos
        Log.d("MisLigasFragment", "4. Puntos de futbolistas actualizados: ${futbolistas}")
    }

    private suspend fun calcularPuntuacionUsuario(usuario: User){
        lifecycleScope.launch {
            val equipo = recuperarEquipo(usuario)
            val futbolistas = recuperarFutbolistas(equipo)

            for (futbolista in futbolistas) {
                usuario.points += futbolista.puntosAportados
            }

            // Mensaje de puntos de usuario actualizados escrito en consola y el valor de los puntos
            Log.d("MisLigasFragment", "7. Puntos de usuario actualizados: ${usuario.points}")

            val id = usuario.userId
            if (id != null) {
                db?.userDao()?.updatePoints(id, usuario.points)
            }

            // Mensaje de puntos de usuario actualizados en la base de datos escrito en consola y el valor de los puntos
            Log.d("MisLigasFragment", "7. Puntos de usuario actualizados en la base de datos: ${usuario.points}")
        }
    }



}