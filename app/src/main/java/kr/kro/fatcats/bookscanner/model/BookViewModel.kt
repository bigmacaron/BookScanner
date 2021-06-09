package kr.kro.fatcats.bookscanner.model

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.util.SingleLiveEvent

class BookViewModel(private val bookRepository: BookRepository?): ViewModel() {

    private val _barcodeData = SingleLiveEvent<String?>()

    val barcodeData : SingleLiveEvent<String?>
        get() = _barcodeData

    init {
        barcodeData.postValue(null)
    }

    fun load(){
        CoroutineScope(Dispatchers.IO).launch {
            bookRepository?.getVideoData("d")?.let {

            }
        }

    }

    companion object {
        private val TAG = BookViewModel::class.java.simpleName
    }
}