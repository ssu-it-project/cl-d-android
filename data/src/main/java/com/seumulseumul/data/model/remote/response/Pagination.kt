package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("limit")
    val limit: Int,

    @SerializedName("skip")
    val skip: Int,

    @SerializedName("total")
    val total: Int
)