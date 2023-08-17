package com.seumulseumul.domain.model

data class Pagination(
    val limit: Int,

    val skip: Int,

    val total: Int
)