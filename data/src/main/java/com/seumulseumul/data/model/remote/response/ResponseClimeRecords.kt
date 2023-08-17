package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseClimeRecords(
    @SerializedName("pagination")
    val pagination: Pagination,

    @SerializedName("records")
    val records: List<Record>
)

data class Record(
    @SerializedName("author")
    val author: Author,

    @SerializedName("climbing_gym_info")
    val climbingGymInfo: ClimbingGymInfo,

    @SerializedName("content")
    val content: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("id")
    val id: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("level")
    val level: String,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("sector")
    val sector: String,

    @SerializedName("video")
    val video: String,

    @SerializedName("view_count")
    val viewCount: Int
)

data class Author(
    @SerializedName("id")
    val id: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profile_image_url")
    val profileImageUrl: String
)

data class ClimbingGymInfo(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)