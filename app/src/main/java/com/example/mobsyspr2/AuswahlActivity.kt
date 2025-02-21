// ListAuswahlActivity.kt
package com.example.mobsyspr2

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobsyspr2.databinding.ActivityListAuswahlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListAuswahlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListAuswahlBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var listAdapter: ListAdapter
    private val listList = mutableListOf<Einkaufsliste>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAuswahlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return // Abbruch, wenn kein Benutzer angemeldet

        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("lists")

        binding.recyclerViewLists.layoutManager = LinearLayoutManager(this)
        listAdapter = ListAdapter(listList) { listId ->
            // Item Click Listener: Öffne die HomeActivity mit der ausgewählten ListId
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("listId", listId)
            startActivity(intent)
        }
        binding.recyclerViewLists.adapter = listAdapter

        fetchLists()

        binding.btnAddList.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun fetchLists() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listList.clear()
                if (snapshot.exists()) {
                    for (listSnapshot in snapshot.children) {
                        val list = listSnapshot.getValue(Einkaufsliste::class.java)
                        list?.let { listList.add(it) }
                    }
                }
                listAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListAuswahlActivity, "Fehler beim Laden der Listen: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCreateListDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Neue Liste erstellen")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Erstellen") { dialog, _ ->
            val listName = input.text.toString()
            if (listName.isNotEmpty()) {
                addListToFirebase(listName)
            } else {
                Toast.makeText(this, "Bitte gib einen Listennamen ein.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Abbrechen") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addListToFirebase(listName: String) {
        val userId = auth.currentUser?.uid ?: return
        val newListId = database.push().key ?: return // Eindeutige ID für die neue Liste
        val newList = Einkaufsliste(newListId, listName) // Verwende die Einkaufsliste Data Class

        database.child(newListId).setValue(newList)
            .addOnSuccessListener {
                Toast.makeText(this, "Liste erfolgreich erstellt", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Fehler beim Erstellen der Liste: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
