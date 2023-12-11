package com.example.sportcomplexresourceoptimizationmobile.models

data class SportComplexModel(
    val items: List<SportComplexItem>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)

data class SportComplexItem(
    val id: String,
    val name: String,
    val email: String,
    val city: String,
    val address: String,
    val description: String?,
    val operatingHours: String,
    val rating: Double,
    val createdById: String
)

data class SportComplexRequest(
    val name: String,
    val email: String,
    val city: String,
    val address: String,
    val description: String,
    val operatingHours: String
)