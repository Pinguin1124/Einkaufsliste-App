// HomeActivity.kt
package com.example.mobsyspr2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobsyspr2.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityHomeBinding
    private lateinit var itemsAdapter: ItemsAdapter
    private val itemsList = mutableListOf<Item>()
    private lateinit var listId: String // ID der ausgewählten Liste
    private lateinit var auth: FirebaseAuth
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ListId aus dem Intent holen
        listId = intent.getStringExtra("listId") ?: run {
            Toast.makeText(this, "Keine ListId gefunden!", Toast.LENGTH_SHORT).show()
            finish() // Activity schließen, wenn keine ListId vorhanden
            return
        }

        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        itemsAdapter = ItemsAdapter(itemsList, listId) // ListId an den Adapter übergeben
        binding.recyclerViewItems.adapter = itemsAdapter

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("lists").child(listId)

        fetchItems()

        binding.btnHinzufuegen.setOnClickListener {
            val produkt = binding.etProdukt.text.toString()
            val menge = binding.etMenge.text.toString().toIntOrNull() ?: 0
            val notiz = binding.etNotiz.text.toString()

            if (produkt.isNotEmpty() && menge > 0) {
                addItemToFirebase(produkt, menge, notiz)
            } else {
                toast("Bitte fülle alle Felder korrekt aus.")
            }
        }
    }

    private fun fetchItems() {
        database.child("items").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemsList.clear()
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        item?.let { itemsList.add(it) }
                    }
                    Log.d("FirebaseData", "Anzahl der Items: ${itemsList.size}")
                    itemsAdapter.notifyDataSetChanged()
                } else {
                    Log.d("FirebaseData", "Keine Daten gefunden")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toast("Fehler beim Laden der Daten: ${error.message}")
            }
        })
    }

    private fun addItemToFirebase(produkt: String, menge: Int, notiz: String) {
        val newItemId = database.child("items").push().key ?: return
        val newItem = mapOf(
            "id" to newItemId,
            "id_kategorie" to 1,
            "menge" to menge,
            "notizen" to notiz,
            "produkt" to produkt
        )

        database.child("items").child(newItemId).setValue(newItem)
            .addOnSuccessListener {
                toast("Eintrag erfolgreich hinzugefügt")
            }
            .addOnFailureListener { e ->
                toast("Fehler beim Hinzufügen: ${e.message}")
            }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
