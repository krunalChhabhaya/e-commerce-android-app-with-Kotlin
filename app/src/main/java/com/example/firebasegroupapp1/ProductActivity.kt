package com.example.firebasegroupapp1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProductActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val topAppBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(topAppBar)
        supportActionBar?.title = "Products"

        auth = FirebaseAuth.getInstance()

        val query = FirebaseDatabase.getInstance().reference.child("Products")
        val options = FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
        adapter = ProductAdapter(options)

        val productsRecyclerView: RecyclerView = findViewById(R.id.productsRecyclerView)
        productsRecyclerView.adapter = adapter

        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 3)
        } else {
            GridLayoutManager(this, 2)
        }
        productsRecyclerView.layoutManager = layoutManager

        val logoutButton: ImageView = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_products -> {
                true
            }
            R.id.action_orders -> {
                startActivity(Intent(this, OrderActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
}