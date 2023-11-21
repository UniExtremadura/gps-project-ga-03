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

        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding){
            btnConfirmar.setOnClickListener {
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
        with(binding){
             var nuevaliga : Liga = Liga (
                 null, editTextNombreLiga.toString(), numPartidos.toString().toInt()
             )
            lifecycleScope.launch {
                val idliga = db?.ligaDao()?.insertarLiga(nuevaliga)
                val usuarioConectado: User? = recuperarUsuario()
                val equipo : Equipo? = recuperarEquipo(usuarioConectado)
                equipo?.ligaId = idliga
                db?.equipoDao()?.update(equipo)
                val bots : List<User> = listOf(
                    User(null, "Bot1", "Bot1", 0),
                    User(null, "Bot2", "Bot2", 0),
                    User(null, "Bot3", "Bot3", 0)
                )
                for (bot in bots) {
                    val id = db?.userDao()?.insert(bot)
                    crearEquipoEnLiga(bot, id, idliga)
                }
            }


        }
    }

    private fun crearEquipoEnLiga(user : User, id : Long?, idLiga: Long?){
        val nuevoEquipo : Equipo = Equipo(
            null,
            name = user.name,
            userId = id,
            ligaId = idLiga
        )
        lifecycleScope.launch{
            val equipoId = db?.equipoDao()?.insert(nuevoEquipo)
            val onceJugadores = seleccionar11Jugadores()
            onceJugadores.forEach {
                it.equipoId = equipoId
                db?.futbolistaDao()?.insert(it)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_liga_personalizada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén el NavController
        val navController = findNavController()

        // Configura el OnClickListener para el botón de retroceso
        view.findViewById<View>(R.id.btnAtras).setOnClickListener {
            // Navega hacia atrás cuando se hace clic en el botón
            navController.navigateUp()
        }
    }
}