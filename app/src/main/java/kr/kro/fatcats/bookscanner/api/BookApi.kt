package kr.kro.fatcats.bookscanner.api

import kr.kro.fatcats.bookscanner.model.BookInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BookApi {

    @GET("v1/search/book_adv")
    suspend fun getBookInfo(
        @Header("X-Naver-Client-Id") id:String,
        @Header("X-Naver-Client-Secret") secret:String,
        @Query("d_isbn") isbn : String?
    ): Response<BookInfo?>
}