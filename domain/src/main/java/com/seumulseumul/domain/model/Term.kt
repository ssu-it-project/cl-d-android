package com.seumulseumul.domain.model

data class Term(
    val id: String,

    val name: String,

    val pageUrl: String,

    var agreed: Boolean = false
)