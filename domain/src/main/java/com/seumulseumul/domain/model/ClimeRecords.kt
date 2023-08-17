package com.seumulseumul.domain.model

data class ClimeRecords(
    val pagination: Pagination,

    val records: List<Record>
)

data class Record(
    val author: Author,

    val climbingGymInfo: ClimbingGymInfo,

    val content: String,

    val date: Date,

    val id: String,

    val image: String,

    val level: String,

    val likeCount: Int,

    val sector: String,

    val video: String,

    val viewCount: Int
)

data class Author(
    val id: String,

    val nickname: String,

    val profileImageUrl: String
)

data class ClimbingGymInfo(
    val id: String,

    val name: String
)