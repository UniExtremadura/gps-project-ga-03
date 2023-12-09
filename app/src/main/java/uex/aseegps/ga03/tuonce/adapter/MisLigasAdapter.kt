package uex.aseegps.ga03.tuonce.adapter

import android.content.Intent
import android.net.Uri
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
            val fecha = article.publishedAt.split("T")[0].split("-")
            holder.textDate.text = "${fecha[2]}/${fecha[1]}/${fecha[0]}"
            holder.textAuthor.text = article.author
            holder.textTitle.text = article.title

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return listaArticulos.size
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textAuthor: TextView = view.findViewById(R.id.textAuthor)
            val textDate: TextView = view.findViewById(R.id.textDate)
            val textTitle: TextView = view.findViewById(R.id.textTitle)
        }
    }