package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharePrefrence (context: Context) {
    private val prefs : SharedPreferences = context.getSharedPreferences("user_prefs",Context.MODE_PRIVATE)

    fun setLoggedIn(isLoggedin :Boolean){
        val editor = prefs.edit()
        editor.putBoolean("isLoggedIn",isLoggedin)
        editor.apply()
    }

    fun isLoggedIn():Boolean{
        return prefs.getBoolean("isLoggedIn",false)
    }

    @SuppressLint("CommitPrefEdits")
    fun saveUserDetails(userDetails : Map<String,String>){
        val editor = prefs.edit()
        editor.putString("user",userDetails["username"])
        editor.putString("emailId",userDetails["email"])
        editor.putString("Phone",userDetails["phone"])
    }

    fun getUsername() : String{
        return prefs.getString("user","")?:""
    }
    fun getemail() : String{
        return prefs.getString("emailId","")?:""
    }
    fun getphone() : String{
        return prefs.getString("Phone","")?:""
    }
}