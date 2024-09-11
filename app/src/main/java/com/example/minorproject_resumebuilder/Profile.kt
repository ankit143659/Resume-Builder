package com.example.minorproject_resumebuilder

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.Basic_personal_details.Companion.PICK_IMAGE_REQUEST
import com.example.minorproject_resumebuilder.Basic_personal_details.Companion.READ_EXTERNAL_STORAGE_REQUEST
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64

@Suppress("DEPRECATION")
class Profile : Fragment() {
    private lateinit var photo: Button
    private lateinit var profile: ImageView
    private lateinit var prefrence: SharePrefrence

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefrence = SharePrefrence(requireContext())

        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val btn: Button = view.findViewById(R.id.logout)
        val name: TextView = view.findViewById(R.id.name)
        val email: TextView = view.findViewById(R.id.email)
        val phone: TextView = view.findViewById(R.id.phone)
        photo = view.findViewById(R.id.profile)
        profile = view.findViewById(R.id.profile_picture)

        loadImageFromSharedPreferences()

        val Name: String = prefrence.getUsername()
        val Email: String = prefrence.getemail()
        val Phone: String = prefrence.getphone()

        name.text = Name
        email.text = Email
        phone.text = Phone

        checkAndRequestPermissions()

        photo.setOnClickListener {
            opengallery()
        }

        btn.setOnClickListener {
            showCustomDialogBox()
        }
        return view
    }

    private fun opengallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!

            // Convert image to bitmap
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Set image to ImageView
            profile.setImageBitmap(bitmap)

            // Save the image in SharedPreferences
            saveImageToSharedPreferences(bitmap)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveImageToSharedPreferences(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Convert to Base64
        val encodedImage = Base64.getEncoder().encodeToString(byteArray)

        prefrence.storeProfileImage(encodedImage)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadImageFromSharedPreferences() {
        val encodedImage = prefrence.getProfileImage()

        // Check if image is saved
        if (encodedImage != null && encodedImage.isNotEmpty()) {
            // Decode Base64 string back to byte array
            val decodedBytes = Base64.getDecoder().decode(encodedImage)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Set image to ImageView
            profile.setImageBitmap(bitmap)
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    requireContext(),
                    "Storage permission is needed to load images.",
                    Toast.LENGTH_LONG
                ).show()
            }

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                opengallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied. Cannot load image.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showCustomDialogBox() {
        val dialog = AlertDialog.Builder(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.log_out, null)
        dialog.setView(dialogView)

        val yes: Button = dialogView.findViewById(R.id.yes)
        val no: Button = dialogView.findViewById(R.id.no)

        val alertBox = dialog.create()

        yes.setOnClickListener {
            prefrence.setLoggedIn(false)
            val intent = Intent(activity, LoginPage::class.java)
            startActivity(intent)
            alertBox.dismiss()
        }

        no.setOnClickListener {
            alertBox.dismiss()
        }

        alertBox.show()
    }
}
