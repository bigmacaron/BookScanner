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
    private var timeArray = longArrayOf(0L,0L,0L,0L,0L,0L)

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
            setTimeNumber(it)

        })
    }

    private fun setTimeNumber(times : Long?){
        val hour    = ((times?.div(1000))?.div(60))?.div(60)
        val minutes = ((times?.div(1000))?.div(60))?.rem(60)
        val seconds = ((times?.div(1000))?.rem(60))
        var timeLeftFormatted = String.format(Locale.getDefault(),  "%02d:%02d:%02d",hour, minutes, seconds)
        if(seconds == null){
            timeLeftFormatted = "00:00:00"
        }
        if(timeArray[0] != timeLeftFormatted.substring(0,1).toLong()){
            timeArray[0] = timeLeftFormatted.substring(0,1).toLong()
            binding.setVariable(BR.timer1,timeArray[0])
        }
        if(timeArray[1] != timeLeftFormatted.substring(1,2).toLong()){
            timeArray[1] = timeLeftFormatted.substring(1,2).toLong()
            binding.setVariable(BR.timer2,timeArray[1])
        }
        if(timeArray[2] != timeLeftFormatted.substring(3,4).toLong()){
            timeArray[2] = timeLeftFormatted.substring(3,4).toLong()
            binding.setVariable(BR.timer3,timeArray[2])
        }
        if(timeArray[3] != timeLeftFormatted.substring(4,5).toLong()){
            timeArray[3] = timeLeftFormatted.substring(4,5).toLong()
            binding.setVariable(BR.timer4,timeArray[3])
        }
        if(timeArray[4] != timeLeftFormatted.substring(6,7).toLong()){
            timeArray[4] = timeLeftFormatted.substring(6,7).toLong()
            binding.setVariable(BR.timer5,timeArray[4])
        }
        if(timeArray[5] != timeLeftFormatted.substring(7,8).toLong()){
            timeArray[5] = timeLeftFormatted.substring(7,8).toLong()
            binding.setVariable(BR.timer6,timeArray[5])
        }






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