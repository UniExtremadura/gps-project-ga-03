package uex.aseegps.ga03.tuonce.view.Home.Mercado

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.databinding.FragmentMercadoBinding
import uex.aseegps.ga03.tuonce.database.dummyFutbolista
import uex.aseegps.ga03.tuonce.model.Futbolista


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
        return binding.root
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
            binding.RvFutbolista.adapter = AdaptadorFutbolista(jugadoresLibres, requireContext())
        }

    }
}


