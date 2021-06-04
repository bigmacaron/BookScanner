package kr.kro.fatcats.bookscanner.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.databinding.FragmentMainBinding

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        initView()
        return binding.root
    }
    private fun initView() {
        Log.d(TAG,"MainFragmentInit() =>")
    }


    override fun onRefresh() {

    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}