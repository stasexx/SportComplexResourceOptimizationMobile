package com.example.sportcomplexresourceoptimizationmobile.models

data class EquipmentResponse(
    val items: List<EquipmentItem>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)

data class EquipmentItem(
    val id: String,
    val name: String,
    var status: Boolean
)

data class EquipmentRequest(
    val name: String
)

data class EquipmentUpdateRequest(
    val id: String,
    val name: String
)