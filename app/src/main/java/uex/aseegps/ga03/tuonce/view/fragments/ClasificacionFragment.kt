package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.view.adapters.UsuarioClasificacionAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentClasificacionBinding
import uex.aseegps.ga03.tuonce.model.User

class ClasificacionFragment : Fragment() {

    private var _binding: FragmentClasificacionBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: TuOnceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClasificacionBinding.inflate(inflater, container, false)
        binding.rvClasificacion.layoutManager = LinearLayoutManager(context)
        cargarUsuariosDesdeBD()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        cargarUsuariosDesdeBD()
    }

    private fun cargarUsuariosDesdeBD() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioActivo = db.userDao().obtenerUsuarioConectado()
            val ligaActiva = usuarioActivo?.userId?.let { db.ligaDao().obtenerLigaPorUsuario(it) }
            var listaUsuariosOrdenada: List<User> = emptyList()

            ligaActiva?.ligaId?.let { ligaId ->
                var listaUsuarios = db.ligaDao().obtenerUsuariosPorLiga(ligaId)
                listaUsuarios = listaUsuarios.distinctBy { it.userId }
                listaUsuariosOrdenada = listaUsuarios.sortedByDescending { it.points }
            }

            withContext(Dispatchers.Main) {
                val adapter = UsuarioClasificacionAdapter(listaUsuariosOrdenada)
                binding.rvClasificacion.adapter = adapter
                binding.tvNombreLiga.text = ligaActiva?.name ?: "No hay liga activa"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
