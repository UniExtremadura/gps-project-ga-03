package uex.aseegps.ga03.tuonce.view.Home

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
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.adapter.ActividadAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentActividadBinding
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.Liga
import uex.aseegps.ga03.tuonce.model.User




class ActividadFragment : Fragment() {
    private var _binding: FragmentActividadBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: TuOnceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentActividadBinding.inflate(inflater, container, false)
        binding.rvActividad.layoutManager = LinearLayoutManager(context)
        cargarActividadesDesdeBD()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        cargarActividadesDesdeBD()
    }

    private fun cargarActividadesDesdeBD() {
        lifecycleScope.launch(Dispatchers.IO) {

            var usuario : User? = recuperarUsuario()
            var actividades : List<Actividad>? = db?.actividadDao()?.findAllByUser(usuario?.userId!!)

            withContext(Dispatchers.Main) {
                val adapter = ActividadAdapter(actividades!!, context, lifecycleScope)
                binding.rvActividad.adapter = adapter
            }

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
}