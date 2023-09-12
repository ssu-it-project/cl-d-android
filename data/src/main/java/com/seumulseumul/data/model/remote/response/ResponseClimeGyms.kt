package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseClimeGyms(
    @SerializedName("climbing_gyms")
    val climbingGyms: List<ClimbingGym>,

    @SerializedName("pagination")
    val pagination: Pagination
)

data class ClimbingGym(
    @SerializedName("id")
    val id: String,

    @SerializedName("location")
    val location: Location,

    @SerializedName("place")
    val place: Place,

    @SerializedName("type")
    val type: String
)

data class Location(
    @SerializedName("distance")
    val distance: Double,

    @SerializedName("x")
    val x: Double,

    @SerializedName("y")
    val y: Double
)

data class Place(
    @SerializedName("address_name")
    val addressName: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("parking")
    val parking: Boolean,

    @SerializedName("road_address_name")
    val roadAddressName: String,

    @SerializedName("shower")
    val shower: Boolean
)
