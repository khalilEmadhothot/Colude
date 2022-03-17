package com.example.colude

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ADDBOOK : AppCompatActivity() {
    private lateinit var auth: Firebase
    private var db: FirebaseFirestore? = null
    lateinit var button: Button
    lateinit var name: TextView
    lateinit var athor: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addbook)
        db = Firebase.firestore
        button = findViewById(R.id.button)
        name = findViewById(R.id.name)
        athor = findViewById(R.id.athor)
        var year = findViewById<TextView>(R.id.year)
        var price = findViewById<TextView>(R.id.price)
        val id = System.currentTimeMillis()
        button.setOnClickListener {
            Addbook(
                id.toString(),
                name.text.toString(),
                athor.text.toString(),
                year.text.toString(),
                price.text.toString(),

                )
            var Intent= Intent(this,MainActivity::class.java)

        }

    }

    private fun Addbook(id: String, name: String, athor: String, year: String, price: String) {

        val book = hashMapOf(


            "id" to id,
            "name" to name,
            "athor" to athor,
            "year" to year,
            "price" to price


        )

        db!!.collection("book").add(book)
    }
}