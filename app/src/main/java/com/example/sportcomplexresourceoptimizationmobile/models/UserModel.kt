package com.example.sportcomplexresourceoptimizationmobile.models

data class UserModel(
    val id: String,
    val roles: List<RoleModel>,
    val phone: String,
    val email: String,
    val passwordHash: String
)

data class UserListModel(
    val items: List<UserItemModel>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)

data class RoleModel(
    val id: String,
    val name: String
)

data class UserItemModel(
    val id: String,
    val roles: List<RoleModel>,
    val phone: String,
    val email: String,
    val passwordHash: String,
    val isDeleted: Boolean
)