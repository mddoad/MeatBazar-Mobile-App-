package com.example.meatbazar.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meatbazar.R
import com.example.meatbazar.adapter.AdminProductAdapter
import com.example.meatbazar.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ManageProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var adapter: AdminProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        recyclerView = findViewById(R.id.recyclerManageProducts)

        productList = arrayListOf()

        adapter = AdminProductAdapter(this, productList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadProducts()
    }

    private fun loadProducts() {

        FirebaseFirestore.getInstance()
            .collection("products")
            .get()
            .addOnSuccessListener {

                productList.clear()

                for (document in it.documents) {

                    val product = document.toObject(Product::class.java)

                    if (product != null) {
                        productList.add(product)
                    }
                }

                adapter.notifyDataSetChanged()
            }
    }
}