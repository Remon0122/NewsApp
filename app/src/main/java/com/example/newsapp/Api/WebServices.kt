package com.example.newsapp.Api

import com.example.newsapp.Model.SourcesResponse.SourcesResponse
import com.example.newsapp.Model.newsResponse.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("v2/top-headlines/sources")
    fun getSources(@Query("category") catId: String):Call<SourcesResponse>

    @GET("v2/everything")
    fun getNews(
        @Query("sources") source:String,
    ):Call<NewsResponse>
}