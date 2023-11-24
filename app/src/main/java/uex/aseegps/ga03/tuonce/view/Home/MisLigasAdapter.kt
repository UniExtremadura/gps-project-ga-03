package uex.aseegps.ga03.tuonce.view.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uex.aseegps.ga03.tuonce.R
import uex.aseegps.ga03.tuonce.model.api.Article

class MisLigasAdapter(private var listaArticulos: List<Article>) :
    RecyclerView.Adapter<MisLigasAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_item_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val article = listaArticulos[position]
            holder.textTitle.text = article.title
        }

        override fun getItemCount(): Int {
            return listaArticulos.size
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textTitle: TextView = view.findViewById(R.id.textTitle)
        }
    }