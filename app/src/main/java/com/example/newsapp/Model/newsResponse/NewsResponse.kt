package com.example.newsapp.Model.newsResponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("articles")
    val newsList: List<News?>? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

data class BaseResponse<T>(
    val message: String? = null,
    val messageType : String? = null,// "error","success","warning"
    val status : String? = null,//"error","success","fail"
    val data:T? = null
)