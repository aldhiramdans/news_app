package id.co.code.newsapp.network

import id.co.code.newsapp.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("articlesearch.json")
    fun getNews(
        @Query("page") page: String,
        @Query("api-key") apikey: String
    ): Call<News>

    @GET("articlesearch.json")
    fun findNews(
        @Query("page") page: String,
        @Query("q") query: String,
        @Query("api-key") apikey: String
    ): Call<News>
}