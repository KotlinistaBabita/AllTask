package com.example.pdfviewer.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdfviewer.R
import com.example.pdfviewer.utils.PermissionUtils


class ImageFragment : Fragment() {

    private lateinit var imagePreview: ImageView
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            imagePreview.setImageBitmap(bitmap)
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imagePreview.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imagePreview = view.findViewById(R.id.imagePreview)
        val btnCamera = view.findViewById<Button>(R.id.btnCamera)
        val btnGallery = view.findViewById<Button>(R.id.btnGallery)

        btnCamera.setOnClickListener {
            if (PermissionUtils.hasPermissions(requireContext(), Manifest.permission.CAMERA)) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    cameraLauncher.launch(intent)
                }
            } else {
                PermissionUtils.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
            }
        }

        btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }


}