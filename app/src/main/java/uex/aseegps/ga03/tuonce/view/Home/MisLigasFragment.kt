package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
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
            lifecycleScope.launch {
                val usuarioConectado = recuperarUsuario()
                val bot1 = recuperarUsuarioPorNombre("Bot1")
                val bot2 = recuperarUsuarioPorNombre("Bot2")
                val bot3 = recuperarUsuarioPorNombre("Bot3")

                val equipoUsuario = recuperarEquipo(usuarioConectado)
                val equipoBot1 = recuperarEquipo(bot1)
                val equipoBot2 = recuperarEquipo(bot2)
                val equipoBot3 = recuperarEquipo(bot3)

                if (equipoUsuario != null && equipoBot1 != null && equipoBot2 != null && equipoBot3 != null) {
                    val equipos = listOf(equipoBot1, equipoBot2, equipoBot3)

                    val equiposMezclados = equipos.shuffled()

                    val equipoBotContraUsuario = equiposMezclados.first()

                    val partidoRestante = equiposMezclados.drop(1)

                    simularPartido(recuperarFutbolistas(equipoUsuario), recuperarFutbolistas(equipoBotContraUsuario))
                    simularPartido(recuperarFutbolistas(partidoRestante[0]), recuperarFutbolistas(partidoRestante[1]))

                    if (usuarioConectado != null && bot1 != null && bot2 != null && bot3 != null) {
                        calcularPuntuacionUsuario(usuarioConectado)
                        calcularPuntuacionUsuario(bot1)
                        calcularPuntuacionUsuario(bot2)
                        calcularPuntuacionUsuario(bot3)
                    }

                }

            }
            findNavController().navigate(R.id.action_misLigasFragment_to_clasificacionFragment)
        }
    }

    private suspend fun recuperarUsuario(): User? {
        return withContext(Dispatchers.IO) {
            db.userDao().obtenerUsuarioConectado()
        }
    }

    private suspend fun recuperarUsuarioPorNombre(nombre: String): User? {
        return withContext(Dispatchers.IO) {
            db.userDao().findByName(nombre)
        }
    }

    private suspend fun recuperarEquipo(usuario: User?): Equipo? {
        return withContext(Dispatchers.IO) {
            usuario?.userId?.let { db.equipoDao().findByUserId(it) }
        }
    }

    private suspend fun recuperarFutbolistas(equipo: Equipo?): List<Futbolista> {
        return withContext(Dispatchers.IO) {
            equipo?.equipoId?.let { equipoId ->
                db?.futbolistaDao()?.findByEquipoId(equipoId) ?: emptyList()
            } ?: emptyList()
        }
    }

    private fun mostrarBotonLiga(liga: Long?) {
        with(binding) {
            if (liga == null) {
                btnCrearLiga.visibility = View.VISIBLE
                btnSimularPartido.visibility = View.GONE
            } else {
                btnCrearLiga.visibility = View.GONE
                btnSimularPartido.visibility = View.VISIBLE
            }
        }
    }

    private fun simularPartido(equipoLocal: List<Futbolista>, equipoVisitante: List<Futbolista>) {

        val futbolistasLocal = equipoLocal.shuffled().take(5)
        val futbolistasVisitante = equipoVisitante.shuffled().take(5)

        for (futbolista in futbolistasLocal) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()

            futbolista.puntosAportados += calcularPuntuacion(futbolista)
        }

        for (futbolista in futbolistasVisitante) {
            futbolista.goles += (0..2).random()
            futbolista.asistencias += (0..2).random()
            futbolista.tarjetaRoja += (0..1).random()
            futbolista.tarjetaAmarilla += (0..2).random()
            futbolista.parada += (0..1).random()

            futbolista.puntosAportados += calcularPuntuacion(futbolista)
        }

        val futbolistas = futbolistasLocal + futbolistasVisitante

        lifecycleScope.launch {
            for(futbolista in futbolistas) {
                db?.futbolistaDao()?.update(futbolista)
            }
        }
    }

    private fun calcularPuntuacionUsuario(usuario: User){
        lifecycleScope.launch {
            val equipo = recuperarEquipo(usuario)
            val futbolistas = recuperarFutbolistas(equipo)

            for (futbolista in futbolistas) {

                usuario.points += futbolista.puntosAportados
            }
            val id = usuario.userId
            if (id != null) {
                db?.userDao()?.updatePoints(id, usuario.points)
            }

        }

    }



}