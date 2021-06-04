package kr.kro.fatcats.bookscanner.util

import android.Manifest
import android.content.Context
import android.util.Log
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.kro.fatcats.bookscanner.R

class Commons {
    companion object {
        private val TAG = Commons::class.java.simpleName

        fun checkPermission(context: Context, permissionListener: PermissionListener) {
            Log.e(TAG, "checkPermission")
            TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(R.string.content_permission_request) // 권한 요청 이유
                .setRationaleConfirmText(R.string.content_button_confirm) // 확인 버튼
                .setDeniedMessage(R.string.content_permission_request_settings) // 거부했을 때 보여지는 메시지
                .setDeniedCloseButtonText(R.string.content_alert_negative_of_mobile_data)
                .setGotoSettingButton(true)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE)
                .check()
        }
        
    }
}