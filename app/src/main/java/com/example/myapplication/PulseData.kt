package com.example.myapplication

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PulseData(
    val id: String? = "",
    val userId: String? = "",
    val userName: String?= null,
    val pulse_rate: Int? = null, // Burada pulse_rate olarak değiştirildi
    val timestamp: String? = null,
    val result: Int? = null
)