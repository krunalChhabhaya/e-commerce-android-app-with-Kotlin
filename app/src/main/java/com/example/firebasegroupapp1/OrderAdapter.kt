package com.example.firebasegroupapp1

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage

class OrderAdapter(options: FirebaseRecyclerOptions<Order>)
    : FirebaseRecyclerAdapter<Order, OrderAdapter.OrderViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrderViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, model: Order) {
        holder.fullNameTextView.text = model.fullName
        holder.productNameTextView.text = model.productName
        holder.quantityTextView.text = model.quantity.toString()
        holder.totalPriceTextView.text = "$" + model.totalPrice.toString()
        holder.addressTextView.text = model.address
        val theImage: String = model.productImage

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
    }

    class OrderViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.order_item, parent, false)) {
        val fullNameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val totalPriceTextView: TextView = itemView.findViewById(R.id.totalPriceTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
    }
}