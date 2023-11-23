package uex.aseegps.ga03.tuonce.view.Home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.Futbolista

class PlantillaAdapter(private var lista: List<Futbolista>, private var contexto: Context?,  private val lifecycleScope: CoroutineScope) :
    RecyclerView.Adapter<PlantillaAdapter.PlantillaViewHolder>()

{
    class PlantillaViewHolder(var vista: View, var contexto: Context?, private val lifecycleScope: CoroutineScope) : RecyclerView.ViewHolder(vista) {
        private lateinit var db: TuOnceDatabase
        fun bind(futbolista: Futbolista) {
            db = TuOnceDatabase.getInstance(contexto!!)!!
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaTxt)
            val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaTxt)

            // Aquí debes establecer los datos del futbolista en los elementos visuales del diseño
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPuntosJugador.text = futbolista.puntosAportados.toString()

            val comprarButton = vista.findViewById<Button>(R.id.comprarBt)
            if(futbolista.estaEnel11 == 1){
                comprarButton.visibility = View.GONE
            }else{
                comprarButton.visibility = View.VISIBLE
            }
            comprarButton.setOnClickListener {
                lifecycleScope.launch {
                    futbolista.estaEnel11 = 2
                    db?.futbolistaDao()?.update(futbolista)
                    val navController = Navigation.findNavController(vista)
                    // Navegar a la acción correspondiente
                    navController.navigate(R.id.action_plantillaFragment_to_moverAl11)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): PlantillaViewHolder {
        return PlantillaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plantilla_item_list,
            parent, false), contexto, lifecycleScope)
    }
    override fun getItemCount() = lista.size
    override fun onBindViewHolder(holder: PlantillaViewHolder, position:
    Int) {
        holder.bind(lista[position])
    }
}