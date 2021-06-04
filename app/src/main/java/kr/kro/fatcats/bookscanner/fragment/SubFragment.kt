package kr.kro.fatcats.bookscanner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding

class SubFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentSubBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub, container, false)
        return binding.root
    }
    override fun onRefresh() {

    }

    companion object {
        private val TAG = SubFragment::class.java.simpleName
    }

}