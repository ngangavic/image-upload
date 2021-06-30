package com.example.imageupload

import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.util.*

class DialogDisplayMultipleImagesViewModel : ViewModel() {

    var storage: FirebaseStorage

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    init {
        storage = Firebase.storage
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
                    _count.value=counting
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