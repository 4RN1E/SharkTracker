package com.example.sharktracker

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface OcearchApiService {

    @GET("sharks")
    suspend fun getSharks(): Response<SharkResponse>

    @GET("sharks/pings")
    suspend fun getSharkPings(): Response<SharkPingResponse>

    @GET("sharks/pings")
    suspend fun getSharkPings(@Query("id") sharkId: String): Response<SharkPingResponse>

    companion object {
        private const val BASE_URL = "https://www.ocearch.org/api/v1/"

        fun create(): OcearchApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OcearchApiService::class.java)
        }
    }
}