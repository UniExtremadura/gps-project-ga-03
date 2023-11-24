package uex.aseegps.ga03.tuonce.view.Home.Mercado

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentMercadoBinding
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers
import uex.aseegps.ga03.tuonce.utils.SortPlayers.clasificarJugadores


class MercadoFragment : Fragment() {
    private lateinit var binding: FragmentMercadoBinding
    private lateinit var db: TuOnceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMercadoBinding.inflate(inflater, container, false)
        setUpListeners()
        return binding.root
    }

    private fun setUpListeners() {
        binding.ordenarPorPuntuacion.setOnClickListener {
            var jugadoresLibres = mutableListOf<Futbolista>()
            lifecycleScope.launch {
                var futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()
                futbolistas?.forEach {
                    if (it.equipoId == null) {
                        jugadoresLibres.add(it)
                    }
                }
                val jugadoresOrdenados = clasificarJugadores(jugadoresLibres)
                binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())
                binding.RvFutbolista.adapter = AdaptadorFutbolista(
                    lista = jugadoresOrdenados,
                    contexto = requireContext(),
                    onClick = {
                        lifecycleScope.launch {

                            var futbolistaComprado: Futbolista? = it

                            //Obtengo el equipo del usuario
                            val usuarioConectado = recuperarUsuario()
                            val equipoUsuario: Equipo? = recuperarEquipo(usuarioConectado)

                            if (equipoUsuario?.presupuesto!! >= futbolistaComprado?.varor!!) {
                                // Pongo que el futbolista va a tener el equipo del usuario
                                futbolistaComprado?.equipoId = equipoUsuario?.equipoId
                                db?.futbolistaDao()?.update(futbolistaComprado)

                                // Reduzco el presupuesto del equipo con lo que se ha gastado
                                equipoUsuario?.presupuesto =
                                    equipoUsuario.presupuesto!! - futbolistaComprado.varor!!
                                db?.equipoDao()?.update(equipoUsuario)

                                val actividadCompra = Actividad(
                                    actividadId = null,
                                    accion = AccionActividad.COMPRAR_FUTBOLISTA,
                                    usuarioActividad = usuarioConectado?.userId,
                                    futbolistaActividad = futbolistaComprado.futbolistaId,
                                    ligaActividad = null,
                                    jornadaActividad = null
                                )
                                db?.actividadDao()?.insertar(actividadCompra)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No tienes suficiente dinero",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            withContext(Dispatchers.Main) {
                                val navOptions =
                                    NavOptions.Builder().setPopUpTo(R.id.mercadoFragment, true)
                                        .build()
                                findNavController().navigate(
                                    R.id.action_mercadoFragment_to_equipoFragment,
                                    null,
                                    navOptions
                                )
                            }
                        }

                    })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var jugadoresLibres = mutableListOf<Futbolista>()
        lifecycleScope?.launch {
            var futbolistas : List<Futbolista>? = db?.futbolistaDao()?.findAll()
            futbolistas?.forEach{
                if(it.equipoId == null){
                    jugadoresLibres.add(it)
                }
            }

            // Utiliza requireContext() para obtener el contexto
            binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())

            // Aquí puedes continuar configurando tu RecyclerView con el adaptador, etc.
            binding.RvFutbolista.adapter = AdaptadorFutbolista(
                lista = jugadoresLibres,
                contexto = requireContext(),
                onClick = {
                    lifecycleScope.launch {

                        var futbolistaComprado: Futbolista? = it

                        //Obtengo el equipo del usuario
                        val usuarioConectado = recuperarUsuario()
                        val equipoUsuario: Equipo? = recuperarEquipo(usuarioConectado)

                        if (equipoUsuario?.presupuesto!! >= futbolistaComprado?.varor!!) {
                            // Pongo que el futbolista va a tener el equipo del usuario
                            futbolistaComprado?.equipoId = equipoUsuario?.equipoId
                            db?.futbolistaDao()?.update(futbolistaComprado)

                            // Reduzco el presupuesto del equipo con lo que se ha gastado
                            equipoUsuario?.presupuesto =
                                equipoUsuario.presupuesto!! - futbolistaComprado.varor!!
                            db?.equipoDao()?.update(equipoUsuario)

                            val actividadCompra = Actividad(
                                actividadId = null,
                                accion = AccionActividad.COMPRAR_FUTBOLISTA,
                                usuarioActividad = usuarioConectado?.userId,
                                futbolistaActividad = futbolistaComprado.futbolistaId,
                                ligaActividad = null,
                                jornadaActividad = null
                            )
                            db?.actividadDao()?.insertar(actividadCompra)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No tienes suficiente dinero",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

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

                })
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


