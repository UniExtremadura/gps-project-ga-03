package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentMercadoBinding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User
import uex.aseegps.ga03.tuonce.utils.SortPlayers.clasificarJugadores
import uex.aseegps.ga03.tuonce.adapter.MercadoAdapter


class MercadoFragment : Fragment() {
    private lateinit var binding: FragmentMercadoBinding
    private lateinit var db: TuOnceDatabase
    private lateinit var repository: Repository
    private lateinit var adapter: MercadoAdapter
    private var futbolistasMercado : List<Futbolista> = emptyList()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = TuOnceDatabase.getInstance(context)!!
        repository = Repository.getInstance(db.userDao(),db.futbolistaDao(), db.equipoDao(), db.actividadDao())
    }

    private fun subscribeUi(adapter: MercadoAdapter) {
        repository.futbolistas.observe(viewLifecycleOwner) { futbolistasActualesMercado ->
            futbolistasMercado = futbolistasActualesMercado.filter{
                it.equipoId == null
            }
            adapter.updateData(futbolistasMercado)
        }
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
                var futbolistas: List<Futbolista>? = futbolistasMercado
                futbolistas?.forEach {
                    if (it.equipoId == null) {
                        jugadoresLibres.add(it)
                    }
                }
                val jugadoresOrdenados = clasificarJugadores(jugadoresLibres)
                binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())
                binding.RvFutbolista.adapter = MercadoAdapter(
                    lista = jugadoresOrdenados,
                    contexto = requireContext(),
                    onClick = {
                            comprarFutbolista(it)
                    }
                )
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = MercadoAdapter(
            lista = futbolistasMercado,
            contexto = requireContext(),
            onClick = {
                comprarFutbolista(it)
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        subscribeUi(adapter)

        binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())
        binding.RvFutbolista.adapter = adapter
    }

    private fun comprarFutbolista(it : Futbolista)
    {

        lifecycleScope.launch {

            var futbolistaComprado: Futbolista? = it

            //Obtengo el equipo del usuario
            val usuarioConectado = recuperarUsuario()
            val equipoUsuario: Equipo? = recuperarEquipo(usuarioConectado)

            if (equipoUsuario?.presupuesto!! >= futbolistaComprado?.varor!!) {
                // Si lo compra, se va del mercado (con lo que ello implica)
                repository.eliminarFutbolistaDelMercado(futbolistaComprado, equipoUsuario, usuarioConectado)

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


