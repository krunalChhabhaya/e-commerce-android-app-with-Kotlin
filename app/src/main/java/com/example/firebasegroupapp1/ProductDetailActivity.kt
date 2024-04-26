package com.example.firebasegroupapp1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import android.view.MenuItem
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth

class ProductDetailActivity : AppCompatActivity() {

    private var quantity: Int = 1
    private lateinit var txtQuantity: TextView
    private lateinit var topAppBar: Toolbar
    var productImage : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        topAppBar = findViewById(R.id.top_app_bar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Product Detail"

        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        productImage = intent.getStringExtra("productImage")
        val productDescription = intent.getStringExtra("productDescription")

        val txtProductName: TextView = findViewById(R.id.txtProductName)
        val txtProductPrice: TextView = findViewById(R.id.txtProductPrice)
        val txtProductDescription: TextView = findViewById(R.id.txtProductDescription)
        val productImageView: ImageView = findViewById(R.id.productImageView)

        var totalPrice = roundToTwoDecimalPlaces(productPrice)

        txtProductName.text = " $productName"
        txtProductPrice.text = "Price: $$totalPrice"
        txtProductDescription.text = "$productDescription"

        txtQuantity = findViewById(R.id.txtQuantity)
        txtQuantity.text = quantity.toString()

        val theImage: String = productImage ?: ""

        if (theImage.indexOf("gs://") > -1) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(theImage)
            Glide.with(this)
                .load(storageReference)
                .into(productImageView)
        } else {
            Glide.with(this)
                .load(theImage)
                .into(productImageView)
        }

        val btnPlus: Button = findViewById(R.id.btnPlus)
        val btnMinus: Button = findViewById(R.id.btnMinus)

        btnPlus.setOnClickListener {
            incrementQuantity()
        }

        btnMinus.setOnClickListener {
            decrementQuantity()
        }

        val btnCheckout: Button = findViewById(R.id.btnCheckout)
        btnCheckout.setOnClickListener {
            checkout(productName, quantity, productPrice, productImage)
        }
    }

    fun roundToTwoDecimalPlaces(number: Double): Double {
        return "%.2f".format(number).toDouble()
    }

    private fun incrementQuantity() {
        if (quantity < 10) {
            quantity++
            updateQuantityTextView()
        } else {
            showToast("Maximum quantity reached")
        }
    }

    private fun decrementQuantity() {
        if (quantity > 1) {
            quantity--
            updateQuantityTextView()
        } else {
            showToast("Minimum quantity reached")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateQuantityTextView() {
        txtQuantity.text = quantity.toString()
    }

    private fun checkout(productName: String?, quantity: Int, productPrice: Double, productImage: String?) {
        val totalPrice = roundToTwoDecimalPlaces(quantity * productPrice)
        val intent = Intent(this, CheckoutActivity::class.java).apply {
            putExtra("productName", productName)
            putExtra("quantity", quantity)
            putExtra("totalPrice", totalPrice)
            putExtra("productImage", productImage)
            putExtra("fullName", FirebaseAuth.getInstance().currentUser?.displayName)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}