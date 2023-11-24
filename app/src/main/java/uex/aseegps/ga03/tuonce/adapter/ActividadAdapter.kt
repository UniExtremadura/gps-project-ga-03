package uex.aseegps.ga03.tuonce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad

class ActividadAdapter(private val listaActividades: List<Actividad>) :
    RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.noticia_actividad_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actividad = listaActividades[position]
        val accion: AccionActividad = listaActividades[position].accion

        holder.tvUsuarioActividad.text = actividad.usuarioActividad?.name

        holder.tvFutbolistaActividadComprar.text = actividad.futbolistaActividad?.nombreJugador

        holder.tvFutbolistaActividadVender.text = actividad.futbolistaActividad?.nombreJugador

        holder.tvIniciarLigaActividad.text = actividad.ligaActividad?.name

        holder.tvAcabarLigaActividad.text = actividad.ligaActividad?.name

        holder.tvIniciarJornadaActividad.text = actividad.jornadaActividad?.toString()

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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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