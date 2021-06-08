package kr.kro.fatcats.bookscanner.model

import androidx.lifecycle.ViewModel

class BookViewModel(private val bookModelRepository: BookModelRepository?): ViewModel() {


    companion object {
        private val TAG = BookViewModel::class.java.simpleName
    }
}