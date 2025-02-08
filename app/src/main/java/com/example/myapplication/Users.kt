package com.example.myapplication

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Users (var userId: String? = "",
                     var username: String? = "",
                     var email: String? = "",
                     var password: String? = "") {

}