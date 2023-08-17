package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseClimeGyms(
    @SerializedName("climbing_gyms")
    val climbingGyms: List<ClimbingGym>,

    @SerializedName("pagination")
    val pagination: Pagination
)

data class ClimbingGym(
    @SerializedName("_id")
    val id: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("location")
    val location: Location,

    @SerializedName("name")
    val name: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("place_id")
    val placeId: String,

    @SerializedName("type")
    val type: String
)

data class Location(
    @SerializedName("coordinates")
    val coordinates: List<Int>,

    @SerializedName("type")
    val type: String
)
