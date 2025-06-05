package com.makita.InventarioDirigido


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

      //   private const val BASE_URL = "http://172.16.1.213:3001/"   // local
      // private const val BASE_URL = "http://172.16.1.206:3024/"   // Servidor
       private const val BASE_URL = "http://172.16.1.234:3024/"   // dk-jherrera
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }



}