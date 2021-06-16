package kr.kro.fatcats.bookscanner.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.databinding.FragmentMainBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kotlin.coroutines.CoroutineContext

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener , CoroutineScope{

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

        initLiveData()
        initClick()
    }

    private fun initClick(){
        binding.tvStart.setOnClickListener {
            timerStartBool =true
            launch { timerStart() }
        }
        binding.tvPause.setOnClickListener {
            timerStartBool =false
            //Todo 시간 저장
        }
    }

    private fun initLiveData() {
        mBookViewModel.mainBookInfo.observe(viewLifecycleOwner,{
            binding.setVariable(BR.title,it.title)
            binding.setVariable(BR.author,it.author)
            binding.setVariable(BR.publisher,it.publisher)
            binding.setVariable(BR.url,it.url)
            binding.setVariable(BR.totalTime,it.time)
        })
        mBookViewModel.timer.observe(viewLifecycleOwner,{
            binding.setVariable(BR.timer,it)
        })
    }

    private suspend fun timerStart() = withContext(Dispatchers.IO){
        var time = 0L
        if(mBookViewModel.timer.value != null){
           time = mBookViewModel.timer.value!!
        }
        while(timerStartBool){
            delay(1000L)
            time += 1000L
            withContext(Dispatchers.Main){
                mBookViewModel.timer.value = time
            }
        }
    }

    override fun onDestroy() {
        timerStartBool =false
        //Todo 시간 저장
        super.onDestroy()
    }

    override fun onRefresh() {

    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}