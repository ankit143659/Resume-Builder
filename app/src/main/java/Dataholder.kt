package com.example.minorproject_resumebuilder

data class PersonalDetail(
    var fname: String,
    var lname: String,
    var phone: String,
    var email: String,
    var nationality: String,
    var gender: String,
    var dateOfBirth: String,
    var profileImage: String?
)

data class EducationDetail(
    var education_id : Long,
    var Degree_name: String,
    var Institute_name: String,
    var Location: String,
    var passingYear: String,
    var grade: String
)

data class SkillDetail(
    var skill_id : Long,
    var skillName: String,
    var strength: String
)

data class ExperienceDetail(
    var experience_id : Long,
    var jobTitle : String,
    var companyName: String,
    var location: String,
    var yearsOfExperience: String
)

data class ProjectDetail(
    var project_id : Long,
    var projectName: String,
    var projectDescription: String,
    var startDate: String,
    var endDate: String,
    var userRole: String,
    var projectUrl: String
)

data class Resume_data(
    val id : String,
    val name: String,
    val creationDate: String
)

data class downLoadedData(
    val id : Long,
    val resumeName: String,
    val createDate : String
)


