package com.example.meatbazar.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.MainActivity
import com.example.meatbazar.R
import com.example.meatbazar.admin.AdminDashboardActivity
import com.example.meatbazar.data.FirebaseManager
import com.example.meatbazar.distributor.DistributorDashboardActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val currentUser = FirebaseManager.auth.currentUser

            if (currentUser == null) {

                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()

            } else {

                FirebaseManager.firestore
                    .collection("users")
                    .document(currentUser.uid)
                    .get()
                    .addOnSuccessListener {

                        val role = it.getString("role")

                        when (role) {

                            "ADMIN" -> {
                                startActivity(
                                    Intent(
                                        this,
                                        AdminDashboardActivity::class.java
                                    )
                                )
                            }

                            "DISTRIBUTOR" -> {
                                startActivity(
                                    Intent(
                                        this,
                                        DistributorDashboardActivity::class.java
                                    )
                                )
                            }

                            else -> {
                                startActivity(
                                    Intent(
                                        this,
                                        MainActivity::class.java
                                    )
                                )
                            }
                        }

                        finish()
                    }
            }

        }, 2000)
    }
}