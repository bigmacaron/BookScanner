package kr.kro.fatcats.bookscanner.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.databinding.FragmentMainBinding
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.util.Constants

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var binding: FragmentMainBinding
    private lateinit var mBookViewModel: BookViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentMainBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
        }
        initView()
        return binding.root
    }
    private fun initView() {
        Log.d(TAG,"MainFragmentInit() =>")
        initLiveData()
    }

    private fun initLiveData() {
        mBookViewModel.bookTitle.observe(viewLifecycleOwner,{
            binding.setVariable(BR.title,it)
        })
        mBookViewModel.bookAuthor.observe(viewLifecycleOwner,{
            binding.setVariable(BR.author,it)
        })
        mBookViewModel.bookPublisher.observe(viewLifecycleOwner,{
            binding.setVariable(BR.publisher,it)
        })
        mBookViewModel.bookUrl.observe(viewLifecycleOwner,{
            binding.setVariable(BR.url,it)
        })
    }


    override fun onRefresh() {

    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}