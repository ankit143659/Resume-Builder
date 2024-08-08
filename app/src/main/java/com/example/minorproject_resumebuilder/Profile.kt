package com.example.minorproject_resumebuilder


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64


@Suppress("DEPRECATION")
class Profile : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefKey = "profile_image"


    companion object{
        val IMAGE_REQUEST_CODE=100
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View= inflater.inflate(R.layout.fragment_profile, container, false)

       /* val name : TextView = view.findViewById(R.id.name)
        val email : TextView = view.findViewById(R.id.email)
        val phone : TextView = view.findViewById(R.id.ph)

        name.text= Dataholder.username ?: "N/A"
        email.text = Dataholder.email ?: "N/A"
        phone.text = Dataholder.ph ?: "N/A"*/


        val btn : Button = view.findViewById(R.id.logout)



        button = view.findViewById(R.id.profile)
        imageView = view.findViewById(R.id.profile_picture)


        button.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }



        btn.setOnClickListener {
            val intent = Intent(activity, LoginPage::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageView.setImageURI(data?.data)
            }
        }
    }


