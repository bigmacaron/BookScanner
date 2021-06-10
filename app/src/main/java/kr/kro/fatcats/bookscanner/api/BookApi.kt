package kr.kro.fatcats.bookscanner.api

import kr.kro.fatcats.bookscanner.model.BookInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BookApi {

    @GET("landingPage/SearchApi.do")
    suspend fun getBookInfo(
        @Query("cert_key") cert_key: String?,
        @Query("result_style") result_style : String?,
        @Query("page_no") page_no : String?,
        @Query("page_size") page_size : String?,
        @Query("isbn") isbn : String?
    ): Response<BookInfo?>
}