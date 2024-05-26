package com.example.androidfundamental.api
import com.example.androidfundamental.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApiConfig {

    companion object{
        val BASE_URL = BuildConfig.BASE_URL
        val API_KEY = BuildConfig.API_KEY

        fun getApiService(): ApiService {
            Interceptor { chain ->
                val request = chain.request()

                val requestWithHeaders = request.newBuilder()
                    .addHeader("Authorization", API_KEY)
                    .build()
                chain.proceed(requestWithHeaders)
            }
            val logging =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}