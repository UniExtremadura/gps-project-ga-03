package uex.aseegps.ga03.tuonce.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.Futbolista
import uex.aseegps.ga03.tuonce.view.activities.DetalleFutbolistaActivity

class PlantillaAdapter(private var lista: List<Futbolista>, private var contexto: Context?, private val lifecycleScope: CoroutineScope, private val onClick: (show: Futbolista) -> Unit) :
    RecyclerView.Adapter<PlantillaAdapter.PlantillaViewHolder>()

{
    class PlantillaViewHolder(var vista: View, var contexto: Context?, private val lifecycleScope: CoroutineScope, private val onClick: (show: Futbolista) -> Unit) : RecyclerView.ViewHolder(vista) {
        private lateinit var db: TuOnceDatabase
        fun bind(futbolista: Futbolista) {
            db = TuOnceDatabase.getInstance(contexto!!)!!
            val xmlimgFutbolista = vista.findViewById<ImageView>(R.id.jugadorPlantillaImg)
            val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaTxt)
            val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaTxt)
            val xmlPosicionJugador = vista.findViewById<TextView>(R.id.posicionFutbolistaTxt)
            val xmlTitularJugador = vista.findViewById<TextView>(R.id.titularTxt)
            // Aquí debes establecer los datos del futbolista en los elementos visuales del diseño
            xmlimgFutbolista.setImageResource(futbolista.image)
            xmlnombreJugador.text = futbolista.nombreJugador
            xmlPuntosJugador.text = futbolista.puntosAportados.toString()
            xmlPosicionJugador.text = futbolista.posicion
            if(futbolista.estaEnel11 == 1){
                xmlTitularJugador.visibility = View.VISIBLE
            }else{
                xmlTitularJugador.visibility = View.GONE
            }

            val movelAl11Button = vista.findViewById<Button>(R.id.comprarBt)
            val venderButton = vista.findViewById<Button>(R.id.venderBt)
            if(futbolista.estaEnel11 == 1){
                movelAl11Button.visibility = View.GONE
                venderButton.visibility = View.GONE
            }else{
                movelAl11Button.visibility = View.VISIBLE
                venderButton.visibility = View.VISIBLE
            }
            movelAl11Button.setOnClickListener {
                lifecycleScope.launch {
                    futbolista.estaEnel11 = 2
                    db?.futbolistaDao()?.update(futbolista)
                    val navController = Navigation.findNavController(vista)
                    // Navegar a la acción correspondiente
                    navController.navigate(R.id.action_plantillaFragment_to_moverAl11)
                }
            }
            val btVender = vista.findViewById<Button>(R.id.venderBt)
            btVender.setOnClickListener {
                onClick(futbolista)
            }

            vista.findViewById<CardView>(R.id.cv_Item).setOnClickListener{
                verFutbolista(futbolista)
            }
        }

        private fun verFutbolista(futbolista: Futbolista){
            val intent = Intent(contexto, DetalleFutbolistaActivity::class.java)
            intent.putExtra("nom", futbolista)
            contexto?.startActivity(intent)
        }
    }

    fun updateData(newList: List<Futbolista>) {
        this.lista = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): PlantillaViewHolder {
        return PlantillaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plantilla_item_list,
            parent, false), contexto, lifecycleScope,onClick)
    }
    override fun getItemCount() = lista.size
    override fun onBindViewHolder(holder: PlantillaViewHolder, position:
    Int) {
        holder.bind(lista[position])
    }
}