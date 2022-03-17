package com.example.colude

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class update : AppCompatActivity() {
    private lateinit var auth: Firebase
    private var db: FirebaseFirestore? = null
    lateinit var button: Button
    lateinit var name: TextView
    lateinit var athor: TextView
    lateinit var year: TextView
    lateinit var price: TextView
    lateinit var button1: Button
    var path:String?=null
lateinit var  updateimage:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        db = Firebase.firestore
        button = findViewById(R.id.button)
        name = findViewById(R.id.name)
        athor = findViewById(R.id.athor)
        year = findViewById(R.id.year)
        price = findViewById(R.id.price)

        updateimage.setOnClickListener {
            updateimage(path)
        }
        name.setText(intent.getStringExtra("name").toString())
        athor.setText(intent.getStringExtra("athor").toString())
        year.setText(intent.getStringExtra("year").toString())
        price.setText(intent.getStringExtra("price").toString())

        button.setOnClickListener {
            Update()
        }

        button1.setOnClickListener {
            DELETE()
        }
    }

    private fun updateimage(path: String?) {
       db!!.collection("book").get()
           .addOnSuccessListener { querSnahot ->
               db!!.collection("book").document(querSnahot.documents.get(0).id).update("image",path)
           }.addOnFailureListener{exption ->

           }
    }

    private fun Update() {
        db!!.collection("book").whereEqualTo(FieldPath.documentId(), intent.getStringExtra("id"))
            .get().addOnSuccessListener { querySnapshot ->
                db!!.collection("book").document(querySnapshot.documents.get(0).id)
                    .update(
                        "name",
                        name.text.toString(),
                        "athor",
                        athor.text.toString(),
                        "year",
                        year.text.toString(),
                        "price",
                        price.text.toString()
                    )
                finish()
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Edit operation failed", Toast.LENGTH_LONG).show()
            }

    }

    private fun DELETE() {
        db!!.collection("book").get().addOnSuccessListener { querySnaphot ->
            for (document in querySnaphot) {
                document.toObject<modle>()
                if (document.get("id") == intent.getStringExtra("id")) {
                    db!!.collection("book").document(document.id).delete()
                }
            }
        }
    }
}