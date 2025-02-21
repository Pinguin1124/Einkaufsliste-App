// ListAdapter.kt
package com.example.mobsyspr2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(
    private val listList: List<Einkaufsliste>,
    private val onItemClick: (String) -> Unit // Listener für Klicks auf eine Liste
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = listList[position]
        holder.tvListName.text = list.name
        holder.itemView.setOnClickListener {
            onItemClick(list.id) // Rufe den Listener mit der ListId auf
        }
    }

    override fun getItemCount(): Int = listList.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvListName: TextView = itemView.findViewById(R.id.tvListName) // Stelle sicher, dass du eine TextView mit dieser ID in deinem list_item_layout hast
    }
}
