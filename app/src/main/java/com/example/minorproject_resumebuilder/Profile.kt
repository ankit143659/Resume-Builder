package com.example.minorproject_resumebuilder


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64


@Suppress("DEPRECATION")
class Profile : Fragment() {

    private lateinit var prefrence: SharePrefrence
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        prefrence=SharePrefrence(requireContext())

        val view :View= inflater.inflate(R.layout.fragment_profile, container, false)
        val btn : Button = view.findViewById(R.id.logout)
       val name : Button = view.findViewById(R.id.name)
        val email : Button = view.findViewById(R.id.email)
        val phone : Button = view.findViewById(R.id.phone)

        name.text=prefrence.getUsername()
        email.text=prefrence.getemail()
        phone.text=prefrence.getphone()

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
            prefrence.setLoggedIn(false)
            val intent= Intent(activity,LoginPage::class.java)
            startActivity(intent)
            alertBox.dismiss()
        }

        no.setOnClickListener{
            alertBox.dismiss()
        }

        alertBox.show()

    }



    }


