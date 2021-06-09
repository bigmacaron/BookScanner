package kr.kro.fatcats.bookscanner.api

class BookRepository {

    private val client = BookService.client

    suspend fun getVideoData(videoId: String?) = client.getBookInfo(videoId)
}