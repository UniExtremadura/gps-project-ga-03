package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.databinding.FragmentCrearLigaPersonalizadaBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CrearLigaPersonalizada.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearLigaPersonalizada : Fragment() {
    private var _binding: FragmentCrearLigaPersonalizadaBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: TuOnceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!

    }

    private fun setUpListeners() {
        with(binding){
            btnConfirmarLigaP.setOnClickListener {
                crearLiga()
            }
        }
    }

    private fun seleccionar11Jugadores(): List<Futbolista> {
        // Obtén la lista de futbolistas barajada aleatoriamente
        val futbolistasBarajados = dummyFutbolista.shuffled()

        // Toma los primeros 11 jugadores de la lista barajada
        val onceJugadores = futbolistasBarajados.take(11)

        return onceJugadores
    }
    private fun crearLiga() {
        with(binding) {
            val nombreLiga = editTextNombreLiga.text.toString()
            val numPartidosStr = numPartidos.text.toString()
            val numPartidos = numPartidosStr.toIntOrNull() // Convierte a entero y maneja posibles entradas no numéricas

            if (nombreLiga.isNotBlank() && numPartidos != null) {
                val nuevaLiga = Liga(null, nombreLiga, numPartidos, 0)
                // Mostrar por consola el nombre de la liga y el número de partidos
                println("Nombre de la liga: ${nuevaLiga.name}")
                println("Número de partidos: ${nuevaLiga.partidos}")
                lifecycleScope.launch {
                    val usuarioConectado: User? = recuperarUsuario()
                    nuevaLiga.userId = usuarioConectado?.userId
                    val idLiga = db.ligaDao().insertarLiga(nuevaLiga)
                    val equipo: Equipo? = recuperarEquipo(usuarioConectado)
                    equipo?.ligaId = idLiga
                    equipo?.let { db.equipoDao().update(it) }

                    val bots: List<User> = listOf(
                        User(null, R.drawable.ic_launcher_background, "Bot1", "Bot1", 0),
                        User(null, R.drawable.ic_launcher_background, "Bot2", "Bot2", 0),
                        User(null, R.drawable.ic_launcher_background, "Bot3", "Bot3", 0)
                    )

                    bots.forEach { bot ->
                        val id = db.userDao().insert(bot)
                        crearEquipoEnLiga(bot, id, idLiga)
                    }

                    findNavController().navigate(R.id.action_crearLigaPersonalizada_to_misLigasFragment)
                }
            } else {
                Toast.makeText(context, "Por favor, ingresa un nombre y un número de partidos válidos.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun crearEquipoEnLiga(user : User, id : Long?, idLiga: Long?){
        val nuevoEquipo : Equipo = Equipo(
            null,
            name = user.name,
            userId = id,
            presupuesto = 1000000,
            ligaId = idLiga
        )
        lifecycleScope.launch{
            val equipoId = db?.equipoDao()?.insert(nuevoEquipo)
            val onceJugadores = seleccionar11Jugadores()
            onceJugadores.forEach {
                it.equipoId = equipoId
                it.estaEnel11 = 1
                db?.futbolistaDao()?.insert(it)
                // Mostrar por consola el nombre de los jugadores y el id del equipo
                println("Nombre del jugador: ${it.nombreJugador} , Id del equipo: ${equipoId}")
            }
            // Mostrar por consola el id del equipo del usuario y el de la liga
            println("Liga ${nuevoEquipo.ligaId} , Id del equipo del usuario: ${equipoId}")
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrearLigaPersonalizadaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        // Obtén el NavController
        val navController = findNavController()

        // Configura el OnClickListener para el botón de retroceso
        view.findViewById<View>(R.id.btnAtrasLigaP).setOnClickListener {
            // Navega hacia atrás cuando se hace clic en el botón
            navController.navigateUp()
        }
    }
}