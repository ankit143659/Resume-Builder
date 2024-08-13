package com.example.minorproject_resumebuilder


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
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
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64


@Suppress("DEPRECATION")
class Profile : Fragment() {

    private lateinit var dbHelper: SQLiteHelper

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefKey = "profile_image"


    companion object{
        val IMAGE_REQUEST_CODE=100
        fun newInstance(username: String): Profile {
            val fragment = Profile()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View= inflater.inflate(R.layout.fragment_profile, container, false)
        val btn : Button = view.findViewById(R.id.logout)
        val name : Button = view.findViewById(R.id.name)
        val email : Button = view.findViewById(R.id.email)
        val phone : Button = view.findViewById(R.id.phone)

        val username = arguments?.getString("username")

        /*if ( username!= null) {
            val user = dbHelper.getUserDetails(username)
            if (user != null) {
                name.text = user.username
                email.text = user.email
                phone.text= user.phone

            }
        }*/



        button = view.findViewById(R.id.profile)
        imageView = view.findViewById(R.id.profile_picture)


        button.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }



        btn.setOnClickListener {
            showCustomDailogBox()
        }
        return view
    }


    fun showCustomDailogBox(){
        val dialog = AlertDialog.Builder(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.log_out,null)
        dialog.setView(dialogView)

        val yes : Button = dialogView.findViewById(R.id.yes)
        val no : Button = dialogView.findViewById(R.id.no)

        val alertBox = dialog.create()

        yes.setOnClickListener{
            val intent= Intent(activity,LoginPage::class.java)
            startActivity(intent)
            alertBox.dismiss()
        }

        no.setOnClickListener{
            alertBox.dismiss()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageView.setImageURI(data?.data)
            }
        }


    }


