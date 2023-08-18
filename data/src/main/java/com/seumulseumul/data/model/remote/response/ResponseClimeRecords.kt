package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseClimeRecords(
    @SerializedName("pagination")
    val pagination: Pagination,

    // 서버 측 오타. 추후 변경 가능
    @SerializedName("Records")
    val records: List<Record>
)

data class Record(
    @SerializedName("id")
    val id: String,

    @SerializedName("author")
    val author: Author,

    @SerializedName("climbing_gym_info")
    val climbingGymInfo: ClimbingGymInfo,

    @SerializedName("sector")
    val sector: String,

    @SerializedName("level")
    val level: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("video")
    val video: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("view_count")
    val viewCount: Int,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("date")
    val date: Date,
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