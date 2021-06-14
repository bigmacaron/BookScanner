package kr.kro.fatcats.bookscanner.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.zxing.ResultPoint
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.DatabaseProvider
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.RoomBookInfo
import kr.kro.fatcats.bookscanner.util.BookInfoDialog
import org.jetbrains.anko.support.v4.toast

class SubFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener  {

    private lateinit var binding: FragmentSubBinding
    private lateinit var mBookViewModel: BookViewModel
    private val db by lazy {DatabaseProvider.provideDB(requireContext().applicationContext).roomBookInfoDao() }
    private val dialog by lazy {  BookInfoDialog(binding.root.context)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentSubBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
            lifecycleOwner = requireActivity()
            viewModel = mBookViewModel
        }
        initView()
        return binding.root
    }
    override fun onRefresh() {

    }
    private fun initView(){
        getPermission()
        initScanner()
        buttonSet()
        initLiveData()
    }

    private fun initLiveData() {

        mBookViewModel.barcodeData.observe(viewLifecycleOwner, {
            binding.barcodeData = it
            CoroutineScope(Dispatchers.Main).launch {
                if(it == null){
                    binding.barcodeView.resume()
                }else{
                    binding.barcodeView.pause()
                    mBookViewModel.loadBookInfo()
                    dialogShow()
                }
            }
        })
    }

    private suspend fun dialogShow() =  withContext(Dispatchers.IO){
        val regex = "^[0-9]+$".toRegex()
        Log.d("booooool","${mBookViewModel.barcodeData.value?.substring(0)?.matches(regex) == true}")
        if(mBookViewModel.barcodeData.value?.substring(0)?.matches(regex) == true){
            val insertData = with(mBookViewModel){
                RoomBookInfo(
                    barcodeData.value?.toLong(),
                    bookTitle.value,
                    bookAuthor.value,
                    bookPublisher.value,
                    bookUrl.value,
                    "0"
                )
            }
            mBookViewModel.barcodeData.value?.let {barcode ->
                val barcodeData = db.getDataForIsbn(barcode.toLong())
                if(barcodeData == null){
                    Log.d("insertData.title","${insertData.title}")
                    insertDbForBarcode(insertData)
                    Log.d("바코드","${barcodeData?.title}")
                    withContext(Dispatchers.Main){dialog.myDialog(
                        insertData.title,
                        insertData.url,
                        insertData.author,
                        insertData.publisher
                    )}
                }else{
                    withContext(Dispatchers.Main){dialog.myDialog(
                        insertData.title,
                        insertData.url,
                        insertData.author,
                        insertData.publisher
                    )}
                }

            }
        }else{
            toast("잘못된 바코드 입니다.")
        }
    }

    override fun onStop() {
//        mBookViewModel.barcodeData.postValue(null)
//        mBookViewModel.bookInfo.postValue(null)
        super.onStop()
    }
    override fun onResume() {
        binding.barcodeView.resume()
        super.onResume()
    }
    override fun onPause() {
        binding.barcodeView.pause()
        super.onPause()
    }

    private fun getPermission(){
        TedPermission.with(binding.root.context).setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {}
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {}
        })
            .setRationaleMessage("권한을 허용해주세요.")
            .setDeniedMessage("권한이 거부되었습니다. 설정 > 권한에서 허용해주세요.")
            .setPermissions(
                Manifest.permission.CAMERA // 저장 권한
            )
            .check()
    }

    private fun initScanner() {
        binding.barcodeView.setStatusText("바코드를 스캔해 주세요")
        binding.barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                mBookViewModel.barcodeData.postValue(result.toString())
            }
            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
    }


    private fun buttonSet(){
        binding.btnReScan.setOnClickListener {
            mBookViewModel.barcodeData.postValue(null)
            mBookViewModel.bookInfo.postValue(null)
        }
        binding.btnConfirm.setOnClickListener {

        }
    }
    private suspend fun insertDbForBarcode(insertData : RoomBookInfo) = withContext(Dispatchers.IO){
        db.insert(insertData)
    }

    private suspend fun alertDialog() = withContext(Dispatchers.Main){

    }


    companion object {
        private val TAG = SubFragment::class.java.simpleName
    }

}