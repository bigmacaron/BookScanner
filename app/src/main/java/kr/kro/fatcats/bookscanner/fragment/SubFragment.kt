package kr.kro.fatcats.bookscanner.fragment

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.zxing.ResultPoint
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.*
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.DatabaseProvider
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding
import kr.kro.fatcats.bookscanner.listeners.DialogClickListener
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.RoomBookInfo
import kr.kro.fatcats.bookscanner.util.BookInfoDialog
import kr.kro.fatcats.bookscanner.util.Constants
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class SubFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener ,CoroutineScope,DialogClickListener {

    private lateinit var binding: FragmentSubBinding
    private lateinit var mBookViewModel: BookViewModel

    private val db by lazy {DatabaseProvider.provideDB(requireContext().applicationContext).roomBookInfoDao() }
    private val dialog by lazy {  BookInfoDialog(binding.root.context)}
    private val job = Job()
    private val listener : DialogClickListener = this

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentSubBinding.inflate(inflater).apply {
            mBookViewModel = ViewModelProvider(requireActivity(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
            lifecycleOwner = requireActivity()
            viewModel = mBookViewModel
        }


        initView()
        setAd()
        return binding.root
    }
    private fun setAd(){
        binding.adView.loadAd(AdRequest.Builder().build())
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
            launch {
                if(it == null){
                    binding.barcodeView.resume()
                }else{
                    binding.barcodeView.pause()
                    mBookViewModel.loadBookInfo()
                    dialogShow()
                }
            }
        })
        cameraStop()
    }

    private suspend fun dialogShow() =  withContext(Dispatchers.IO){
        val regex = "^[0-9]+$".toRegex()
        Log.d("booooool","${mBookViewModel.barcodeData.value?.substring(0)?.matches(regex) == true}")
        if(mBookViewModel.barcodeData.value?.substring(0)?.matches(regex) == true){
            mBookViewModel.barcodeData.value?.let {barcode ->
                val barcodeData = db.getDataForIsbn(barcode.toLong())
                Log.d("barcodeData" ,"barcodeData=> ${barcodeData?.isbn}")
                if(barcodeData?.isbn == null){
                    Log.d("바코드","${barcodeData?.ct_date} : ${barcodeData?.mod_date}")
                    withContext(Dispatchers.Main){
                        dialog.myDialog(
                            mBookViewModel.bookTitle.value,
                            mBookViewModel.bookUrl.value,
                            mBookViewModel.bookAuthor.value,
                            mBookViewModel.bookPublisher.value,
                            null
                            ,listener
                    )}
                }else{
                    withContext(Dispatchers.Main){
                        dialog.myDialog(
                            mBookViewModel.bookTitle.value,
                            mBookViewModel.bookUrl.value,
                            mBookViewModel.bookAuthor.value,
                            mBookViewModel.bookPublisher.value,
                            Constants.DialogType.IS_ROOM,listener
                        )}
                }

            }
        }else{
            withContext(Dispatchers.Main){toast("잘못된 바코드 입니다.")}
        }
    }

    //Dialog interface function
    override fun onClick(type: String) {
        val now =  SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(Calendar.getInstance().time)
        val insertData = with(mBookViewModel){
            RoomBookInfo(
                barcodeData.value?.toLong(),
                bookTitle.value,
                bookAuthor.value,
                bookPublisher.value,
                bookUrl.value,
                now,
                now,
                "0"
            )
        }
        launch {
            when(type){
                Constants.DialogType.REGISTER ->{
                    insertDbForBarcode(insertData)
                    Handler(Looper.getMainLooper()).postDelayed({
                        (activity as MainActivity).openDrawer()
                    }, 800)
                }
                Constants.DialogType.TIMER ->{
                    mBookViewModel.mainBookInfo.postValue(mBookViewModel.barcodeData.value?.toLong()?.let {
                        getRoomBookInfo(it)
                    })
                    mBookViewModel.fragment.postValue(type)
                }
                else -> toast("죄송합니다. 관리자에게 문의해 주세요")
            }
        }
    }

    //카메라가 계속 백그라운드에서 작동되어서 임시로 만든함수
    private fun cameraStop(){
        mBookViewModel.cameraStop.observe(viewLifecycleOwner,{
            if(it==null){
                binding.barcodeView.pause()
            }else{
                binding.barcodeView.resume()
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
                Manifest.permission.CAMERA
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

    // 다시스캔 (검색용 주석)
    private fun buttonSet(){
        binding.btnReScan.setOnClickListener {
            mBookViewModel.barcodeData.postValue(null)
            mBookViewModel.bookInfo.postValue(null)
        }
    }

    private suspend fun insertDbForBarcode(insertData : RoomBookInfo) = withContext(Dispatchers.IO){
        db.insert(insertData)
        mBookViewModel.bookListSize.postValue(db.getAll().size)
    }
    private suspend fun getRoomBookInfo(isbn : Long) = withContext(Dispatchers.IO){
        db.getDataForIsbn(isbn)
    }

    companion object {
        private val TAG = SubFragment::class.java.simpleName
    }



}