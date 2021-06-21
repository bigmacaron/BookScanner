package kr.kro.fatcats.bookscanner.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.util.Event
import kr.kro.fatcats.bookscanner.util.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

class BookViewModel(private val bookRepository: BookRepository?): ViewModel() ,CoroutineScope {


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _showToast = MutableLiveData<Event<String>>()
    val showToast: LiveData<Event<String>> = _showToast

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
    private val _totalTime =SingleLiveEvent<Long?>()
    private val _cameraStop = SingleLiveEvent<String?>()
    private val _fragment = SingleLiveEvent<String?>()
    private val _sensorData = SingleLiveEvent<IntArray?>()
    private val _startTimer = SingleLiveEvent<Boolean>()
    private val _sensorLightData = SingleLiveEvent<Int?>()

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
    val totalTime : SingleLiveEvent<Long?>
        get() = _totalTime
    val cameraStop : SingleLiveEvent<String?>
        get() = _cameraStop
    val fragment : SingleLiveEvent<String?>
        get() = _fragment
    val sensorXyzData : SingleLiveEvent<IntArray?>
        get() = _sensorData
    val sensorProximityData : SingleLiveEvent<Int?>
        get() = _sensorLightData
    val startTimer : SingleLiveEvent<Boolean>
        get() = _startTimer


    init {
        _barcodeData.value = null
        _startTimer.value = false
    }

    suspend fun startTimer()= withContext(Dispatchers.IO){
        if(_startTimer.value == false){
            _startTimer.postValue(true)
        }
    }


    suspend fun loadBookInfo() = withContext(Dispatchers.IO){
        bookRepository?.getVideoData("${barcodeData.value}")?.let { response ->
            Log.d("response", "response=> $response")
            if(response.isSuccessful){
                Log.d("loadBookInfo","book 함수 진입 => ${response.toString()}")
                response.body()?.let {
                    Log.d("loadBookInfo","book 함수 진입 => $it")
                    _bookInfo.postValue(it)
                    if(it.items?.size!! > 0){
                         _bookTitle.postValue(it.items[0].title)
                         _bookAuthor.postValue(it.items[0].author)
                         _bookUrl.postValue(it.items[0].url)
                         _bookPublisher.postValue(it.items[0].publisher)
                    }else{
                        showToast("죄송합니다.정보가 없습니다.")
                    }
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

    fun updateDrawer(){

    }

    fun showToast(text : String){
        _showToast.postValue(Event(text))
    }

    companion object {
        private val TAG = BookViewModel::class.java.simpleName
    }


}