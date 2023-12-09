package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.tiviclone.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.TuOnceApplication
import uex.aseegps.ga03.tuonce.view.adapters.ClasificacionAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentClasificacionBinding
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User

class ClasificacionFragment : Fragment() {

    private var _binding: FragmentClasificacionBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: Repository
    private lateinit var adapter: ClasificacionAdapter
    private lateinit var db: TuOnceDatabase

    private var ligaActiva : Liga? = null
    private var listaUsuarios : List<User> = emptyList()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = TuOnceDatabase.getInstance(context)!!

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClasificacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpRecyclerView() {
        adapter = ClasificacionAdapter(
                listaUsuarios
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (this.activity?.application as TuOnceApplication).appContainer
        repository = appContainer.repository

        lifecycleScope.launch(Dispatchers.IO) {
            var usuarioInteresado = recuperarUsuario()
            withContext(Dispatchers.Main) {
                repository.setUserid(usuarioInteresado?.userId!!)
                setUpRecyclerView()
                subscribeUi(adapter)
                binding.rvClasificacion.layoutManager = LinearLayoutManager(requireContext())
                binding.rvClasificacion.adapter = adapter
                binding.tvNombreLiga.text = "No hay liga activa"
            }
        }
    }

    private fun subscribeUi(adapter: ClasificacionAdapter) {
        repository.ligaUsuario.observe(viewLifecycleOwner) { liga ->
            ligaActiva = liga
            if (ligaActiva != null) {
                repository.setLigaId(liga.ligaId!!)
                binding.tvNombreLiga.text = ligaActiva?.name ?: "Liga sin nombre"
            }
        }
        repository.usuariosLiga.observe(viewLifecycleOwner) { usuarios ->
            listaUsuarios = usuarios
            listaUsuarios = listaUsuarios.distinctBy { it.userId }
            listaUsuarios = listaUsuarios.sortedByDescending { it.points }
            adapter.updateData(listaUsuarios)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun recuperarUsuario(): User? {
        return withContext(Dispatchers.Main) {
            db.userDao().obtenerUsuarioConectado()
        }
    }
}
