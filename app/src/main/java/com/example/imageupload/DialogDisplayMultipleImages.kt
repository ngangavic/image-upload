package com.example.imageupload

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageupload.databinding.DialogDisplayMulipleImagesBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class DialogDisplayMultipleImages(var imageAdapter: ImageAdapter, var uriArray: ArrayList<ImageModel>) : DialogFragment() {

    lateinit var binding: DialogDisplayMulipleImagesBinding

    lateinit var viewModel: DialogDisplayMultipleImagesViewModel

    val CHANNEL_ID="1000"
    lateinit var storage: FirebaseStorage


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(DialogDisplayMultipleImagesViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_display_muliple_images,
            container,
            false
        )

        binding.recyclerViewMultiImages.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewMultiImages.adapter = imageAdapter

        storage = Firebase.storage

        createNotificationChannel()

        binding.buttonUpload.setOnClickListener {
binding.buttonUpload.isEnabled=false
            for (i in uriArray.size until 0) {
                Log.e("URI", uriArray[i].uri)
                uploadImage(Uri.parse(uriArray[i].uri))
            }
        }

//        viewModel.count.observe(viewLifecycleOwner, Observer {
//            uploadNotification(uriArray.size,it)
//        })

        return binding.root
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun uploadNotification(progressMax:Int,progressCurrent:Int){
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Uploading")
//            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(requireContext()).apply {
            // Issue the initial notification with zero progress
            builder.setProgress(progressMax, progressCurrent, false)
            notify(100000, builder.build())

            // Do the job here that tracks the progress.
            // Usually, this should be in a
            // worker thread
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            // notificationManager.notify(notificationId, builder.build());

            // When done, update the notification one more time to remove the progress bar
            if (progressCurrent==uriArray.size) {
                builder.setContentText("Download complete")
                    .setProgress(0, 0, false)
                notify(100000, builder.build())
                binding.buttonUpload.isEnabled=true
            }
        }
    }

    fun uploadImage(uri: Uri) {
        Log.e("UPLOAD","STARTING")
        var counting=0
//        viewModelScope.launch {
        Log.e("UPLOAD","INSIDE")
        val ref = storage.reference.child("images/upload-app/" + UUID.randomUUID())
        val uploadTask = ref.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                counting++
                uploadNotification(uriArray.size,counting)
//                _count.value=counting
                val downloadUri = task.result
                //save uri
                Log.e("DOWNLOAD URL", downloadUri.toString())

            } else {
                //error
                Log.e("ERROR",task.exception!!.message.toString())
            }
        }
//        }
    }


}