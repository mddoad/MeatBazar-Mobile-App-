package com.example.meatbazar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meatbazar.R
import com.example.meatbazar.product.Product
import com.google.firebase.firestore.FirebaseFirestore

class AdminProductAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
) : RecyclerView.Adapter<AdminProductAdapter.AdminViewHolder>() {

    inner class AdminViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val image: ImageView = view.findViewById(R.id.adminProductImage)
        val name: TextView = view.findViewById(R.id.adminProductName)
        val price: TextView = view.findViewById(R.id.adminProductPrice)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteProduct)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.admin_product_item, parent, false)

        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: AdminViewHolder,
        position: Int
    ) {

        val product = list[position]

        // Set com.example.meatbazar.product.Product Name & Price
        holder.name.text = product.productName
        holder.price.text = "৳ ${product.productPrice}"

        // Load Image from URL using Glide
        Glide.with(context)
            .load(product.productImage)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.image)

        // Delete com.example.meatbazar.product.Product
        holder.btnDelete.setOnClickListener {

            FirebaseFirestore.getInstance()
                .collection("products")
                .document(product.productId)
                .delete()
                .addOnSuccessListener {

                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, list.size)

                    Toast.makeText(
                        context,
                        "com.example.meatbazar.product.Product Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        context,
                        "Failed to Delete com.example.meatbazar.product.Product",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}