package uex.aseegps.ga03.tuonce.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.view.adapters.Al11Adapter
import uex.aseegps.ga03.tuonce.databinding.FragmentMoverAl11Binding
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.MoverAl11ViewNodel

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
    private lateinit var binding: FragmentMoverAl11Binding
    private lateinit var adapter: Al11Adapter
    private val viewModel: MoverAl11ViewNodel by viewModels { MoverAl11ViewNodel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoverAl11Binding.inflate(inflater, container, false)
        subscribeUi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
        }
        subscribeUi()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val context = this.context
        viewModel.initializeEquipo()
        var futbolistasDelEquipo = viewModel.obtenerFutbolistasDelEquipoOrdenados()
        lifecycleScope?.launch {
            adapter = Al11Adapter(
                lista = futbolistasDelEquipo,
                contexto = context!!,
                onClick = { futbolista, jugador ->
                    modificarDatos(futbolista, jugador)
                }
            )
            with(binding) {
                RvAl11.layoutManager = LinearLayoutManager(context)
                RvAl11.adapter = adapter
            }
            // Establece el texto que desees
            binding.TvRvAl11.text = "Selecciona el jugador que quieres quitar."
        }
    }
    private fun modificarDatos(futbolista: Futbolista, jugador : Futbolista){
            if (futbolista.posicion == jugador?.posicion) {
                viewModel.modificarDatos(futbolista, jugador)
                navegarAEquipo()
                showToast(requireContext(),"Cambiado correctamente")

                // mostrar un mensaje de satifecho el cambio
            }else{
                showToast(requireContext(),"No son de la misma posicion")
                viewModel.noCambio(futbolista,jugador)
                navegarAEquipo()
               // mensaje de error en el cambio
        }
    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun navegarAEquipo(){
        val navController = findNavController()
        navController.navigate(R.id.action_moverAl11_to_equipoFragment)
    }
    private fun subscribeUi() {
        viewModel.equipoUsuario.observe(viewLifecycleOwner) { equipo ->
            viewModel.initializeEquipo()
        }
        viewModel.futbolistasDelEquipoUsuario.observe(viewLifecycleOwner) { equipo ->
            viewModel.initializeEquipo()
        }
        }
    }

