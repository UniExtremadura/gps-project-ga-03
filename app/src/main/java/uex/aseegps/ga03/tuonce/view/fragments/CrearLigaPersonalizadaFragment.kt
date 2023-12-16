package uex.aseegps.ga03.tuonce.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import uex.aseegps.ga03.tuonce.databinding.FragmentCrearLigaPersonalizadaBinding
import uex.aseegps.ga03.tuonce.view.viewmodels.CrearLigaPersonalizadaViewModel
import uex.aseegps.ga03.tuonce.view.viewmodels.HomeViewModel
import java.lang.Thread.sleep
import java.util.Arrays

class CrearLigaPersonalizadaFragment : Fragment() {
    private var _binding: FragmentCrearLigaPersonalizadaBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CrearLigaPersonalizadaViewModel by viewModels { CrearLigaPersonalizadaViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrearLigaPersonalizadaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModel.initialize()
        }

        subscribeUi()
        setUpListeners()

    }

    private fun subscribeUi() {
        viewModel.equipoUsuario.observe(viewLifecycleOwner){}
    }


    private fun setUpListeners() {
        with(binding){
            btnConfirmarLigaP.setOnClickListener {
                if (viewModel.crearLiga(editTextNombreLiga.text.toString(),
                        editTextNumJornadas.text.toString(), Arrays.asList(
                            uex.aseegps.ga03.tuonce.R.drawable.bot1,
                            uex.aseegps.ga03.tuonce.R.drawable.bot2,
                            uex.aseegps.ga03.tuonce.R.drawable.bot3
                        )))
                {
                    findNavController().navigate(uex.aseegps.ga03.tuonce.R.id.action_crearLigaPersonalizada_to_misLigasFragment)
                }else
                    Toast.makeText(context, "Por favor, ingresa un nombre y un número de jornadas correctamente.", android.widget.Toast.LENGTH_LONG).show()
            }

            val navController = findNavController()
            btnAtrasLigaP.setOnClickListener {
                navController.navigateUp()
            }
        }
    }




}