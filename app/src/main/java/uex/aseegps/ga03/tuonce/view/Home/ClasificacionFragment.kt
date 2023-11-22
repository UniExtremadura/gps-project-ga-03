package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uex.aseegps.ga03.tuonce.adapter.UsuarioClasificacionAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase

class ClasificacionFragment : Fragment() {

    private lateinit var rvClasificacion: RecyclerView
    private lateinit var db: TuOnceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragment
        val view = inflater.inflate(R.layout.fragment_clasificacion, container, false)

        // Encuentra el RecyclerView en el layout
        rvClasificacion = view.findViewById(R.id.rvClasificacion)
        rvClasificacion.layoutManager = LinearLayoutManager(context)

        // Cargar los usuarios de la base de datos
        cargarUsuariosDesdeBD()

        return view
    }

    private fun cargarUsuariosDesdeBD() {
        lifecycleScope.launch(Dispatchers.IO) {
            val listaUsuarios = db.userDao().getAllUsers()
            // Ordenar usuarios por puntos de forma descendente
            val listaUsuariosOrdenada = listaUsuarios.sortedByDescending { it.points }

            // Actualiza el adaptador del RecyclerView en el hilo principal
            withContext(Dispatchers.Main) {
                val adapter = UsuarioClasificacionAdapter(listaUsuariosOrdenada)
                rvClasificacion.adapter = adapter
            }
        }
    }
}
