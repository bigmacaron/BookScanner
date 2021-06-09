package kr.kro.fatcats.bookscanner.api

import retrofit2.Response
import retrofit2.http.*

interface BookApi {

    @GET("v/video/details")
    suspend fun getBookInfo(
        @Query("isbn") isbn: String?
    ): Response<String?>
}