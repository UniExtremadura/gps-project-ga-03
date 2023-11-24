package uex.aseegps.ga03.tuonce.view.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.databinding.FragmentMisLigasBinding
import uex.aseegps.ga03.tuonce.api.RetrofitServiceFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MisLigasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisLigasFragment : Fragment() {

    private var _binding: FragmentMisLigasBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMisLigasBinding.inflate(inflater, container, false)
        binding.rvShowList.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArticles()
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            binding.spinner.visibility = View.VISIBLE
            try {
                val service = RetrofitServiceFactory.makeRetrofitService()
                val remoteResource = service.listNoticias("sports", "ar", "49aa5dc1188e4486810c0f8cf239bc00")
                val notices = remoteResource.articles
                binding.spinner.visibility = View.GONE
                withContext(Dispatchers.Main) {
                    val adapter = MisLigasAdapter(notices)
                    binding.rvShowList.adapter = adapter
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MisLigasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MisLigasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}