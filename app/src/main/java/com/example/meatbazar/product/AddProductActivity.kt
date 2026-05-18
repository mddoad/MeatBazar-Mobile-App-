package com.example.meatbazar.product

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.meatbazar.R
import com.google.firebase.database.FirebaseDatabase

class AddProductActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var etImageUrl: EditText

    private lateinit var imagePreview: ImageView

    private lateinit var btnPreview: Button
    private lateinit var btnAddProduct: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etDescription = findViewById(R.id.etProductDescription)
        etImageUrl = findViewById(R.id.etImageUrl)

        imagePreview = findViewById(R.id.imagePreview)

        btnPreview = findViewById(R.id.btnPreviewImage)
        btnAddProduct = findViewById(R.id.btnAddProduct)

        // Preview Image
        btnPreview.setOnClickListener {

            val imageUrl = etImageUrl.text.toString().trim()

            if (imageUrl.isEmpty()) {

                Toast.makeText(
                    this,
                    "Enter Image URL",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imagePreview)

                Toast.makeText(
                    this,
                    "Preview Loaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Add Product
        btnAddProduct.setOnClickListener {

            addProduct()
        }
    }

    private fun addProduct() {

        val name = etName.text.toString().trim()
        val price = etPrice.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val imageUrl = etImageUrl.text.toString().trim()

        // Validation
        if (name.isEmpty()) {
            etName.error = "Enter Product Name"
            etName.requestFocus()
            return
        }

        if (price.isEmpty()) {
            etPrice.error = "Enter Product Price"
            etPrice.requestFocus()
            return
        }

        if (description.isEmpty()) {
            etDescription.error = "Enter Product Description"
            etDescription.requestFocus()
            return
        }

        if (imageUrl.isEmpty()) {
            etImageUrl.error = "Enter Image URL"
            etImageUrl.requestFocus()
            return
        }

        val databaseRef =
            FirebaseDatabase.getInstance()
                .getReference("Products")

        val productId = databaseRef.push().key!!

        // IMPORTANT
        val product = Product(
            productId,
            name,
            price,
            description,
            imageUrl
        )

        databaseRef.child(productId)
            .setValue(product)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Product Added Successfully",
                    Toast.LENGTH_LONG
                ).show()

                // Clear Fields
                etName.text.clear()
                etPrice.text.clear()
                etDescription.text.clear()
                etImageUrl.text.clear()

                imagePreview.setImageResource(0)
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed : ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}