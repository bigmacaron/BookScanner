package kr.kro.fatcats.bookscanner.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BookModelFactory(private val repositoryBook: BookModelRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(BookModelRepository::class.java).newInstance(repositoryBook)
    }

}