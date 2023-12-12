package com.example.sportcomplexresourceoptimizationmobile.models

data class ServiceResponse(
    val items: List<ServiceItem>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)

// Доданий новий клас ServiceItem
data class ServiceItem(
    val id: String,
    val name: String
)

data class ServiceRequest(
    val name: String,
    val sportComplexId: String
)

data class ServiceUpdateRequest(
    val id: String,
    val name: String
)