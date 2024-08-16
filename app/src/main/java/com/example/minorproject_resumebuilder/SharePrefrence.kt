package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

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

    fun saveUserDetails(userDetails : Map<String,String>){
        val editor = prefs.edit()
        editor.putString("username",userDetails["username"])
        editor.putString("email",userDetails["email"])
        editor.putString("phone",userDetails["phone"])
    }

    fun getUsername() : String{
        return prefs.getString("username","")?:""
    }
    fun getemail() : String{
        return prefs.getString("email","")?:""
    }
    fun getphone() : String{
        return prefs.getString("phone","")?:""
    }
}