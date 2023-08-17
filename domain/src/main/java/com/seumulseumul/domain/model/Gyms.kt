package com.seumulseumul.domain.model

data class Gyms(
    val climbingGyms: List<ClimbingGym>,

    val pagination: Pagination
)

data class ClimbingGym(
    val id: String,

    val address: String,

    val date: Date,

    val location: Location,

    val name: String,

    val phone: String,

    val placeId: String,

    val type: String
)