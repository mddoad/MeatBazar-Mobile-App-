package com.example.meatbazar.adapter

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meatbazar.R
import com.example.meatbazar.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class AdminProductAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
) : RecyclerView.Adapter<AdminProductAdapter.AdminViewHolder>() {

    inner class AdminViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val image = view.findViewById<ImageView>(R.id.adminProductImage)
        val name = view.findViewById<TextView>(R.id.adminProductName)
        val price = view.findViewById<TextView>(R.id.adminProductPrice)
        val btnDelete = view.findViewById<Button>(R.id.btnDeleteProduct)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.admin_product_item, parent, false)

        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: AdminViewHolder,
        position: Int
    ) {

        val product = list[position]

        holder.name.text = product.productName
        holder.price.text = "৳ ${product.productPrice}"

        Glide.with(context)
            .load(product.productImage)
            .into(holder.image)

        holder.btnDelete.setOnClickListener {

            FirebaseFirestore.getInstance()
                .collection("products")
                .document(product.productId)
                .delete()
                .addOnSuccessListener {

                    list.removeAt(position)
                    notifyDataSetChanged()

                    Toast.makeText(
                        context,
                        "Product Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}