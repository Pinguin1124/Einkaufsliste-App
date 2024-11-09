package com.example.mobsyspr2
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ItemsAdapter(private val itemsList: List<Item>) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tvProdukt.text = item.produkt
        holder.tvMenge.text = item.menge.toString()
        holder.tvNotiz.text = item.notizen

        holder.btnDelete.setOnClickListener {
            deleteItemFromFirebase(item.id)
        }
    }

    override fun getItemCount(): Int = itemsList.size

    private fun deleteItemFromFirebase(itemId: String) {
        FirebaseDatabase.getInstance().getReference("Einkaufsliste")
            .child(itemId)
            .removeValue()
            .addOnSuccessListener {
                println("Item erfolgreich gelöscht")
            }
            .addOnFailureListener { e ->
                println("Fehler beim Löschen des Items: ${e.message}")
            }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProdukt: TextView = itemView.findViewById(R.id.tvProdukt)
        val tvMenge: TextView = itemView.findViewById(R.id.tvMenge)
        val tvNotiz: TextView = itemView.findViewById(R.id.tvNotiz)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}
