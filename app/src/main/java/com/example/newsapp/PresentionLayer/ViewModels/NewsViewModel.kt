package com.example.newsapp.PresentionLayer.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.DataLayer.Api.Model.SourcesResponse.Source
import com.example.newsapp.DataLayer.Api.Model.SourcesResponse.SourcesResponse
import com.example.newsapp.DataLayer.Api.Model.newsResponse.News
import com.example.newsapp.DataLayer.Api.Model.newsResponse.NewsResponse
import com.example.newsapp.DataLayer.Api.WebServices
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val apiServices: WebServices
): ViewModel(){
    private val _sources = MutableLiveData<List<Source>>()
    val sources: LiveData<List<Source>> = _sources

    private val _newsList = MutableLiveData<List<News>>()
    val newsList : LiveData<List<News>> = _newsList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadSources(categoryId: String) {
        _loading.value = true
        apiServices.getSources(categoryId).enqueue(object : Callback<SourcesResponse> {
            override fun onResponse(call: Call<SourcesResponse>, response: Response<SourcesResponse>) {
                _loading.value = false

                if (response.isSuccessful) {
                    val sourcesList = response.body()?.sources?.filterNotNull() ?: emptyList()
                    _sources.value = sourcesList

                    // Log to debug API response
                    Log.d("NewsViewModel", "Sources loaded: ${sourcesList.size}")
                    sourcesList.forEach { Log.d("Source", "${it.id} - ${it.name}") }
                } else {
                    val errorMsg = "Error loading sources: ${response.code()} - ${response.message()}"
                    _error.value = errorMsg
                    Log.e("NewsViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                _loading.value = false
                val failMsg = t.localizedMessage ?: "Unknown error"
                _error.value = failMsg
                Log.e("NewsViewModel", "Failed to load sources: $failMsg")
            }
        })
    }


    fun loadNews(sourceId: String){
        _loading.value = true

        apiServices.getNews(source = sourceId).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _newsList.value = response.body()?.newsList?.filterNotNull() ?: emptyList()
                    Log.e("NewsViewModel", "News Response: ${Gson().toJson(response.body())}")
                } else {
                    _error.value = "Error loading news"
                }
            }

            override fun onFailure(call: Call<NewsResponse?>, t: Throwable) {
                _loading.value = false
                _error.value = t.localizedMessage ?: "UnKnown error"
            }
        })
    }
    fun clearError() {
        _error.value = null
    }
}