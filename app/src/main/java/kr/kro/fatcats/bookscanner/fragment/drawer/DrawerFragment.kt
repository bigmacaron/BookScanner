package kr.kro.fatcats.bookscanner.fragment.drawer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.DatabaseProvider
import kr.kro.fatcats.bookscanner.api.RoomBookInfoDao
import kr.kro.fatcats.bookscanner.databinding.FragmentDrawerBinding
import kr.kro.fatcats.bookscanner.fragment.ContentFragment
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.ListInfo
import kr.kro.fatcats.bookscanner.util.SwipeHelperCallback
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import kotlin.coroutines.CoroutineContext

class DrawerFragment : Fragment() , CoroutineScope{

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: FragmentDrawerBinding
    private lateinit var mBookViewModel: BookViewModel
    private lateinit var db : RoomBookInfoDao


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        db = DatabaseProvider.provideDB(requireContext().applicationContext).roomBookInfoDao()
        binding= FragmentDrawerBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
            lifecycleOwner = requireActivity()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }


    private fun initView() {
        setAdapter()
        initObserve()
        clickListeners()
        launch {
            initDb()
        }
        initBtn()
        initSwipeHelper()
    }

    private fun initSwipeHelper(){
        val swipeHelperCallback = SwipeHelperCallback()
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvBookList)
    }

    private fun initBtn() {
        binding.btnDeleteAll.setOnClickListener {
            alert("정말 초기화 하시겠습니까? \n지운 데이터는 복구가 불가능 합니다" ){
                yesButton { launch { deleteAll() } }
                noButton { it.dismiss() }
            }.show()
        }
    }

    private fun setAdapter() {
        binding.rvBookList.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            mAdapter = DrawerAdapter(db,binding.root.context)
            adapter = mAdapter
        }
    }

    private fun initObserve(){
        mBookViewModel.bookList.observe(viewLifecycleOwner, {
            mAdapter.clear()
            mAdapter.addItem(it)
        })
        mBookViewModel.bookListSize.observe(viewLifecycleOwner, {
            launch {
                returnBookListSize()
            }
        })
        mBookViewModel.totalTime.observe(viewLifecycleOwner,{
            launch{
                refreshAdapter()
            }
        })
    }

    private suspend fun initDb() = withContext(Dispatchers.IO){
        val dbData = db.getAll() as ArrayList<ListInfo>
        Log.d("DBData","$dbData")
        mBookViewModel.bookList.postValue(dbData)
    }
    private suspend fun refreshAdapter() = withContext(Dispatchers.IO){
        mBookViewModel.bookListSize.postValue(db.getAll().size)
    }

    private suspend fun deleteAll() = withContext(Dispatchers.IO){
        db.deleteAll()
        mBookViewModel.bookListSize.postValue(db.getAll().size)
    }
    private suspend fun returnBookListSize() = withContext(Dispatchers.IO){
        mBookViewModel.bookList.postValue(db.getAll() as ArrayList<ListInfo>)
    }

    private fun clickListeners(){
        binding.ivCloseDrawer.setOnClickListener {
            (activity as MainActivity).closeDrawer()
        }
    }

    companion object {

        private val TAG = DrawerFragment::class.java.simpleName
        lateinit var mAdapter : DrawerAdapter
        fun newInstance() = DrawerFragment().apply {
            arguments = androidx.core.os.bundleOf()
        }
    }
}