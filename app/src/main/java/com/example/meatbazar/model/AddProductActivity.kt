package com.example.meatbazar

import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class AddProductActivity : AppCompatActivity() {
    var etName: EditText? = null
    var etPrice: EditText? = null
    var etDescription: EditText? = null
    var btnSelectImage: Button? = null
    var btnAddProduct: Button? = null
    var imagePreview: ImageView? = null

    var imageUri: Uri? = null

    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null

    private val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        etName = findViewById<EditText?>(R.id.etProductName)
        etPrice = findViewById<EditText?>(R.id.etProductPrice)
        etDescription = findViewById<EditText?>(R.id.etProductDescription)
        btnSelectImage = findViewById<Button?>(R.id.btnSelectImage)
        btnAddProduct = findViewById<Button?>(R.id.btnAddProduct)
        imagePreview = findViewById<ImageView?>(R.id.imagePreview)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        btnSelectImage!!.setOnClickListener(View.OnClickListener { v: View? -> openGallery() })

        btnAddProduct!!.setOnClickListener(View.OnClickListener { v: View? -> uploadProduct() })
    }

    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && data != null) {
            imageUri = data.getData()
            imagePreview!!.setImageURI(imageUri)
        }
    }

    private fun uploadProduct() {
        val name = etName!!.getText().toString()
        val price = etPrice!!.getText().toString()
        val description = etDescription!!.getText().toString()

        val dialog = ProgressDialog(this)
        dialog.setMessage("Uploading Product...")
        dialog.show()

        val reference = storage!!.getReference()
            .child("product_images")
            .child(System.currentTimeMillis().toString() + ".jpg")

        reference.putFile(imageUri!!)
            .addOnSuccessListener(OnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                reference.getDownloadUrl().addOnSuccessListener(OnSuccessListener { uri: Uri? ->
                    val productId = firestore!!.collection("products")
                        .document().getId()
                    val map = HashMap<String?, Any?>()

                    map.put("productId", productId)
                    map.put("productName", name)
                    map.put("productPrice", price)
                    map.put("productDescription", description)
                    map.put("productImage", uri.toString())
                    firestore!!.collection("products")
                        .document(productId)
                        .set(map)
                        .addOnSuccessListener(OnSuccessListener { unused: Void? ->
                            dialog.dismiss()
                            Toast.makeText(
                                this,
                                "Product Added",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                })
            })
    }
}