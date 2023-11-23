package uex.aseegps.ga03.tuonce.view.Home.Mercado

import android.view.View
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.view.Home.DetalleFutbolista



class AdaptadorFutbolista(private var lista: List<Futbolista>,private var contexto: Context) : RecyclerView.Adapter<AdaptadorFutbolista.viewHolder>() {

    class viewHolder(var vista: View, var contexto: Context) : RecyclerView.ViewHolder(vista) {
        fun bind(futbolista: Futbolista) {
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.xmlnombreJugador)
            val xmlPrecioJugador = vista.findViewById<TextView>(R.id.xmlPrecioJugador)
            // Aquí debes establecer los datos del futbolista en los elementos visuales del diseño
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPrecioJugador.text = futbolista.varor.toString()

            vista.setOnClickListener{
                val intent = Intent(contexto, DetalleFutbolista::class.java)
                intent.putExtra("nom", futbolista)
                contexto.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cartafutbolista ,parent, false), contexto)
    }

    override fun getItemCount(): Int {
        return minOf(lista.size, 12)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(lista[position])
    }
}