package kr.kro.fatcats.bookscanner.api

import android.util.Log
import kr.kro.fatcats.bookscanner.util.Constants
import kr.kro.fatcats.bookscanner.util.Preferences
import retrofit2.http.Query

class BookRepository {


    private val client = BookService.client

    suspend fun getVideoData(isbn:String) =
        client.getBookInfo(
            Preferences.certKey,
            Constants.BookApi.RESULT_STYLE,
            Constants.BookApi.PAGE_NO_DEFAULT,
            Constants.BookApi.PAGE_SIZE_DEFAULT,
            isbn
        )

}