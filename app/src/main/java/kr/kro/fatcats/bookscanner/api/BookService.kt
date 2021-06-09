package kr.kro.fatcats.bookscanner.api

import kr.kro.fatcats.bookscanner.util.Constants

object BookService {
    val client : BookApi = BaseService.getClient(Constants.BASE_DOMAIN).create(BookApi::class.java)
}