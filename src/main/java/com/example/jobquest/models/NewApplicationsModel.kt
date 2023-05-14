package com.example.jobquest.models

import android.widget.EditText
import com.example.jobquest.R

data class NewApplicationsModel(
    var applicationId: String? = null,
    var userId: String? = null,
    val name: String? = null,
    val age: String? = null,
    val city: String? = null,
    val jobTitle: String? = null,
    val jobBudget: String? = null,
    val employer: String? = null,
    val employerID: String? = null,
    val paymentType: String? = null,
    val expectedSalary: String? = null,
    val chargingRate: String? = null,
    val status: String? = "IN REVIEW"
)
