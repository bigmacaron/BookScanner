package kr.kro.fatcats.bookscanner.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.zxing.ResultPoint
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding
import org.jetbrains.anko.support.v4.toast

class SubFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener  {

    private lateinit var binding: FragmentSubBinding
    private lateinit var capture : CaptureManager
    private var readBarcode : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub, container, false)
        initView()
        return binding.root
    }
    override fun onRefresh() {

    }
    private fun initView(){
        getPermission()
        initScanner()
        buttonSet()
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
                if(readBarcode == null){
                    readBarcode = result.toString()
                    binding.setVariable(BR.barcodeData,readBarcode)
                    binding.barcodeView.pause()
                }
            }
            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
    }

    private fun buttonSet(){
        binding.btnReScan.setOnClickListener {
            readBarcode = null
            binding.barcodeView.resume()
            binding.setVariable(BR.barcodeData,readBarcode)
        }
        binding.btnConfirm.setOnClickListener {
        }
    }

    companion object {
        private val TAG = SubFragment::class.java.simpleName
    }

}