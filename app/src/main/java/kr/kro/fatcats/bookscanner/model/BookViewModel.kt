package kr.kro.fatcats.bookscanner.model

import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.util.SingleLiveEvent

class BookViewModel(private val bookRepository: BookRepository?): ViewModel() {

    private val _barcodeData = SingleLiveEvent<String?>()
    private val _bookInfo = SingleLiveEvent<BookInfo?>()
    private val _bookTitle = SingleLiveEvent<String?>()
    private val _bookAuthor = SingleLiveEvent<String?>()
    private val _bookUrl = SingleLiveEvent<String?>()
    private val _bookPublisher = SingleLiveEvent<String?>()

    val barcodeData : SingleLiveEvent<String?>
        get() = _barcodeData
    val bookInfo : SingleLiveEvent<BookInfo?>
        get() = _bookInfo
    val bookTitle : SingleLiveEvent<String?>
        get() =  _bookTitle
    val bookAuthor : SingleLiveEvent<String?>
        get() =  _bookAuthor
    val bookUrl : SingleLiveEvent<String?>
        get() =  _bookUrl
    val bookPublisher : SingleLiveEvent<String?>
        get() =  _bookPublisher

    init {
        _barcodeData.value = null
    }

    fun loadBookInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            bookRepository?.getVideoData("${barcodeData.value}")?.let { response ->
                Log.d("response", "response=> $response")
                if(response.isSuccessful){
                    Log.d("진입","book 함수 진입=> ${response.code()}")
                    response.body()?.let {
                        Log.d("진입","book 함수 진입1 => $it")
                        _bookInfo.postValue(it)
                        _bookTitle.postValue(it.docs?.get(0)?.title)
                        _bookAuthor.postValue(it.docs?.get(0)?.author)
                        _bookUrl.postValue(it.docs?.get(0)?.url)
                        _bookPublisher.postValue(it.docs?.get(0)?.publisher)
                    }
                }
            }
        }

    }

    companion object {
        private val TAG = BookViewModel::class.java.simpleName
    }
}