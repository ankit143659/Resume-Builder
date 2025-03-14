package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.minorproject_resumebuilder.EducationDetail
import com.example.minorproject_resumebuilder.ExperienceDetail
import com.example.minorproject_resumebuilder.PersonalDetail
import com.example.minorproject_resumebuilder.ProjectDetail
import com.example.minorproject_resumebuilder.Resume_data
import com.example.minorproject_resumebuilder.SkillDetail
import com.example.minorproject_resumebuilder.downLoadedData
import java.util.Date


@Suppress("UNREACHABLE_CODE")
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "UserDB.db"
        const val TABLE_USERS = "users"
        //const val TABLE_ADMIN = "admin"
        const val TABLE_RESUME = "resumes"
        const val TABLE_PERSONAL = "personal"
        const val TABLE_EDUCATION = "education"
        const val TABLE_SKILL = "skill"
        const val TABLE_PROJECT = "project"
        const val TABLE_EXPERIENCE = "experience"
        const val TABLE_RESUME_TEMPELATE = "resume_template"
        private const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val TABLE_DOWNLOAD = "download"
        //const val COLUMN_ADMIN_USERNAME = "admin_username"
        //const val COLUMN_ADMIN_PASSWORD = "admin_password"

        private const val TABLE_CREATE = (
                "CREATE TABLE $TABLE_USERS (" +
                        "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_USERNAME TEXT UNIQUE, " +
                        "$COLUMN_PASSWORD TEXT NOT NULL, " +
                        "$COLUMN_PHONE TEXT UNIQUE NOT NULL, " +
                        "$COLUMN_EMAIL TEXT UNIQUE NOT NULL);" +
                        "isAdmin INTEGER DEFAULT 0);"
        )

        private const val TABLE_RESUMEE = ("""
            CREATE TABLE $TABLE_RESUME (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                user_id INTEGER NOT NULL, 
                name TEXT NOT NULL, 
                created_date TEXT NOT NULL  ,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(id) ON DELETE CASCADE
            )
        """)

        /*private const val TABLE_CREATE_ADMIN = ("""
            CREATE TABLE $TABLE_ADMIN (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ADMIN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_ADMIN_PASSWORD TEXT NOT NULL
            )
        """)*/

        private const val TABLE_PERSONALl = (
                """
            CREATE TABLE $TABLE_PERSONAL (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                fname TEXT, 
                lname TEXT, 
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
        private const val TABLE_EDUCATIONAL = (
                """
            CREATE TABLE $TABLE_EDUCATION (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                Degree_name TEXT, 
                Institute_name TEXT, 
                Location TEXT, 
                passing_year TEXT, 
                grade TEXT NOT NULL,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )
        private const val TABLE_SKILLl = (
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
                job_title TEXT, 
                company_name TEXT, 
                location TEXT, 
                years_of_experience INTEGER,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )
        private const val TABLE_PROJECTt = (
                """
            CREATE TABLE $TABLE_PROJECT (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                resume_id INTEGER, 
                project_name TEXT, 
                project_Url TEXT, 
                project_description TEXT, 
                start_date TEXT, 
                end_date TEXT, 
                user_role TEXT,
                FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
            )
        """
                )

        private const val TABLE_RESUME_TEMPLATE = (
                """
                    CREATE TABLE $TABLE_RESUME_TEMPELATE(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    resume_id INTEGER, 
                    template_name TEXT,
                    FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
                    )
                """
                )

        private const val TABLE_DOWNLOADED = (
                """
                    CREATE TABLE $TABLE_DOWNLOAD(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    resume_id,
                    resumeName TEXT,
                    createDate TEXT,
                    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
                    )
                """
                )
    }



    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
        db.execSQL(TABLE_RESUMEE)
       // db.execSQL(TABLE_CREATE_ADMIN)
        db.execSQL(TABLE_PERSONALl)
        db.execSQL(TABLE_EDUCATIONAL)
        db.execSQL(TABLE_SKILLl)
        db.execSQL(TABLE_EXP)
        db.execSQL(TABLE_PROJECTt)
        db.execSQL(TABLE_RESUME_TEMPLATE)
        db.execSQL(TABLE_DOWNLOADED)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
       // db.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESUME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EDUCATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SKILL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPERIENCE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROJECT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESUME_TEMPELATE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DOWNLOAD")
        onCreate(db)
    }

    /*fun insertAdmin(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADMIN_USERNAME, username)
            put(COLUMN_ADMIN_PASSWORD, password)
        }
        val result = db.insert(TABLE_ADMIN, null, values)
        db.close()
        return result != -1L
    }

    fun checkAdmin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_ADMIN WHERE $COLUMN_ADMIN_USERNAME = ? AND $COLUMN_ADMIN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        val isAdmin = cursor.count > 0
        cursor.close()
        db.close()

        return isAdmin
    }*/

    fun checkUser(username: String, password: String): Map<String,String>? {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))
        var userdetails:Map<String,String>?=null
        if(cursor.moveToFirst()){
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val user_id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
           // val isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1

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


    fun insertResume(userId: Long, name: String, createdDate: String): Long{
        val db = writableDatabase

            val values = ContentValues().apply {
                put("user_id", userId)
                put("name", name)
                put("created_date", createdDate)
            }
            return db.insert("resumes", null, values)

    }

    fun insertPersonalDetails(resumeId: Long?,fname: String,lname: String, phone: String, email: String, nationality: String, gender: String, dateOfBirth: String, profileImage: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("fname", fname)
                put("lname", lname)
                put("phone", phone)
                put("email", email)
                put("nationality", nationality)
                put("gender", gender)
                put("date_of_birth", dateOfBirth)
                put("profile_image", profileImage)
            }
            val value = db.insert("personal", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }

    }


    fun insertEducationDetails(resumeId: Long?, Degree_Name: String,Institute_Name: String,location: String, passingYear: String, grade: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("Degree_name", Degree_Name)
                put("Location", location)
                put("Institute_name", Institute_Name)
                put("passing_year", passingYear)
                put("grade", grade)
            }
            val value = db.insert("education", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }

    }

    fun insertSkill(resumeId: Long?, skillName: String, strength: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("skill_name", skillName)
                put("strength", strength)
            }
            val value = db.insert("skill", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }
    }

    fun insertExperience(resumeId: Long?,jobTitle:String,companyName: String, location: String, yearsOfExperience: String): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("job_title", jobTitle)
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

    fun insertProject(resumeId: Long?, projectName: String, projectDescription: String, projectUrl: String, startDate: String, endDate: String, userRole: String):Boolean{
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id", resumeId)
                put("project_name", projectName)
                put("project_Url", projectUrl)
                put("project_description", projectDescription)
                put("start_date", startDate)
                put("end_date", endDate)
                put("user_role", userRole)
            }
            val value =  db.insert("project", null, values)
            value !=-1L
        }catch (e:Exception){
            false
        }

    }

    fun storeResumeName (resumeId: Long?,template_name : String):Boolean{
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("resume_id",resumeId)
                put("template_name",template_name)
            }
            val result = db.insert(TABLE_RESUME_TEMPELATE,null,values)
            result!=-1L
        }catch (e:Exception){
            false
        }
    }

    fun getTemplateName(resumeId: Long?):String{
        val db = readableDatabase
        val cursor = db.query(
            "$TABLE_RESUME_TEMPELATE",
            null,
            "resume_id = ?",
            arrayOf(resumeId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()){
            val template_name = cursor.getString(cursor.getColumnIndexOrThrow("template_name"))
            template_name
        }else{
            cursor.close()
            "medical_1"
        }
    }

    fun getPersonalDetails(resumeId: Long?): PersonalDetail? {
        val db = readableDatabase
        val cursor = db.query(
            "$TABLE_PERSONAL",
            null,
            "resume_id = ?",
            arrayOf(resumeId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"))
            val lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val nationality = cursor.getString(cursor.getColumnIndexOrThrow("nationality"))
            val gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"))
            val dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"))
            val profileImage = cursor.getString(cursor.getColumnIndexOrThrow("profile_image"))
            cursor.close()
            PersonalDetail(fname,lname, phone, email, nationality, gender, dateOfBirth, profileImage)
        } else {
            cursor.close()
            null
        }
    }

    fun getAllEducationDetails(resumeId: Long?): List<EducationDetail> {
        val educationDetailsList = mutableListOf<EducationDetail>()
        val db = readableDatabase
        val cursor = db.query(
            "$TABLE_EDUCATION",
            null,
            "resume_id = ?",
            arrayOf(resumeId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val Degree_name = getString(getColumnIndexOrThrow("Degree_name"))
                val id = getLong(getColumnIndexOrThrow("id"))
                val institute_name = getString(getColumnIndexOrThrow("Institute_name"))
                val Location = getString(getColumnIndexOrThrow("Location"))
                val passingYear = getString(getColumnIndexOrThrow("passing_year"))
                val grade = getString(getColumnIndexOrThrow("grade"))
                educationDetailsList.add(EducationDetail(id,Degree_name,institute_name,Location,passingYear,grade))
            }
            close()
        }

        return educationDetailsList
    }

    fun getAllSkills(resumeId: Long?): List<SkillDetail> {
        val skillsList = mutableListOf<SkillDetail>()
        val db = readableDatabase
        val cursor = db.query(
            "$TABLE_SKILL",
            null,
            "resume_id = ?",
            arrayOf(resumeId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("id"))
                val skillName = getString(getColumnIndexOrThrow("skill_name"))
                val strength = getString(getColumnIndexOrThrow("strength"))
                skillsList.add(SkillDetail(id,skillName, strength))
            }
            close()
        }

        return skillsList
    }

    fun getAllExperienceDetails(resumeId: Long?): List<ExperienceDetail> {
        val experienceList = mutableListOf<ExperienceDetail>()
        val db = readableDatabase
        val cursor = db.query(
            "$TABLE_EXPERIENCE",
            null,
            "resume_id = ?",
            arrayOf(resumeId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("id"))
                val jobtitle = getString(getColumnIndexOrThrow("job_title"))
                val companyName = getString(getColumnIndexOrThrow("company_name"))
                val location = getString(getColumnIndexOrThrow("location"))
                val yearsOfExperience = getString(getColumnIndexOrThrow("years_of_experience"))
                experienceList.add(ExperienceDetail(id,jobtitle,companyName, location, yearsOfExperience))
            }
            close()
        }

        return experienceList
    }

    fun getAllProjectDetails(resumeId: Long?): List<ProjectDetail> {
        val projectList = mutableListOf<ProjectDetail>()
        val db = readableDatabase
        val cursor = db.query(
            "$TABLE_PROJECT",
            null,
            "resume_id = ?",
            arrayOf(resumeId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("id"))
                val projectName = getString(getColumnIndexOrThrow("project_name"))
                val projectDescription = getString(getColumnIndexOrThrow("project_description"))
                val startDate = getString(getColumnIndexOrThrow("start_date"))
                val endDate = getString(getColumnIndexOrThrow("end_date"))
                val userRole = getString(getColumnIndexOrThrow("user_role"))
                val projectUrl = getString(getColumnIndexOrThrow("project_Url"))
                projectList.add(ProjectDetail(id,projectName, projectDescription, startDate, endDate, userRole,projectUrl))
            }
            close()
        }

        return projectList
    }


    fun getAllResumes(userId: Long): List<Resume_data> {
    val resumes = mutableListOf<Resume_data>()
    val db = readableDatabase

    val selection = "user_id = ?"
    val selectionArgs = arrayOf(userId.toString())

    
    val cursor = db.query(
        TABLE_RESUME,
        null, 
        selection, 
        selectionArgs,
        null,
        null,
        null
    )

    with(cursor) {
        while (moveToNext()) {
            val id = getLong(getColumnIndexOrThrow("id")).toString()
            val resumeName = getString(getColumnIndexOrThrow("name"))
            val createDate = getString(getColumnIndexOrThrow("created_date"))
            val resume = Resume_data(id, resumeName, createDate)
            resumes.add(0, resume)
        }
    }

    cursor.close()
    return resumes
}



    fun deleteResume(resumeId: String):Int{
        val db = writableDatabase
        var totaldelete = 0

        totaldelete+=db.delete(TABLE_PERSONAL,"resume_id=?", arrayOf(resumeId))
        totaldelete+=db.delete(TABLE_EDUCATION,"resume_id=?", arrayOf(resumeId))
        totaldelete+=db.delete(TABLE_SKILL,"resume_id=?", arrayOf(resumeId))
        totaldelete+=db.delete(TABLE_PROJECT,"resume_id=?", arrayOf(resumeId))
        totaldelete+=db.delete(TABLE_EXPERIENCE,"resume_id=?", arrayOf(resumeId))
        totaldelete+=db.delete(TABLE_RESUME_TEMPELATE,"resume_id=?", arrayOf(resumeId))

        totaldelete+=db.delete(TABLE_RESUME,"id=?", arrayOf(resumeId))
        resetresumeIdSequences()

        return totaldelete
    }

    private fun resetresumeIdSequences() {
        val db = writableDatabase
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='resumes'")
    }



fun updatePersonalDetails(
    resumeId: Long?,
    fname: String,
    lname: String,
    phone: String,
    email: String,
    nationality: String,
    gender: String,
    dateOfBirth: String,
    profileImage: String
): Boolean {
    val db = writableDatabase
    return try {
        val values = ContentValues().apply {
            put("fname", fname)
            put("lname", lname)
            put("phone", phone)
            put("email", email)
            put("nationality", nationality)
            put("gender", gender)
            put("date_of_birth", dateOfBirth)
            put("profile_image", profileImage)
        }
        val rowsUpdated = db.update(
            TABLE_PERSONAL,
            values,
            "resume_id = ?",
            arrayOf(resumeId.toString())
        )
        rowsUpdated > 0
    } catch (e: Exception) {
        false
    } finally {
        db.close()
    }
}


fun updateEducationDetails(
    educationId: Long?,
    Degree_Name: String,
    Institute_Name: String,
    passingYear: String,
    grade: String,
    location: String
): Boolean {
    val db = writableDatabase
    return try {
        val values = ContentValues().apply {
            put("Degree_name", Degree_Name)
            put("Institute_name", Institute_Name)
            put("Location", location)
            put("passing_year", passingYear)
            put("grade", grade)
        }
        val rowsUpdated = db.update(
            TABLE_EDUCATION,
            values,
            "id = ?",
            arrayOf(educationId.toString())
        )
        rowsUpdated > 0
    } catch (e: Exception) {
        false
    } finally {
        db.close()
    }
}


fun updateSkill(
    skillId: Long?,
    skillName: String,
    strength: String
): Boolean {
    val db = writableDatabase
    return try {
        val values = ContentValues().apply {
            put("skill_name", skillName)
            put("strength", strength)
        }
        val rowsUpdated = db.update(
            TABLE_SKILL,
            values,
            "id = ?",
            arrayOf(skillId.toString())
        )
        rowsUpdated > 0
    } catch (e: Exception) {
        false
    } finally {
        db.close()
    }
}


fun updateExperience(
    experienceId: Long?,
    companyName: String,
    location: String,
    yearsOfExperience: String
): Boolean {
    val db = writableDatabase
    return try {
        val values = ContentValues().apply {
            put("company_name", companyName)
            put("location", location)
            put("years_of_experience", yearsOfExperience)
        }
        val rowsUpdated = db.update(
            TABLE_EXPERIENCE,
            values,
            "id = ?",
            arrayOf(experienceId.toString())
        )
        rowsUpdated > 0
    } catch (e: Exception) {
        false
    } finally {
        db.close()
    }
}


fun updateProject(
    projectId: Long?,
    projectName: String,
    projectDescription: String,
    projectUrl: String,
    startDate: String,
    endDate: String,
    userRole: String
): Boolean {
    val db = writableDatabase
    return try {
        val values = ContentValues().apply {
            put("project_name", projectName)
            put("project_description", projectDescription)
            put("project_Url", projectUrl)
            put("start_date", startDate)
            put("end_date", endDate)
            put("user_role", userRole)
        }
        val rowsUpdated = db.update(
            TABLE_PROJECT,
            values,
            "id = ?",
            arrayOf(projectId.toString())
        )
        rowsUpdated > 0
    } catch (e: Exception) {
        false
    } finally {
        db.close()
    }
}

    fun updateResumeTemplateName(resumeId: Long?,templateName : String):Boolean{
        val db = writableDatabase
        return try {
            val value = contentValuesOf().apply {
                put("template_name",templateName)
            }
            val rowUpdate = db.update(TABLE_RESUME_TEMPELATE,value,"resume_Id=?", arrayOf(resumeId.toString()))
            rowUpdate>0
        }catch (e:Exception){
            false
        }finally {
            db.close()
        }
    }

    fun deleteEducation(educationId: Long?):Boolean{
        val db = writableDatabase
        return try {
            val delete = db.delete(TABLE_EDUCATION,"id=?", arrayOf(educationId.toString()))
            delete >0
        }catch (e:Exception){
            false
        }
    }
    fun deleteExperience(expId: Long?):Boolean{
        val db = writableDatabase
        return try {
            val delete = db.delete(TABLE_EXPERIENCE,"id=?", arrayOf(expId.toString()))
            delete >0
        }catch (e:Exception){
            false
        }
    }
    fun deleteSkill(skillId: Long?):Boolean{
        val db = writableDatabase
        return try {
            val delete = db.delete(TABLE_SKILL,"id=?", arrayOf(skillId.toString()))
            delete >0
        }catch (e:Exception){
            false
        }
    }
    fun deleteProject(proId: Long?):Boolean{
        val db = writableDatabase
        return try {
            val delete = db.delete(TABLE_PROJECT,"id=?", arrayOf(proId.toString()))
            delete >0
        }catch (e:Exception){
            false
        }
    }

    fun addDownloadedResume(user_id : Long?,resumeId: Long?,resumeName : String? , createDate : String?):Boolean{
      val db= writableDatabase
      return try {
          val values = ContentValues().apply {
              put("user_id",user_id)
              put("resume_id",resumeId)
              put("resumeName",resumeName)
              put("createDate",createDate)
          }
          val insertt = db.insert(TABLE_DOWNLOAD,null,values)
          insertt !=-1L
      }catch (e:Exception){
          false
      }
    }


    fun getAllDownloaded(userId: Long): MutableList<downLoadedData> {
        val resumes = mutableListOf<downLoadedData>()
        val db = readableDatabase

        val selection = "user_id = ?"
        val selectionArgs = arrayOf(userId.toString())


        val cursor = db.query(
            TABLE_DOWNLOAD,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("id"))
                val resumeName = getString(getColumnIndexOrThrow("resumeName"))
                val createDate = getString(getColumnIndexOrThrow("createDate"))
                val resume = downLoadedData(id, resumeName, createDate)
                resumes.add(0, resume)
            }
        }

        cursor.close()
        return resumes
    }

    fun deleteDownloadedResume(proId: Long?):Boolean{
        val db = writableDatabase
        return try {
            val delete = db.delete(TABLE_DOWNLOAD,"id=?", arrayOf(proId.toString()))
            delete >0
        }catch (e:Exception){
            false
        }
    }

    fun isTableEmpty(): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_DOWNLOAD", null)
        var isEmpty = false
        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0
        }
        cursor.close()
        return isEmpty
    }

    fun resetAutoIncrement() {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            // Reset auto-increment by using VACUUM when table is empty
            db.execSQL("DELETE FROM sqlite_sequence WHERE name=$TABLE_DOWNLOAD")
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }





}