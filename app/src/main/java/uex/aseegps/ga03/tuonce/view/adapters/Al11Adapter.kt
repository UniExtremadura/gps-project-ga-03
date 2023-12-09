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

class Al11Adapter(private var lista: List<Futbolista>, private var contexto: Context, private val lifecycleScope: CoroutineScope) : RecyclerView.Adapter<Al11Adapter.Al11ViewHolder>() {

    class Al11ViewHolder(var vista: View, var contexto: Context?, private val lifecycleScope: CoroutineScope) : RecyclerView.ViewHolder(vista) {
        private lateinit var db: TuOnceDatabase
        fun bind(futbolista: Futbolista, adapter: Al11Adapter) {
            lifecycleScope.launch {
                db = TuOnceDatabase.getInstance(contexto!!)!!
                val xmlImgJugador = vista.findViewById<ImageView>(R.id.imgFutbolistaAlOnce)
                val xmlnombreJugador = vista.findViewById<TextView>(R.id.nombreFutbolistaal11)
                val xmlPuntosJugador = vista.findViewById<TextView>(R.id.puntosFutbolistaal11)
                val xmlPosicionJugador = vista.findViewById<TextView>(R.id.posTxt)

                var futbolistas: List<Futbolista>? = withContext(Dispatchers.IO) {
                    db?.futbolistaDao()?.findAll()
                }

                val equipo: Equipo? = withContext(Dispatchers.IO) {
                    recuperarEquipo(recuperarUsuario())
                }

                var jugador: Futbolista? = null
                futbolistas?.forEach {
                    if ((it.equipoId == equipo?.equipoId) and (it.estaEnel11 == 2)) {
                        jugador = it
                    }
                }
                // Establecer los datos del futbolista en los elementos visuales del diseño
                xmlImgJugador.setImageResource(futbolista.image)
                xmlnombreJugador.text = futbolista.nombreJugador
                xmlPuntosJugador.text = futbolista.puntosAportados.toString()
                xmlPosicionJugador.text = futbolista.posicion.toString()
                val comprarButton = vista.findViewById<Button>(R.id.anadiral11)
                comprarButton.setOnClickListener {
                    if (futbolista.posicion == jugador?.posicion) {
                        lifecycleScope.launch {
                            futbolista.estaEnel11 = 0
                            db?.futbolistaDao()?.update(futbolista)
                            jugador?.estaEnel11 = 1
                            db?.futbolistaDao()?.update(jugador)
                            val navController = Navigation.findNavController(vista)
                            // Navegar a la acción correspondiente
                            navController.navigate(R.id.action_moverAl11_to_equipoFragment)
                            showToast("Se ha realizado el cambio")
                        }
                    } else {
                        lifecycleScope.launch {
                            jugador?.estaEnel11 = 0
                            db?.futbolistaDao()?.update(jugador)
                            val navController = Navigation.findNavController(vista)
                            navController.navigate(R.id.action_moverAl11_to_equipoFragment)
                            showToast("Las posiciones no coinciden")
                        }
                    }
                }
            }
        }
        private fun showToast(message: String) {
            Toast.makeText(contexto, message, Toast.LENGTH_SHORT).show()
        }
        private suspend fun recuperarUsuario(): User? {
            return withContext(Dispatchers.IO) {
                db?.userDao()?.obtenerUsuarioConectado()
            }
        }
        private suspend fun recuperarEquipo(usuario: User?): Equipo? {
            return withContext(Dispatchers.IO) {
                db?.equipoDao()?.findByUserId(usuario?.userId)
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
