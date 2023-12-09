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
import uex.aseegps.ga03.tuonce.adapter.Al11Adapter
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentMoverAl11Binding
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoverAl11Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoverAl11Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMoverAl11Binding
    private lateinit var adapter: Al11Adapter
    private lateinit var db: TuOnceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = TuOnceDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoverAl11Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = this.context
        var futbolistasDelEquipo = mutableListOf<Futbolista>()

        lifecycleScope?.launch {
            var futbolistas: List<Futbolista>? = db?.futbolistaDao()?.findAll()
            val equipo: Equipo? = recuperarEquipo(recuperarUsuario())
            futbolistas?.forEach {
                if ((it.equipoId == equipo?.equipoId) and (it.estaEnel11 == 1)) {
                    futbolistasDelEquipo.add(it)
                }
            }
            adapter = Al11Adapter(
                lista = futbolistasDelEquipo,
                contexto = context!!,
                viewLifecycleOwner.lifecycleScope
            )
            with(binding) {
                RvAl11.layoutManager = LinearLayoutManager(context)
                RvAl11.adapter = adapter
            }
            // Establece el texto que desees
                binding.TvRvAl11.text = "Selecciona el jugador que quieres quitar."
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

