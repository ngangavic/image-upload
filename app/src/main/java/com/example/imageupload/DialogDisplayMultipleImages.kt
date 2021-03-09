package com.example.imageupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageupload.databinding.DialogDisplayMulipleImagesBinding

class DialogDisplayMultipleImages(var imageAdapter: ImageAdapter) : DialogFragment() {

    lateinit var binding: DialogDisplayMulipleImagesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_display_muliple_images,
            container,
            false
        )

        binding.recyclerViewMultiImages.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewMultiImages.adapter = imageAdapter

        return binding.root
    }
}