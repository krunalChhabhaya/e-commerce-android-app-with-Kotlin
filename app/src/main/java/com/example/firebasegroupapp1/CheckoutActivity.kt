package com.example.firebasegroupapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CheckoutActivity : AppCompatActivity() {

    private lateinit var topAppBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        topAppBar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Checkout"

        val productName = intent.getStringExtra("productName")
        val quantity = intent.getIntExtra("quantity", 0)
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val totalPriceFormatted = roundToTwoDecimalPlaces(totalPrice)

        val txtProductName: TextView = findViewById(R.id.txtProductName)
        val txtQuantity: TextView = findViewById(R.id.txtQuantity)
        val txtTotalPrice: TextView = findViewById(R.id.txtTotalPrice)

        txtProductName.text = "Product Name: $productName"
        txtQuantity.text = "Quantity: $quantity"
        txtTotalPrice.text = "Total Price: $$totalPriceFormatted"

        val currentUser = FirebaseAuth.getInstance().currentUser
        val fullName = currentUser?.displayName
        val email = currentUser?.email
        val etFullName: EditText = findViewById(R.id.etFullName)
        etFullName.setText(fullName)

        val btnPlaceOrder: Button = findViewById(R.id.btnPlaceOrder)
        val etPhoneNumber: EditText = findViewById(R.id.etPhoneNumber)
        val etAddress: EditText = findViewById(R.id.etAddress)
        val etCity: EditText = findViewById(R.id.etCity)
        val etProvince: EditText = findViewById(R.id.etProvince)
        val etPostalCode: EditText = findViewById(R.id.etPostalCode)
        val etCardHolderName: EditText = findViewById(R.id.etCardHolderName)
        val etCardNumber: EditText = findViewById(R.id.etCardNumber)
        val etSecurityCode: EditText = findViewById(R.id.etSecurityCode)
        val etValidUntil: EditText = findViewById(R.id.etValidUntil)

        btnPlaceOrder.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString()
            val address = etAddress.text.toString()
            val city = etCity.text.toString()
            val province = etProvince.text.toString()
            val postalCode = etPostalCode.text.toString()
            val cardHolderName = etCardHolderName.text.toString()
            val cardNumber = etCardNumber.text.toString()
            val securityCode = etSecurityCode.text.toString()
            val validUntil = etValidUntil.text.toString()

            val validationErrors = mutableListOf<Pair<EditText, String>>()

            val nameRegex = Regex("^[a-zA-Z ]+\$")
            val phoneRegex = Regex("^[0-9]{10}\$")
            val cityRegex = Regex("^[a-zA-Z.,\\- ]+\$")
            val provinceRegex = Regex("^[a-zA-Z]+\$")
            val postalCodeRegex = Regex("^[a-zA-Z0-9]{6}\$")
            val cardNumberRegex = Regex("^[0-9]{16}\$")
            val securityCodeRegex = Regex("^[0-9]{3}\$")
            val validUntilRegex = Regex("^[0-9]{4}\$")

            if (!phoneNumber.matches(phoneRegex)) {
                validationErrors.add(Pair(etPhoneNumber, "Invalid phone number"))
            }

            if (address.isBlank()) {
                validationErrors.add(Pair(etAddress, "Address cannot be empty"))
            }

            if (!city.matches(cityRegex)) {
                validationErrors.add(Pair(etCity, "Invalid city"))
            }

            if (!province.matches(provinceRegex)) {
                validationErrors.add(Pair(etProvince, "Invalid province"))
            }

            if (!postalCode.matches(postalCodeRegex)) {
                validationErrors.add(Pair(etPostalCode, "Invalid postal code"))
            }

            if (!cardHolderName.matches(nameRegex)) {
                validationErrors.add(Pair(etCardHolderName, "Invalid card holder name"))
            }

            if (!cardNumber.matches(cardNumberRegex)) {
                validationErrors.add(Pair(etCardNumber, "Invalid card number"))
            }

            if (!securityCode.matches(securityCodeRegex)) {
                validationErrors.add(Pair(etSecurityCode, "Invalid security code"))
            }

            if (!validUntil.matches(validUntilRegex)) {
                validationErrors.add(Pair(etValidUntil, "Invalid valid until"))
            }

            validationErrors.forEach { (editText, errorMessage) ->
                editText.error = errorMessage
            }

            if (validationErrors.isNotEmpty()) {
                return@setOnClickListener
            }

            val order = Order(
                fullName = fullName,
                email = email,
                quantity = quantity,
                productName = productName,
                phoneNumber = phoneNumber,
                totalPrice = totalPrice,
                address = address,
                city = city,
                province = province,
                postalCode = postalCode,
                cardHolderName = cardHolderName,
                cardNumber = cardNumber,
                securityCode = securityCode,
                validUntil = validUntil,
                productImage = intent.getStringExtra("productImage") ?: ""
            )
            saveOrder(order)
        }
    }

    private fun saveOrder(order: Order) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        if (uid != null) {
            val roundedTotalPrice = roundToTwoDecimalPlaces(order.totalPrice)
            order.totalPrice = roundedTotalPrice
            FirebaseDatabase.getInstance().reference.child("Orders/$uid").push().setValue(order).addOnSuccessListener {
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, OrderActivity::class.java))
                finish()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to place order: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }


    fun roundToTwoDecimalPlaces(number: Double): Double {
        return "%.2f".format(number).toDouble()
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