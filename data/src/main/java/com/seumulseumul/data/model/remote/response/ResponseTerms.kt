package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseTerms(
    @SerializedName("terms")
    val terms: List<Term>
)

data class Term(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("page_url")
    val pageUrl: String
)