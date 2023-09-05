package com.seumulseumul.domain.model

data class Gyms(
    val climbingGyms: List<ClimbingGym>,

    val pagination: Pagination
)

data class ClimbingGym(
    val id: String,

    val location: Location,

    val place: Place,

    val type: String
)

data class Place(
    val addressName: String,

    val name: String,

    val parking: Boolean,

    val roadAddressName: String,

    val shower: Boolean
)