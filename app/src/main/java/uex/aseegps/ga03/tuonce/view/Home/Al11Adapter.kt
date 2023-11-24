package uex.aseegps.ga03.tuonce.view.Home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.model.User

class Al11Adapter(private var lista: List<Futbolista>, private var contexto: Context, private val lifecycleScope: CoroutineScope) : RecyclerView.Adapter<Al11Adapter.Al11ViewHolder>() {

    class Al11ViewHolder(var vista: View, var contexto: Context?, private val lifecycleScope: CoroutineScope) : RecyclerView.ViewHolder(vista) {
        private lateinit var db: TuOnceDatabase
        fun bind(futbolista: Futbolista, adapter: Al11Adapter) {
            db = TuOnceDatabase.getInstance(contexto!!)!!
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaal11)
            val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaal11)

            // Establecer los datos del futbolista en los elementos visuales del diseño
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPuntosJugador.text = futbolista.puntosAportados.toString()
            val comprarButton = vista.findViewById<Button>(R.id.anadiral11)
            comprarButton.setOnClickListener {
                lifecycleScope.launch {
                    futbolista.estaEnel11 = 0
                    db?.futbolistaDao()?.update(futbolista)
                    val navController = Navigation.findNavController(vista)
                    // Navegar a la acción correspondiente
                    navController.navigate(R.id.action_moverAl11_to_equipoFragment)
                }
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Al11ViewHolder {
        return Al11ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.anadiral11, parent, false),
            contexto, lifecycleScope
        )
    }

    override fun getItemCount() = lista.size
    override fun onBindViewHolder(holder: Al11ViewHolder, position: Int) {
        Log.d("Posicion", lista[position].toString())
        holder.bind(lista[position], this)
    }
}
