package com.example.meatbazar.adapter

import android.content.Context
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meatbazar.R
import com.example.meatbazar.model.CartModel
import com.example.meatbazar.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val image = view.findViewById<ImageView>(R.id.productImage)
        val name = view.findViewById<TextView>(R.id.productName)
        val price = view.findViewById<TextView>(R.id.productPrice)
        val btnCart = view.findViewById<Button>(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.product_item, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {

        val product = list[position]

        holder.name.text = product.productName
        holder.price.text = "৳ ${product.productPrice}"

        Glide.with(context)
            .load(product.productImage)
            .into(holder.image)

        holder.btnCart.setOnClickListener {

            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            val cart = CartModel(
                product.productId,
                product.productName,
                product.productPrice,
                product.productImage,
                1
            )

            FirebaseFirestore.getInstance()
                .collection("cart")
                .document(uid)
                .collection("items")
                .document(product.productId)
                .set(cart)
                .addOnSuccessListener {

                    Toast.makeText(
                        context,
                        "Added To Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}