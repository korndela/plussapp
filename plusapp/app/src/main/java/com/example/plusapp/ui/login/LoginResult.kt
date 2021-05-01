package com.example.plusapp.ui.login

import com.google.firebase.auth.FirebaseUser

data class LoginResult(
    val success: FirebaseUser? = null,
    val error: String? = null
)