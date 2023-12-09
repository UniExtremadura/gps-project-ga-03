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
import uex.aseegps.ga03.tuonce.view.adapters.ActividadAdapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentActividadBinding
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.User

class ActividadFragment : Fragment() {
    private var _binding: FragmentActividadBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: Repository
    private lateinit var db: TuOnceDatabase

    private lateinit var adapter: ActividadAdapter
    private var usuarioInteresado : User? = null
    private var actividades : List<Actividad> = emptyList()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = TuOnceDatabase.getInstance(context)!!
        repository = Repository.getInstance(db.userDao(),db.futbolistaDao(), db.equipoDao(), db.actividadDao())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentActividadBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Obtengo el usuario conectado y se lo digo al repository
        lifecycleScope.launch(Dispatchers.IO) {
            usuarioInteresado = recuperarUsuario()
            withContext(Dispatchers.Main) {
                repository.setUserid(usuarioInteresado?.userId!!)
                setUpRecyclerView()
                subscribeUi(adapter)
                binding.rvActividad.layoutManager = LinearLayoutManager(requireContext())
                binding.rvActividad.adapter = adapter
            }
        }
    }


    private fun subscribeUi(adapter: ActividadAdapter) {
        repository.actividades.observe(viewLifecycleOwner) { actividadesTotales ->
            actividades = actividadesTotales
            adapter.updateData(actividades)
        }
    }

    private fun setUpRecyclerView() {
        adapter = ActividadAdapter(actividades!!,
            context,
            lifecycleScope,
            usuarioInteresado)
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