package kr.kro.fatcats.bookscanner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.databinding.FragmentDrawerBinding

class DrawerFragment : Fragment(){


    private lateinit var binding: FragmentDrawerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drawer, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        clickListeners()
    }

    private fun clickListeners(){
        binding.ivCloseDrawer.setOnClickListener {
            (activity as MainActivity).closeDrawer()
        }
    }

    companion object {
        private val TAG = DrawerFragment::class.java.simpleName

        fun newInstance() = DrawerFragment().apply {
            arguments = androidx.core.os.bundleOf()
        }
    }
}