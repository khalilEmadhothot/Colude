package com.example.colude

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import java.util.*

class MainActivity : AppCompatActivity(), IPickResult {
    private var db: FirebaseFirestore? = null
    private lateinit var auth: Firebase
    var reference: StorageReference? = null
    var storg: FirebaseStorage? = null
    var path:String?=null
    var adapter: FirestoreRecyclerAdapter<modle, BookViewHolder>? = null
    var count = 1
  lateinit var   progressDialog:ProgressDialog
    private lateinit var add: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var Image_VIEW_IM:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Firebase.firestore
        storg = Firebase.storage
        reference = storg!!.reference
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري لتحميل")
        progressDialog.setCancelable(false)
        add = findViewById(R.id.floatingActionButton)
        recyclerView = findViewById(R.id.recyclerView)
        getAllBook()
        Image_VIEW_IM.setOnClickListener {
            PickImageDialog.build(PickSetup()).show(this)
        }
        add.setOnClickListener {
            startActivity(Intent(this, ADDBOOK::class.java))
        }
    }
    private fun getAllBook() {
        recyclerView = findViewById(R.id.recyclerView)
        val query = db!!.collection("book")
        val options = FirestoreRecyclerOptions.Builder<modle>().setQuery(
            query,
            modle::class.java
        ).build()

        adapter = object : FirestoreRecyclerAdapter<modle,BookViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

                val view = LayoutInflater.from(this@MainActivity).inflate(
                    R.layout.card,
                    parent,
                    false)

                return BookViewHolder(view)


            }

            override fun onBindViewHolder(holder: BookViewHolder, position: Int, model: modle) {
                holder.name.text = model.name
                holder.athor.text = model.athor
                holder.year.text = model.year
                holder.price.text = model.price
                holder.edit.setOnClickListener {
                    intent(
                        model.id,
                        model.name,
                        model.athor,
                        model.year,
                        model.price
                    )
                }
                count++
            }
        }
//        adapter=BookViewHolder(this,options)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.name)!!
        var athor = view.findViewById<TextView>(R.id.athor)!!
        var year = view.findViewById<TextView>(R.id.year)!!
        var price = view.findViewById<TextView>(R.id.price)!!
        val edit = view.findViewById<Button>(R.id.edit)!!
    }

    fun intent(
        id: String,
        name: String,
        athor: String,
        year: String,
        price: String
    ) {
        val i = Intent(this, update::class.java)
        i.putExtra("id", id)
        i.putExtra("name", name)
        i.putExtra("athor", athor)
        i.putExtra("year", year)
        i.putExtra("price", price)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    override fun onPickResult(r: PickResult?) {
        Image_VIEW_IM.setImageBitmap(r!!.bitmap)
        uploadImage(r.uri)
    }

    private fun uploadImage(uri: Uri?) {
        progressDialog.show()
reference!!.child("profial"+UUID.randomUUID().toString()).putFile(uri!!).addOnSuccessListener { querySnaphot ->
progressDialog.dismiss()
    querySnaphot.storage.downloadUrl.addOnSuccessListener { uri ->
path=uri.toString()
    }.addOnFailureListener{exption ->

    }
    Toast.makeText(this,"suessuse",Toast.LENGTH_SHORT).show()
}.addOnFailureListener{exption ->

}
    }

}