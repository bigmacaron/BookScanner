package kr.kro.fatcats.bookscanner.api

import kr.kro.fatcats.bookscanner.util.Constants
import kr.kro.fatcats.bookscanner.util.Preferences

class BookRepository {


    private val client = BookService.client

    suspend fun getVideoData(isbn:String) =
        client.getBookInfo(
            Preferences.id,
            Preferences.secret,
            isbn
        )

}