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
    var Degree_name: String,
    var Institute_name: String,
    var Location: String,
    var passingYear: String,
    var grade: String
)

data class SkillDetail(
    var skillName: String,
    var strength: String
)

data class ExperienceDetail(
    var companyName: String,
    var location: String,
    var yearsOfExperience: String
)

data class ProjectDetail(
    var projectName: String,
    var projectDescription: String,
    var startDate: String,
    var endDate: String,
    var userRole: String,
    var projectUrl: String
)

data class Resume_data(
    val name: String,
    val creationDate: String
)


