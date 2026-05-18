package com.example.meatbazar.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.R
import com.example.meatbazar.auth.LoginActivity
import com.example.meatbazar.product.AddProductActivity
import com.example.meatbazar.product.ProductListActivity
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnAddProduct = findViewById<Button>(R.id.btnAddProduct)
        val btnViewProducts = findViewById<Button>(R.id.btnViewProducts)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnAddProduct.setOnClickListener {

            startActivity(
                Intent(this, AddProductActivity::class.java)
            )
        }

        btnViewProducts.setOnClickListener {

            startActivity(
                Intent(this, ProductListActivity::class.java)
            )
        }

        btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }
}