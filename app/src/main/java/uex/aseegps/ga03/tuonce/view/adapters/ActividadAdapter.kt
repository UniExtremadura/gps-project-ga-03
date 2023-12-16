package uex.aseegps.ga03.tuonce.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.AccionActividad
import uex.aseegps.ga03.tuonce.model.Actividad
import uex.aseegps.ga03.tuonce.model.User

class ActividadAdapter(private var listaActividades: List<Actividad>, var contexto: Context?, private val lifecycleScope: CoroutineScope, var usuarioConectado : String?) :
    RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    class ViewHolder(view: View, var contexto: Context?, private val lifecycleScope: CoroutineScope, val usuarioConectado : String?) : RecyclerView.ViewHolder(view) {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.noticia_actividad_list, parent, false)
        return ViewHolder(view, contexto, lifecycleScope, usuarioConectado)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actividad = listaActividades[position]
        val accion: AccionActividad = listaActividades[position].accion

        lifecycleScope.launch {
            holder.tvUsuarioActividad.text = usuarioConectado
            when (actividad.accion) {
                AccionActividad.INICIAR_LIGA -> {
                    holder.tvIniciarLigaActividad.text = actividad.ligaActividad
                }
                AccionActividad.ACABAR_LIGA -> {
                    holder.tvAcabarLigaActividad.text = actividad.ligaActividad
                }
                AccionActividad.COMPRAR_FUTBOLISTA -> {
                    holder.tvFutbolistaActividadComprar.text = actividad.futbolistaActividad

                }
                AccionActividad.VENDER_FUTBOLISTA ->{
                    holder.tvFutbolistaActividadVender.text = actividad.futbolistaActividad
                }
                AccionActividad.INICIAR_JORNADA ->{
                    holder.tvIniciarJornadaActividad.text = actividad.jornadaActividad?.toString()
                }
            }
        }



        when (accion) {
            AccionActividad.COMPRAR_FUTBOLISTA -> {
                showView(View.VISIBLE, holder.tvHaComprado, holder.tvFutbolistaActividadComprar)
                showView(View.GONE, holder.tvHaVendido, holder.tvFutbolistaActividadVender,
                    holder.tvHaIniciado, holder.tvIniciarLigaActividad,
                    holder.tvHaAcabadoLiga, holder.tvAcabarLigaActividad,
                    holder.tvHaIniciadoJornada, holder.tvIniciarJornadaActividad)
            }
            AccionActividad.VENDER_FUTBOLISTA -> {
                showView(View.VISIBLE, holder.tvHaVendido, holder.tvFutbolistaActividadVender)
                showView(View.GONE, holder.tvHaComprado, holder.tvFutbolistaActividadComprar,
                    holder.tvHaIniciado, holder.tvIniciarLigaActividad,
                    holder.tvHaAcabadoLiga, holder.tvAcabarLigaActividad,
                    holder.tvHaIniciadoJornada, holder.tvIniciarJornadaActividad)
            }
            AccionActividad.INICIAR_LIGA -> {
                showView(View.VISIBLE, holder.tvHaIniciado, holder.tvIniciarLigaActividad)
                showView(View.GONE, holder.tvHaComprado, holder.tvFutbolistaActividadComprar,
                    holder.tvHaVendido, holder.tvFutbolistaActividadVender,
                    holder.tvHaAcabadoLiga, holder.tvAcabarLigaActividad,
                    holder.tvHaIniciadoJornada, holder.tvIniciarJornadaActividad)
            }
            AccionActividad.ACABAR_LIGA -> {
                showView(View.VISIBLE, holder.tvHaAcabadoLiga, holder.tvAcabarLigaActividad)
                showView(View.GONE, holder.tvHaComprado, holder.tvFutbolistaActividadComprar,
                    holder.tvHaVendido, holder.tvFutbolistaActividadVender,
                    holder.tvHaIniciado, holder.tvIniciarLigaActividad,
                    holder.tvHaIniciadoJornada, holder.tvIniciarJornadaActividad)
            }
            AccionActividad.INICIAR_JORNADA -> {
                showView(View.VISIBLE, holder.tvHaIniciadoJornada, holder.tvIniciarJornadaActividad)
                showView(View.GONE, holder.tvHaComprado, holder.tvFutbolistaActividadComprar,
                    holder.tvHaVendido, holder.tvFutbolistaActividadVender,
                    holder.tvHaIniciado, holder.tvIniciarLigaActividad,
                    holder.tvHaAcabadoLiga, holder.tvAcabarLigaActividad)
            }
        }
    }

    fun showView(visibility: Int, vararg views: View) {
        views.forEach { it.visibility = visibility }
    }

    fun updateData(nuevasActividades: List<Actividad>) {
        listaActividades = nuevasActividades
        notifyDataSetChanged()
    }
    override fun getItemCount() = listaActividades.size
    fun updateDataUser(user: User?) {
        usuarioConectado = user?.name.toString()
        notifyDataSetChanged()
    }
}