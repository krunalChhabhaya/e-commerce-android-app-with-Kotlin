package com.example.firebasegroupapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.Menu
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView
import android.view.MenuItem
import android.content.res.Configuration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.FirebaseDatabase
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class OrderActivity : AppCompatActivity() {

    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val topAppBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Orders"

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: ""
        val query = FirebaseDatabase.getInstance().reference.child("Orders").child(uid)
        val options = FirebaseRecyclerOptions.Builder<Order>().setQuery(query, Order::class.java).build()
        adapter = OrderAdapter(options)

        val ordersRecyclerView: RecyclerView = findViewById(R.id.ordersRecyclerView)

        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        ordersRecyclerView.layoutManager = layoutManager

        ordersRecyclerView.adapter = adapter

        val removeAllButton: Button = findViewById(R.id.removeAllButton)
        removeAllButton.setOnClickListener {
            removeOrdersFromDatabase(uid)
        }
    }

    private fun removeOrdersFromDatabase(uid: String) {
        val ordersRef = FirebaseDatabase.getInstance().reference.child("Orders").child(uid)
        ordersRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "All orders removed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to remove orders", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_products -> {
                startActivity(Intent(this, ProductActivity::class.java))
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}