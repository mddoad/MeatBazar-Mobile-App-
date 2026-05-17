package com.example.meatbazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.MainActivity
import com.example.meatbazar.R
import com.example.meatbazar.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        firestore = FirebaseFirestore.getInstance()

        val nameEt = findViewById<EditText>(R.id.nameEt)

        val emailEt = findViewById<EditText>(R.id.emailEt)

        val passwordEt = findViewById<EditText>(R.id.passwordEt)

        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {

            val name = nameEt.text.toString().trim()

            val email = emailEt.text.toString().trim()

            val password = passwordEt.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                email,
                password
            ).addOnSuccessListener {

                val uid = auth.currentUser!!.uid

                val user = User(
                    uid,
                    name,
                    email,
                    "USER"
                )

                firestore.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Registration Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )

                        finish()

                    }

            }.addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

    }
}