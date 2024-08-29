package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date


class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDB.db"
        const val TABLE_USERS = "users"
        const val TABLE_RESUME = "resume"
        const val TABLE_PERSONAL = "personal"
        const val TABLE_EDUCATION = "education"
        const val TABLE_SKILL = "skill"
        const val TABLE_PROJECT = "project"
        const val TABLE_EXPERIENCE = "experience"
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
                        "$COLUMN_EMAIL TEXT UNIQUE);")

        private const val TABLE_RESUMEE = ("""
            CREATE TABLE $TABLE_RESUME (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                user_id INTEGER, 
                name TEXT, 
                created_date TEXT,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(id) ON DELETE CASCADE
            )
        """)
        private const val TABLE_PERSONALl = (
                """
            CREATE TABLE $TABLE_PERSONAL (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                name TEXT, 
                phone TEXT, 
                email TEXT, 
                nationality TEXT, 
                gender TEXT, 
                date_of_birth TEXT, 
                profile_image TEXT,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )
        private const val TABLE_EDUCATIONALL = (
                """
            CREATE TABLE $TABLE_EDUCATION (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                school_name TEXT, 
                passing_year TEXT, 
                grade TEXT,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )
        private const val TABLE_SKILLL = (
                """
            CREATE TABLE $TABLE_SKILL (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                skill_name TEXT, 
                strength TEXT,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )
        private const val TABLE_EXP = (
                """
            CREATE TABLE $TABLE_EXPERIENCE (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                company_name TEXT, 
                location TEXT, 
                years_of_experience INTEGER,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )
        private const val TABLE_PROJECTT = (
                """
            CREATE TABLE $TABLE_PROJECT (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                project_name TEXT, 
                project_description TEXT, 
                start_date TEXT, 
                end_date TEXT, 
                user_role TEXT,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )

    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
        db.execSQL(TABLE_RESUMEE)
        db.execSQL(TABLE_PERSONALl)
        db.execSQL(TABLE_EDUCATIONALL)
        db.execSQL(TABLE_SKILLL)
        db.execSQL(TABLE_EXP)
        db.execSQL(TABLE_PROJECTT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESUME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EDUCATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SKILL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPERIENCE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROJECT")
        onCreate(db)
    }

    fun checkUser(username: String, password: String): Map<String,String>? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))
        var userdetails:Map<String,String>?=null
        if(cursor.moveToFirst()){
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val user_id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))

            userdetails= mapOf(
                "username" to username,
                "email" to email,
                "phone" to phone,
                "user_id" to user_id
            )
        }


        cursor.close()
        db.close()

        return userdetails
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


    fun insertResume(userId: Long, name: String, createdDate: String): Int{
        val db = writableDatabase

            val values = ContentValues().apply {
                put("user_id", userId)
                put("name", name)
                put("created_date", createdDate)
            }
            return db.insert("resumes", null, values)

    }

    fun insertPersonalDetails(resumeId: Long, name: String, phone: String, email: String, nationality: String, gender: String, dateOfBirth: String, profileImage: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("name", name)
                put("phone", phone)
                put("email", email)
                put("nationality", nationality)
                put("gender", gender)
                put("date_of_birth", dateOfBirth)
                put("profile_image", profileImage)
            }
            val value = db.insert("personal_details", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }

    }


    fun insertEducationDetails(resumeId: Long, schoolName: String, passingYear: String, grade: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("school_name", schoolName)
                put("passing_year", passingYear)
                put("grade", grade)
            }
            val value = db.insert("education_details", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }

    }

    fun insertSkill(resumeId: Long, skillName: String, strength: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("skill_name", skillName)
                put("strength", strength)
            }
            val value = db.insert("skills", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }
    }

    fun insertExperience(resumeId: Long, companyName: String, location: String, yearsOfExperience: Int): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("company_name", companyName)
                put("location", location)
                put("years_of_experience", yearsOfExperience)
            }
            val value = db.insert("experience", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }
    }

    fun insertProject(resumeId: Long, projectName: String, projectDescription: String, startDate: String, endDate: String, userRole: String):Boolean{
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("project_name", projectName)
                put("project_description", projectDescription)
                put("start_date", startDate)
                put("end_date", endDate)
                put("user_role", userRole)
            }
            val value =  db.insert("projects", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }
    }

}