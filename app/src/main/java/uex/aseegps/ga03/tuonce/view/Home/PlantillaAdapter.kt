package uex.aseegps.ga03.tuonce.view.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.Futbolista

class PlantillaAdapter(private var lista: List<Futbolista>, private var contexto: Context?) :
    RecyclerView.Adapter<PlantillaAdapter.PlantillaViewHolder>()
{
    class PlantillaViewHolder(var vista: View, var contexto: Context?) : RecyclerView.ViewHolder(vista) {
        fun bind(futbolista: Futbolista) {
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaTxt)
            val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaTxt)

            // Aquí debes establecer los datos del futbolista en los elementos visuales del diseño
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPuntosJugador.text = futbolista.puntosAportados.toString()

            //Listener para vender y añadir al 11 ling :)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): PlantillaViewHolder {
        return PlantillaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plantilla_item_list,
            parent, false), contexto)
    }
    override fun getItemCount() = lista.size
    override fun onBindViewHolder(holder: PlantillaViewHolder, position:
    Int) {
        holder.bind(lista[position])
    }
}