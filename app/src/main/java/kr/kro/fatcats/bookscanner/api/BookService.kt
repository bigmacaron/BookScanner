package kr.kro.fatcats.bookscanner.api

import kr.kro.fatcats.bookscanner.util.Constants

object BookService {
    val CLIENT: BookApi = BookService.getClient(Constants.BASE_DOMAIN).create(BookApi::class.java)
}