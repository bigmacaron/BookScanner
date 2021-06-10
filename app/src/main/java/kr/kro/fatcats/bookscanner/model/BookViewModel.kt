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

    val barcodeData : SingleLiveEvent<String?>
        get() = _barcodeData
    val bookInfo : SingleLiveEvent<BookInfo?>
        get() = _bookInfo

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
                    }
                }
            }
        }

    }

    companion object {
        private val TAG = BookViewModel::class.java.simpleName
    }
}