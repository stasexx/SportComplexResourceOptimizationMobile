package com.example.sportcomplexresourceoptimizationmobile.models

data class UserModel(
    val id: String,
    val roles: List<RoleModel>,
    val phone: String,
    val email: String,
    val passwordHash: String
)

data class RoleModel(
    val id: String,
    val name: String
)