package com.example.jobquest.models

data class NewUserProfileModel(
    var UserID: String? = null,
    val userName: String? = null,
    val userEmail: String? = null,
    //val userCity: String? = null,
    val userType: String? = null
)
