package com.example.sharktracker

// Data models for OCEARCH API responses

data class Shark(
    val id: String,
    val name: String,
    val species: String,
    val gender: String,
    val stage: String,
    val length: String,
    val weight: String,
    val tagLocation: String,
    val tagDate: String,
    val description: String,
    val profilePhoto: String,
    val tracker: Boolean = false
)

data class SharkPing(
    val id: String,
    val sharkId: String,
    val datetime: String,
    val latitude: Double,
    val longitude: Double,
    val depth: String?,
    val temperature: String?,
    val name: String,
    val species: String,
    val profilePhoto: String
)

data class SharkResponse(
    val sharks: List<Shark>
)

data class SharkPingResponse(
    val pings: List<SharkPing>
)