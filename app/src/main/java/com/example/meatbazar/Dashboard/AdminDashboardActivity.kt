package com.example.meatbazar.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.R
import com.example.meatbazar.auth.LoginActivity
import com.example.meatbazar.order.AdminOrdersActivity
import com.example.meatbazar.product.AddProductActivity
import com.example.meatbazar.product.ManageProductsActivity
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnAddProduct = findViewById<Button>(R.id.btnAddProduct)
        val btnManageProducts = findViewById<Button>(R.id.btnManageProducts)
        val btnViewOrders = findViewById<Button>(R.id.btnViewOrders)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnAddProduct.setOnClickListener {

            startActivity(
                Intent(this, AddProductActivity::class.java)
            )
        }

        btnManageProducts.setOnClickListener {

            startActivity(
                Intent(this, ManageProductsActivity::class.java)
            )
        }

        btnViewOrders.setOnClickListener {

            startActivity(
                Intent(this, AdminOrdersActivity::class.java)
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