package uex.aseegps.ga03.tuonce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.database.TuOnceDatabase
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.User

class ActividadAdapter(private val listaActividades: List<Actividad>, var contexto: Context?, private val lifecycleScope: CoroutineScope) :
    RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {
    private lateinit var db: TuOnceDatabase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.noticia_actividad_list, parent, false)
        return ViewHolder(view, contexto, lifecycleScope)
    }

    private suspend fun recuperarUsuario(): User? {
        return withContext(Dispatchers.Main) {
            db.userDao().obtenerUsuarioConectado()
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db = TuOnceDatabase.getInstance(contexto!!)!!
        val actividad = listaActividades[position]
        val accion: AccionActividad = listaActividades[position].accion

        lifecycleScope.launch {
            val usuarioConectado = recuperarUsuario()
            holder.tvUsuarioActividad.text = usuarioConectado?.name

            when (actividad.accion) {
                AccionActividad.INICIAR_LIGA -> {
                    holder.tvIniciarLigaActividad.text = db?.ligaDao()?.obtenerLigaPorId(actividad.ligaActividad!!)?.name.toString()
                }
                AccionActividad.ACABAR_LIGA -> {
                    holder.tvAcabarLigaActividad.text = db?.ligaDao()?.obtenerLigaPorId(actividad.ligaActividad!!)?.name.toString()
                }
                AccionActividad.COMPRAR_FUTBOLISTA -> {
                    holder.tvFutbolistaActividadComprar.text = db?.futbolistaDao()?.findById(actividad.futbolistaActividad!!)?.nombreJugador.toString()

                }
                AccionActividad.VENDER_FUTBOLISTA ->{
                    holder.tvFutbolistaActividadVender.text = db?.futbolistaDao()?.findById(actividad.futbolistaActividad!!)?.nombreJugador.toString()
                }
                AccionActividad.INICIAR_JORNADA ->{
                    holder.tvIniciarJornadaActividad.text = actividad.jornadaActividad?.toString()
                }
            }
        }


        if(accion == AccionActividad.COMPRAR_FUTBOLISTA) {

            holder.tvHaComprado.visibility = View.VISIBLE
            holder.tvFutbolistaActividadComprar.visibility = View.VISIBLE

            holder.tvHaVendido.visibility = View.GONE
            holder.tvFutbolistaActividadVender.visibility = View.GONE
            holder.tvHaIniciado.visibility = View.GONE
            holder.tvIniciarLigaActividad.visibility = View.GONE
            holder.tvHaAcabadoLiga.visibility = View.GONE
            holder.tvAcabarLigaActividad.visibility = View.GONE
            holder.tvHaIniciadoJornada.visibility = View.GONE
            holder.tvIniciarJornadaActividad.visibility = View.GONE

        } else if(accion == AccionActividad.VENDER_FUTBOLISTA) {

            holder.tvHaVendido.visibility = View.VISIBLE
            holder.tvFutbolistaActividadVender.visibility = View.VISIBLE

            holder.tvHaComprado.visibility = View.GONE
            holder.tvFutbolistaActividadComprar.visibility = View.GONE
            holder.tvHaIniciado.visibility = View.GONE
            holder.tvIniciarLigaActividad.visibility = View.GONE
            holder.tvHaAcabadoLiga.visibility = View.GONE
            holder.tvAcabarLigaActividad.visibility = View.GONE
            holder.tvHaIniciadoJornada.visibility = View.GONE
            holder.tvIniciarJornadaActividad.visibility = View.GONE

        } else if(accion == AccionActividad.INICIAR_LIGA) {

            holder.tvHaIniciado.visibility = View.VISIBLE
            holder.tvIniciarLigaActividad.visibility = View.VISIBLE

            holder.tvHaComprado.visibility = View.GONE
            holder.tvFutbolistaActividadComprar.visibility = View.GONE
            holder.tvHaVendido.visibility = View.GONE
            holder.tvFutbolistaActividadVender.visibility = View.GONE
            holder.tvHaAcabadoLiga.visibility = View.GONE
            holder.tvAcabarLigaActividad.visibility = View.GONE
            holder.tvHaIniciadoJornada.visibility = View.GONE
            holder.tvIniciarJornadaActividad.visibility = View.GONE

        } else if(accion == AccionActividad.ACABAR_LIGA) {

            holder.tvHaAcabadoLiga.visibility = View.VISIBLE
            holder.tvAcabarLigaActividad.visibility = View.VISIBLE

            holder.tvHaComprado.visibility = View.GONE
            holder.tvFutbolistaActividadComprar.visibility = View.GONE
            holder.tvHaVendido.visibility = View.GONE
            holder.tvFutbolistaActividadVender.visibility = View.GONE
            holder.tvHaIniciado.visibility = View.GONE
            holder.tvIniciarLigaActividad.visibility = View.GONE
            holder.tvHaIniciadoJornada.visibility = View.GONE
            holder.tvIniciarJornadaActividad.visibility = View.GONE

        } else if(accion == AccionActividad.INICIAR_JORNADA) {

            holder.tvHaIniciadoJornada.visibility = View.VISIBLE
            holder.tvIniciarJornadaActividad.visibility = View.VISIBLE

            holder.tvHaComprado.visibility = View.GONE
            holder.tvFutbolistaActividadComprar.visibility = View.GONE
            holder.tvHaVendido.visibility = View.GONE
            holder.tvFutbolistaActividadVender.visibility = View.GONE
            holder.tvHaIniciado.visibility = View.GONE
            holder.tvIniciarLigaActividad.visibility = View.GONE
            holder.tvHaAcabadoLiga.visibility = View.GONE
            holder.tvAcabarLigaActividad.visibility = View.GONE

        }

    }

    override fun getItemCount() = listaActividades.size

    class ViewHolder(view: View, var contexto: Context?, private val lifecycleScope: CoroutineScope) : RecyclerView.ViewHolder(view) {
        val tvUsuarioActividad: TextView = view.findViewById(R.id.tvUsuarioActividad)

        val tvHaComprado: TextView = view.findViewById(R.id.tvHaComprado)
        val tvFutbolistaActividadComprar: TextView = view.findViewById(R.id.tvFutbolistaActividadComprar)

        val tvHaVendido: TextView = view.findViewById(R.id.tvHaVendido)
        val tvFutbolistaActividadVender: TextView = view.findViewById(R.id.tvFutbolistaActividadVender)

        val tvHaIniciado: TextView = view.findViewById(R.id.tvHaIniciado)
        val tvIniciarLigaActividad: TextView = view.findViewById(R.id.tvIniciarLigaActividad)

        val tvHaAcabadoLiga: TextView = view.findViewById(R.id.tvHaAcabadoLiga)
        val tvAcabarLigaActividad: TextView = view.findViewById(R.id.tvAcabarLigaActividad)

        val tvHaIniciadoJornada: TextView = view.findViewById(R.id.tvHaIniciadoJornada)
        val tvIniciarJornadaActividad: TextView = view.findViewById(R.id.tvIniciarJornadaActividad)
    }
}