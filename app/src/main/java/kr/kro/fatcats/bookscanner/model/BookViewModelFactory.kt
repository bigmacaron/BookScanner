package kr.kro.fatcats.bookscanner.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.kro.fatcats.bookscanner.api.BookRepository

class BookViewModelFactory(private val repository: BookRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(BookRepository::class.java).newInstance(repository)
    }
}