package com.example.imageupload

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.imageupload.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private val REQUEST_IMAGES_GALLERY = 3

    private lateinit var uriArray: MutableList<ImageModel>

    private lateinit var imageAdapter: ImageAdapter

    private fun openImages() {
        imageAdapter = ImageAdapter(uriArray as ArrayList<ImageModel>, this)
        val dialogDisplayMultipleImages = DialogDisplayMultipleImages(imageAdapter)
        dialogDisplayMultipleImages.show(supportFragmentManager, "show images")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.imageViewPhoto.visibility = View.GONE

        binding.buttonTakePicture.setOnClickListener { takePicture() }

        binding.buttonGetPicture.setOnClickListener { getFromGallery() }

        binding.buttonGetPictures.setOnClickListener { getMultipleFromGallery() }

        uriArray = ArrayList()
    }

    private fun getMultipleFromGallery() {
        val getMultiplePictureIntent =
            Intent(Intent.ACTION_GET_CONTENT).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                .setType("image/*")
        try {
            startActivityForResult(
                Intent.createChooser(getMultiplePictureIntent, "Select Picture"),
                REQUEST_IMAGES_GALLERY
            )
        } catch (e: ActivityNotFoundException) {
            Log.e("ERROR:", e.message.toString())
        }
    }

    private fun getFromGallery() {
        val getPictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(getPictureIntent, REQUEST_IMAGE_GALLERY)
        } catch (e: ActivityNotFoundException) {
            Log.e("ERROR:", e.message.toString())
        }
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Log.e("ERROR:", e.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (data != null && resultCode == RESULT_OK) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    binding.imageViewPhoto.setImageBitmap(imageBitmap)
                    binding.imageViewPhoto.visibility = View.VISIBLE
                }
            }
            REQUEST_IMAGE_GALLERY -> {
                if (data != null && resultCode == RESULT_OK) {
                    val uri: Uri = data.data!!
                    Picasso.get().load(uri.toString()).into(binding.imageViewPhoto)
                    binding.imageViewPhoto.visibility = View.VISIBLE
                }
            }
            REQUEST_IMAGES_GALLERY -> {
                if (data != null && resultCode == RESULT_OK) {
                    if (data.clipData != null) {
                        uriArray.clear()
                        for (i in 0 until data.clipData!!.itemCount) {
                            val uri = data.clipData!!.getItemAt(i).uri
                            uriArray.add(ImageModel(uri.toString()))
                        }
                        Log.e("SIZE", uriArray.size.toString())
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "You selected " + uriArray.size.toString() + " images",
                            Snackbar.LENGTH_LONG
                        ).show()
                        openImages()
                    } else {
                        val uri: Uri = data.data!!
                        Picasso.get().load(uri.toString()).into(binding.imageViewPhoto)
                        binding.imageViewPhoto.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


}