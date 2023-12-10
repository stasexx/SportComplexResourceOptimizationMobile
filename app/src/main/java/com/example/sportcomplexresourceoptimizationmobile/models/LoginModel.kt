package com.example.sportcomplexresourceoptimizationmobile.models

data class LoginModel(
    val id: String?,
    val email: String?,
    val roles: List<String>,
    val password: String?,
    val token: String
)