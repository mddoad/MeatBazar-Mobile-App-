package com.example.meatbazar.product

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.meatbazar.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddProductActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var imagePreview: ImageView

    private var imageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etDescription = findViewById(R.id.etProductDescription)
        imagePreview = findViewById(R.id.imagePreview)

        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val btnAddProduct = findViewById<Button>(R.id.btnAddProduct)

        btnSelectImage.setOnClickListener {
            openGallery()
        }

        btnAddProduct.setOnClickListener {
            uploadProduct()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && data != null) {
            imageUri = data.data
            imagePreview.setImageURI(imageUri)
        }
    }

    private fun uploadProduct() {

        val dialog = ProgressDialog(this)
        dialog.setMessage("Uploading...")
        dialog.show()

        val reference = storage.reference
            .child("productImages")
            .child(System.currentTimeMillis().toString())

        imageUri?.let {

            reference.putFile(it)
                .addOnSuccessListener {

                    reference.downloadUrl.addOnSuccessListener { uri ->

                        val productId = firestore.collection("products")
                            .document().id

                        val map = hashMapOf(
                            "productId" to productId,
                            "productName" to etName.text.toString(),
                            "productPrice" to etPrice.text.toString(),
                            "productDescription" to etDescription.text.toString(),
                            "productImage" to uri.toString()
                        )

                        firestore.collection("products")
                            .document(productId)
                            .set(map)
                            .addOnSuccessListener {

                                dialog.dismiss()

                                Toast.makeText(this,
                                    "Product Added",
                                    Toast.LENGTH_SHORT).show()
                            }
                    }
                }
        }
    }
}