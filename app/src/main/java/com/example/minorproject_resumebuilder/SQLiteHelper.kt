package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDB.db"
        const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"

        private const val TABLE_CREATE = (
                "CREATE TABLE $TABLE_USERS (" +
                        "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_USERNAME TEXT UNIQUE, " +
                        "$COLUMN_PASSWORD TEXT, " +
                        "$COLUMN_PHONE TEXT, " +
                        "$COLUMN_EMAIL TEXT);")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))

        val userExists = cursor.count > 0
        cursor.close()
        db.close()

        return userExists
    }

    fun getpasswordbyemail(email:String):String?{
        val db = this.readableDatabase
        var password : String? = null

        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_PASSWORD), "$COLUMN_EMAIL=?", arrayOf(email),
            null,null,null)


        if(cursor!=null && cursor.moveToFirst()){
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            cursor.close()
        }
        db.close()
        return password
    }



}