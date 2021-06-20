package kr.kro.fatcats.bookscanner.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.DatabaseProvider
import kr.kro.fatcats.bookscanner.databinding.FragmentMainBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainFragment : Fragment(), CoroutineScope {

    private val db by lazy { DatabaseProvider.provideDB(requireContext().applicationContext).roomBookInfoDao() }
    private lateinit var binding: FragmentMainBinding
    private lateinit var mBookViewModel: BookViewModel
    private var timerBool = false

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentMainBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
        }
        initView()
        return binding.root
    }
    private fun initView() {
        binding.setVariable(BR.timerButton,mBookViewModel.startTimer.value)
        initLiveData()
        initClick()
    }

    private suspend fun startTimer() = withContext(Dispatchers.IO) {
            var time = 0L
            if(mBookViewModel.timer.value != null){
                time = mBookViewModel.timer.value!!
            }
            while(timerBool){
                Log.e("bool","${mBookViewModel.startTimer.value}")
                mBookViewModel.timer.postValue(time)
                time += 1000L
                delay(1000L)
            }

    }

    private fun timerStart(){
        if(mBookViewModel.mainBookInfo.value == null){
            mBookViewModel.showToast("책을 선택해주세요")
        }else{
            timerBool =true
            CoroutineScope(Dispatchers.IO).launch {
                mBookViewModel.startTimer()
                startTimer()
            }

        }
    }
    private fun timerPause(){
        if(mBookViewModel.mainBookInfo.value == null){
            mBookViewModel.showToast("책을 선택해주세요")
        }else{
            timerBool = false
            mBookViewModel.startTimer.postValue(false)
        }
    }

    private fun initClick(){
        binding.tvTimerStart.setOnClickListener {
            timerStart()
        }
        binding.tvTimerPause.setOnClickListener {
            timerPause()
        }
        binding.tvSave.setOnClickListener {
            if(mBookViewModel.timer.value != null){
                launch {
                    updateTime()
                }
            }else{
                mBookViewModel.showToast("타이머가 작동되지 않았습니다.")
            }
        }
    }


    private fun initLiveData() {
        //메인에 책정보 표기
        mBookViewModel.mainBookInfo.observe(viewLifecycleOwner,{
            binding.setVariable(BR.title,it.title)
            binding.setVariable(BR.author,it.author)
            binding.setVariable(BR.publisher,it.publisher)
            binding.setVariable(BR.url,it.url)
            binding.setVariable(BR.totalTime,it.time)
        })
        mBookViewModel.startTimer.observe(viewLifecycleOwner,{
            if(mBookViewModel.mainBookInfo.value != null){
                binding.setVariable(BR.timerButton,it)
            }
            if(it == true){
                timerStart()
            }
        })
        mBookViewModel.timer.observe(viewLifecycleOwner,{
            Log.e("timer","it => $it")
            binding.setVariable(BR.timer,it)
        })
    }

    //타이머 시간 저장 버튼 -> 룸에 저장
    private suspend fun updateTime() = withContext(Dispatchers.IO){
        mBookViewModel.mainBookInfo.value?.isbn?.let { isbn->
            val now =  SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(Calendar.getInstance().time)
            val timerTime = mBookViewModel.timer.value
            val dbTime =  db.getDataForIsbn(isbn)?.time
            val totalTime =dbTime?.let { timerTime?.plus(it) }
            mBookViewModel.totalTime.postValue(totalTime)
            Log.d("updateTime","timerTime :$timerTime ,dbTime: $dbTime , totalTime : $totalTime")
            db.updateTime(totalTime,isbn)
            db.updateLastDate(now,isbn)
            MainActivity.mBookViewModel.mainBookInfo.postValue( db.getDataForIsbn(isbn))
            resetTimer()
        }
    }
    //타이머 시간 저장 버튼 -> 타이머 리셋
    private suspend fun resetTimer() = withContext(Dispatchers.IO){
        mBookViewModel.timer.postValue(null)
    }

    override fun onDestroy() {

        if(mBookViewModel.timer.value != null){
            launch { updateTime() }
        }
        super.onDestroy()
    }


    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}