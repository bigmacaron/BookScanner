package kr.kro.fatcats.bookscanner.model

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.util.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

class BookViewModel(private val bookRepository: BookRepository?): ViewModel() ,CoroutineScope {


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _barcodeData = SingleLiveEvent<String?>()
    private val _bookInfo = SingleLiveEvent<BookInfo?>()
    private val _bookTitle = SingleLiveEvent<String?>()
    private val _bookAuthor = SingleLiveEvent<String?>()
    private val _bookUrl = SingleLiveEvent<String?>()
    private val _bookPublisher = SingleLiveEvent<String?>()
    private val _bookList = SingleLiveEvent<ArrayList<ListInfo>>()
    private val _bookListSize = SingleLiveEvent<Int?>()
    private val _mainBookInfo = SingleLiveEvent<ListInfo>()
    private val _timer =SingleLiveEvent<Long?>()

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
    val bookList : SingleLiveEvent<ArrayList<ListInfo>>
        get() = _bookList
    //size 측정 -> Drawer 컨트롤
    val bookListSize : SingleLiveEvent<Int?>
        get() =  _bookListSize
    //메인에 그려지는것
    val mainBookInfo : SingleLiveEvent<ListInfo>
        get() = _mainBookInfo
    val timer : SingleLiveEvent<Long?>
        get() = _timer

    init {
        _barcodeData.value = null
    }

    suspend fun loadBookInfo() = withContext(Dispatchers.IO){
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


//        //점검용
//        val it = BookInfo(arrayListOf(Doc(
//            "홍길동",null,null,null,null,
//            null,null,null,null,null,
//            null,barcodeData.value,null,null,null,
//            null,null,null,null,null,
//            "의적단",null,null,null,null,
//            null,null,null,null,null,
//            null,null,"홍길동전","http://image.kyobobook.co.kr/images/book/xlarge/663/x9788935668663.jpg",null,
//            null
//        )),null,null)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            _bookInfo.postValue(it)
//            _bookTitle.postValue(it.docs?.get(0)?.title)
//            _bookAuthor.postValue(it.docs?.get(0)?.author)
//            _bookUrl.postValue(it.docs?.get(0)?.url)
//            _bookPublisher.postValue(it.docs?.get(0)?.publisher)
//        }
    }

    companion object {
        private val TAG = BookViewModel::class.java.simpleName
    }


}