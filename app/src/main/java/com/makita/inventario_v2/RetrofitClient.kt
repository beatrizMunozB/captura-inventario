package com.makita.inventario_v2

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://172.16.1.234:3024/"   // Servidor

    // Crear un interceptor de logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // Puedes cambiar a HEADERS o BASIC si quieres menos detalle
    }

    // Crear un cliente HTTP con el interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Crear instancia de Retrofit con el cliente que incluye el interceptor
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)  // <-- Se añade el cliente con el interceptor
        .build()

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
