package kr.kro.fatcats.bookscanner.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.databinding.FragmentContentBinding
import kr.kro.fatcats.bookscanner.listeners.OnFragmentInteractionListener

class ContentFragment : Fragment() , BottomNavigationView.OnNavigationItemSelectedListener{

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentContentBinding
    private lateinit var mainFragment: MainFragment
    private lateinit var subFragment: SubFragment
    private lateinit var actionbar: ActionBar

    private var selectFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v(TAG, "onAttach()...")
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.v(TAG, "onDetach()...")
        mListener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        setupFragment()
        setActionbar()
        replaceFragment(mainFragment)
    }

    private fun setupFragment() {
        mainFragment = MainFragment()
        subFragment = SubFragment()
        val fm: FragmentManager = (context as MainActivity).supportFragmentManager
        fm.beginTransaction()
            .add(R.id.main_content_view, mainFragment)
            .hide(mainFragment)
            .add(R.id.main_content_view, subFragment)
            .hide(subFragment)
            .commit()
    }
    private fun setActionbar() {
        setHasOptionsMenu(true)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            actionbar = (activity as AppCompatActivity).supportActionBar!!
            actionbar.setDisplayShowTitleEnabled(false)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        Log.v(TAG, "replaceFragment() => ${fragment.tag}")
        val fm: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
        selectFragment?.let { fm.hide(it) }
        fm.show(fragment)
        fm.addToBackStack(null)
        fm.commit()
        selectFragment = fragment
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "onCreateOptionsMenu() =>")
        inflater.inflate(R.menu.content_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected() => ${item.itemId}")
        return when (item.itemId) {
            R.id.action_drawer -> {
                Log.v(TAG, "drawer selected...")
                mListener?.openDrawer()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.v(TAG, "onNavigationItemSelected() => ${item.itemId}")
        return when (item.itemId) {
            R.id.action_home -> {
                replaceFragment(mainFragment)
                true
            }
            R.id.action_progress -> {
                replaceFragment(subFragment)
                true
            }
            else -> false
        }
    }

    companion object {

        private val TAG = ContentFragment::class.java.simpleName

        fun newInstance() = ContentFragment().apply {
            arguments = bundleOf()
        }
    }
}