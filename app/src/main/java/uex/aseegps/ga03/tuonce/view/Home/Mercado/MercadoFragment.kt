package uex.aseegps.ga03.tuonce.view.Home.Mercado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.Futbolista
import uex.aseegps.ga03.tuonce.databinding.FragmentMercadoBinding
import uex.aseegps.ga03.tuonce.database.dummyFutbolista


class MercadoFragment : Fragment() {
    private lateinit var binding: FragmentMercadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Utiliza requireContext() para obtener el contexto
        binding.RvFutbolista.layoutManager = LinearLayoutManager(requireContext())

        // Aquí puedes continuar configurando tu RecyclerView con el adaptador, etc.
        binding.RvFutbolista.adapter = AdaptadorFutbolista(dummyFutbolista, requireContext())
    }
}


