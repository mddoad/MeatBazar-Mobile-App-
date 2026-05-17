package com.example.meatbazar.auth
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.R
import com.example.meatbazar.dashboard.AdminDashboardActivity
import com.example.meatbazar.dashboard.CustomerDashboardActivity
import com.example.meatbazar.dashboard.DistributorDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

                    firestore.collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { document ->

                            val role = document.getString("role")

                            when(role) {
                                "admin" -> {
                                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                                }

                                "distributor" -> {
                                    startActivity(Intent(this, DistributorDashboardActivity::class.java))
                                }

                                else -> {
                                    startActivity(Intent(this, CustomerDashboardActivity::class.java))
                                }
                            }

                            finish()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}