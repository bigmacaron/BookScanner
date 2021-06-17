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

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener , CoroutineScope {

    private val db by lazy { DatabaseProvider.provideDB(requireContext().applicationContext).roomBookInfoDao() }
    private lateinit var binding: FragmentMainBinding
    private lateinit var mBookViewModel: BookViewModel
    private var timerStartBool = true
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
        binding.setVariable(BR.timerButton,timerStartBool)
        initLiveData()
        initClick()
    }

    private fun initClick(){
        binding.tvStart.setOnClickListener {
            if(mBookViewModel.mainBookInfo.value == null){
                mBookViewModel.showToast("책을 선택해주세요")
            }else{
                timerStartBool = timerStartBool.not()
                launch { timerStart() }
            }
        }
        binding.tvSave.setOnClickListener {
            if(mBookViewModel.timer.value != null){
                launch {
                    updateTime()
                    resetTimer()
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
        //타이머
        mBookViewModel.timer.observe(viewLifecycleOwner,{
            binding.setVariable(BR.timer,it)  //카운트 표기
            binding.setVariable(BR.timerButton,timerStartBool.not())  // 버튼 표기
        })

    }

    private suspend fun timerStart() = withContext(Dispatchers.IO){
        var time = 0L
        if(mBookViewModel.timer.value != null){
           time = mBookViewModel.timer.value!!
        }else{
            timerStartBool = true
        }
        while(timerStartBool){
            time += 1000L
            delay(1000L)
            mBookViewModel.timer.postValue(time)

        }
    }
    //저장하기
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
        }
    }

    private suspend fun resetTimer() = withContext(Dispatchers.IO){
        mBookViewModel.timer.postValue(null)
    }

    override fun onDestroy() {
        timerStartBool =false
        if(mBookViewModel.timer.value != null){
            launch { updateTime() }
        }
        super.onDestroy()
    }

    override fun onRefresh() {

    }


    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}