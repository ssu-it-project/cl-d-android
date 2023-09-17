package com.seumulseumul.domain.model

import java.io.Serializable

data class ClimeRecords(
    val pagination: Pagination,

    val records: List<Record>
): Serializable

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
): Serializable

data class Author(
    val id: String,

    val nickname: String,

    val profileImageUrl: String?
): Serializable

data class ClimbingGymInfo(
    val id: String,

    val name: String
): Serializable