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
        editor.putString("user_id",userDetails["user_id"])
        editor.apply()
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
    fun getuser_id() : String{
        return prefs.getString("user_id","")?:""
    }

    fun storeResumeId(Resume_id : Long){
        val editor = prefs.edit()
        editor.putLong("resume_id",Resume_id)
        editor.apply()
    }

    fun getResumeId():Long{
        return prefs.getLong("resume_id",1L)
    }

}