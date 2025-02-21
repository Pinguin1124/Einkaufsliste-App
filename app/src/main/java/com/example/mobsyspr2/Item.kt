// Item.kt
package com.example.mobsyspr2

data class Item(
    val id: String = "",
    val id_kategorie: Long? = null, // Optionaler Long für `id_kategorie`
    val menge: Long = 0,
    val notizen: String = "",
    val produkt: String = ""
)
