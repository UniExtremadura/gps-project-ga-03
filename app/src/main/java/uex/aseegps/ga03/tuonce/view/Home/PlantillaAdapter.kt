package uex.aseegps.ga03.tuonce.view.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.Equipo
import uex.aseegps.ga03.tuonce.model.Futbolista

class PlantillaAdapter(private var lista: List<Futbolista>, private var contexto: Context?, private val onClick: (show: Futbolista) -> Unit) :
    RecyclerView.Adapter<PlantillaAdapter.PlantillaViewHolder>()
{
    class PlantillaViewHolder(var vista: View, var contexto: Context, private val onClick: (show: Futbolista) -> Unit,) : RecyclerView.ViewHolder(vista) {
        private val db: TuOnceDatabase? = TuOnceDatabase.getInstance(contexto)
        fun bind(futbolista: Futbolista) {
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaTxt)
            val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaTxt)

            // Aquí debes establecer los datos del futbolista en los elementos visuales del diseño
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPuntosJugador.text = futbolista.puntosAportados.toString()

            val btVender = vista.findViewById<Button>(R.id.venderBt)
            btVender.setOnClickListener {
                onClick(futbolista)
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): PlantillaViewHolder {
        return PlantillaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plantilla_item_list,
            parent, false), contexto!!, onClick)
    }
    override fun getItemCount() = lista.size
    override fun onBindViewHolder(holder: PlantillaViewHolder, position:
    Int) {
        holder.bind(lista[position])
    }
}