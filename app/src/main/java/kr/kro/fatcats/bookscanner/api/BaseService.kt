package kr.kro.fatcats.bookscanner.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseService {
    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .setLenient()
        .create()

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ResponseInterceptor())
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getClient(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}