package com.example.meatbazar.dashboard

import com.example.meatbazar.product.Product
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meatbazar.R
import com.example.meatbazar.adapter.ProductAdapter
import com.example.meatbazar.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustomerDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        recyclerView = findViewById(R.id.recyclerProducts)

        productList = arrayListOf()

        adapter = ProductAdapter(this, productList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadProducts()

        btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
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