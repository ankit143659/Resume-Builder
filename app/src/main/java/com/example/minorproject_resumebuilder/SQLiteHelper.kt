package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val TABLE_NAME = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
                + COLUMN_PHONE + " NUMBER UNIQUE NOT NULL," + " )")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(username : String, password : String, email:String, phone : String): Long {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME,username)
        contentValues.put(COLUMN_PASSWORD,password)
        contentValues.put(COLUMN_EMAIL,email)
        contentValues.put(COLUMN_PHONE,phone)

        val db = writableDatabase
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD"
        val selectionArgs = arrayOf(username,password)
        val cursor: Cursor = db.query( TABLE_NAME,null,selection,selectionArgs,null,null,null
        )

        val count = cursor.count>0
        cursor.close()
        return count
    }

    fun getUserDetails(username: String): Cursor? {
        val db = readableDatabase
        return db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_EMAIL, COLUMN_PHONE),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )
    }
}