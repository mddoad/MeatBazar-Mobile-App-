package com.example.meatbazar.auth
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.R
import com.example.meatbazar.model.UserModel
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

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val spRole = findViewById<Spinner>(R.id.spRole)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val roles = arrayOf("customer", "admin", "distributor")

        spRole.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        btnRegister.setOnClickListener {

            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = spRole.selectedItem.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val uid = auth.currentUser?.uid ?: ""

                    val user = UserModel(
                        uid,
                        name,
                        email,
                        role
                    )

                    firestore.collection("users")
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener {

                            Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}