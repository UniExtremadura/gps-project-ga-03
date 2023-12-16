package uex.aseegps.ga03.tuonce.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

class Al11Adapter(private var lista: List<Futbolista>, private var contexto: Context,private val onClick: (futbolista: Futbolista, otroFutbolista: Futbolista) -> Unit) : RecyclerView.Adapter<Al11Adapter.Al11ViewHolder>() {
    class Al11ViewHolder(var vista: View, var contexto: Context, private val onClick: (futbolista: Futbolista, jugador: Futbolista) -> Unit) : RecyclerView.ViewHolder(vista) {
        fun bind(futbolista: Futbolista, adapter: Al11Adapter, lista: List<Futbolista>) {
            val xmlImgJugador = vista.findViewById<ImageView>(R.id.imgFutbolistaAlOnce)
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaal11)
            val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaal11)
            val xmlPosicionJugador = vista.findViewById<TextView>(R.id.posTxt)

            var jugador: Futbolista? = null
            lista?.forEach {
                if (it.estaEnel11 == 2) {
                    jugador = it
                }
            }
            if (futbolista.estaEnel11 == 2) {
                vista.visibility = View.GONE // Esto ocultará el elemento en el RecyclerView
                return // Salir de la función para no seguir configurando la vista
            } else {
                vista.visibility = View.VISIBLE // Asegurarse de que la vista sea visible para otros elementos
            }
            // Establecer los datos del futbolista en los elementos visuales del diseño
            xmlImgJugador.setImageResource(futbolista.image)
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPuntosJugador.text = futbolista.puntosAportados.toString()
            xmlPosicionJugador.text = futbolista.posicion.toString()
            val comprarButton = vista.findViewById<Button>(R.id.anadiral11)
            comprarButton.setOnClickListener {
                jugador?.let { onClick(futbolista, it) } ?: showToast(contexto,"Jugador no seleccionado")
            }
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Al11ViewHolder {
        return Al11ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.anadiral11, parent, false),
            contexto, onClick
        )
    }

    override fun getItemCount() = lista.size
    override fun onBindViewHolder(holder: Al11ViewHolder, position: Int) {
        holder.bind(lista[position], this, lista)
    }
}
