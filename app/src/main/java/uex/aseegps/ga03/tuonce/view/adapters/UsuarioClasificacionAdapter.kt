package uex.aseegps.ga03.tuonce.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.User

class UsuarioClasificacionAdapter(private val listaUsuarios: List<User>) :
    RecyclerView.Adapter<UsuarioClasificacionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.usuario_clasificacion_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.ivUsuario.setImageResource(usuario.image)
        holder.tvNombreUsuario.text = usuario.name
        holder.tvPuntosUsuario.text = usuario.points.toString()
    }

    override fun getItemCount() = listaUsuarios.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUsuario: ImageView = view.findViewById(R.id.ivUsuarioClasificacion)
        val tvNombreUsuario: TextView = view.findViewById(R.id.tvUsuarioClasificacion)
        val tvPuntosUsuario: TextView = view.findViewById(R.id.tvPuntosUsuarioClasificacion)
    }
}