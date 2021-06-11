package kr.kro.fatcats.bookscanner.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.zxing.ResultPoint
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.RoomBookInfoDatabase
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.RoomBookInfo
import kr.kro.fatcats.bookscanner.util.SingleLiveEvent
import org.jetbrains.anko.support.v4.toast

class SubFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener  {

    private lateinit var binding: FragmentSubBinding
    private lateinit var mBookViewModel: BookViewModel
    private var db : RoomBookInfoDatabase? = null

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
        initRoom()
    }

    private fun initRoom() {
        db = RoomBookInfoDatabase.getInstance(binding.root.context.applicationContext)
    }

    private fun initLiveData() {
        mBookViewModel.barcodeData.observe(viewLifecycleOwner, {
            binding.barcodeData = it
            if(it == null){
                binding.barcodeView.resume()
            }else{
                binding.barcodeView.pause()
                mBookViewModel.loadBookInfo()
            }
        })
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
            CoroutineScope(Dispatchers.IO).launch { // 다른애 한테 일 시키기
                db!!.roomBookInfoDao().insert(insertData)
                val listA : List<RoomBookInfo> = db!!.roomBookInfoDao().getAll()
            }
        }
    }

    companion object {
        private val TAG = SubFragment::class.java.simpleName
    }

}