package kr.kro.fatcats.bookscanner.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.databinding.FragmentContentBinding
import kr.kro.fatcats.bookscanner.listeners.OnFragmentInteractionListener
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kotlin.coroutines.CoroutineContext

class ContentFragment : Fragment() , BottomNavigationView.OnNavigationItemSelectedListener, CoroutineScope  {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentContentBinding
    private lateinit var actionbar: ActionBar
    private lateinit var mBookViewModel: BookViewModel
    private lateinit var mainFragment: MainFragment
    private lateinit var subFragment: SubFragment
    private lateinit var params : WindowManager.LayoutParams
    private var originBright : Float = 0f

    private var selectFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContentBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
        }
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
        initBright()
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        setupFragment()
        setActionbar()
        replaceFragment(mainFragment)
        initLiveData()
    }

    private fun initBright(){
        params = (activity as MainActivity).window.attributes
        originBright = params.screenBrightness
    }

    private fun brightSetOrigin(){
        params.screenBrightness = originBright
        (activity as MainActivity).window.attributes = params
    }

    private fun brightSetMin(){
        params.screenBrightness = 0f
        (activity as MainActivity).window.attributes = params
    }


    private fun initLiveData(){
        mBookViewModel.fragment.observe(viewLifecycleOwner,{
            Handler(Looper.getMainLooper()).postDelayed({
                selectTimer()
                (activity as MainActivity).closeDrawer()
            }, 500)
        })

        mBookViewModel.sensorXyzData.observe(viewLifecycleOwner,{
            launch {
                if(mBookViewModel.mainBookInfo.value != null){
                    startTimer(it)
                }
            }
        })

        mBookViewModel.sensorProximityData.observe(viewLifecycleOwner,{
            mBookViewModel.sensorXyzData.value?.get(2)?.let { xyz->
                if(xyz < -5 && it == 0){
                    brightSetMin()
                }else{
                    brightSetOrigin()
                }
            }
        })
    }

    private suspend fun startTimer(array : IntArray?) = withContext(Dispatchers.IO){
        delay(1000L)
        if (array?.get(2)!! < -5) {
            Log.e("startTimer" , "${array?.get(2)!!}")
            mBookViewModel.startTimer()
        }
    }

    private fun selectTimer(){
        binding.bottomNavigation.getMenu().getItem(0).setChecked(true)
        onNavigationItemSelected(binding.bottomNavigation.getMenu().getItem(0))
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
            R.id.action_timer -> {
                MainActivity.mBookViewModel.cameraStop.postValue(null)
                replaceFragment(mainFragment)
                true
            }
            R.id.action_scanner -> {
                MainActivity.mBookViewModel.cameraStop.postValue("play")
                replaceFragment(subFragment)
                true
            }
            else -> false
        }
    }

    companion object {
        val contentFragment = this
        private val TAG = ContentFragment::class.java.simpleName

        fun newInstance() = ContentFragment().apply {
            arguments = bundleOf()
        }
    }
}