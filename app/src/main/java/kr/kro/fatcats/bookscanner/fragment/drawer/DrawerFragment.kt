package kr.kro.fatcats.bookscanner.fragment.drawer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.DatabaseProvider
import kr.kro.fatcats.bookscanner.api.RoomBookInfoDao
import kr.kro.fatcats.bookscanner.databinding.FragmentDrawerBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.ListInfo
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import kotlin.coroutines.CoroutineContext

class DrawerFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener , CoroutineScope{

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: FragmentDrawerBinding
    private lateinit var mBookViewModel: BookViewModel
    private lateinit var db : RoomBookInfoDao
    private lateinit var mAdapter : DrawerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        db = DatabaseProvider.provideDB(requireContext().applicationContext).roomBookInfoDao()
        binding= FragmentDrawerBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
            lifecycleOwner = requireActivity()
        }
        return binding.root
    }

    override fun onRefresh() {
        mAdapter.clear()
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
    }

    private fun initBtn() {
        binding.btnDeleteAll.setOnClickListener {
            alert("정말 초기화 하시겠습니까? \n 지운 데이터는 복구가 불가능 합니다" ){
                yesButton { launch { deleteAll() } }
                noButton { it.dismiss() }
            }.show()
        }
    }

    private fun setAdapter() {
        binding.rvBookList.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            mAdapter = DrawerAdapter(db)
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
    }

    private suspend fun initDb() = withContext(Dispatchers.IO){
        val dbData = db.getAll() as ArrayList<ListInfo>
        Log.d("DBData","$dbData")
        mBookViewModel.bookList.postValue(dbData)
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

        fun newInstance() = DrawerFragment().apply {
            arguments = androidx.core.os.bundleOf()
        }
    }
}