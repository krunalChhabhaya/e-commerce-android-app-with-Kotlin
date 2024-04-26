package com.example.firebasegroupapp1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductAdapter(options: FirebaseRecyclerOptions<Product>)
    : FirebaseRecyclerAdapter<Product, ProductAdapter.ProductViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ProductViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Product) {
        holder.productNameTextView.text = model.name
        holder.productPriceTextView.text = "$" + model.price.toString()
        val theImage: String = model.image

        if (theImage.indexOf("gs://") >-1) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(theImage)
            Glide.with(holder.productImageView.context)
                .load(storageReference)
                .into(holder.productImageView)
        } else {
            Glide.with(holder.productImageView.context)
                .load(theImage)
                .into(holder.productImageView)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java).apply {
                putExtra("productName", model.name)
                putExtra("productPrice", model.price)
                putExtra("productImage", model.image)
                putExtra("productDescription", model.description)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    class ProductViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_product, parent, false)) {
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
    }
}