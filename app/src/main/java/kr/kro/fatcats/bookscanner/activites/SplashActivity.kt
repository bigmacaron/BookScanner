package kr.kro.fatcats.bookscanner.activites

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kr.kro.fatcats.bookscanner.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
        private const val REQUEST_CODE_UPDATE = 1111
        private const val delayTime = 1500L // 1.5초
    }

    private var mPackageName: String? = null
    private var mAppUpdateManager: AppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mPackageName = packageName
        MobileAds.initialize(this)
        playStoreVersionCheck()
//        next() // 릴리즈 배포시 주석
    }

    override fun onResume() {
        super.onResume()
        mAppUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = mAppUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                Log.d(TAG, "업데이트 있음")
                try {
                    mAppUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        REQUEST_CODE_UPDATE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else {
                Log.d(TAG, "업데이트 없음")
                next()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun next() {
        Handler().postDelayed({
            finish()
//            startActivity(Intent(this,MainActivity::class.java))
        }, delayTime)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            when {
                resultCode == Activity.RESULT_CANCELED -> { // 사용자가 업데이트를 거부하거나 취소
                    Log.d("AppUpdate", "Update Failed or Canceled!")
                }
                requestCode == Activity.RESULT_OK -> { // 사용자가 업데이트를 수락
                    Log.d("AppUpdate", "Update Complete!")
                }
                resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    Log.d("AppUpdate", "Update flow failed! Result code: $resultCode")
                }
            }
            next()
        }
    }

    private fun playStoreVersionCheck() {
        mAppUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = mAppUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                Log.d(TAG, "업데이트 있음")
                try {
                    mAppUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        REQUEST_CODE_UPDATE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else {
                Log.d(TAG, "업데이트 없음")
                next()
            }
        }

        val listener = object: InstallStateUpdatedListener {
            override fun onStateUpdate(state: InstallState) {
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    if (mAppUpdateManager != null) mAppUpdateManager?.completeUpdate()
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null) mAppUpdateManager?.unregisterListener(this)
                    else Log.d(TAG, "InstallStateUpdatedListener: state: ${state.installStatus()}")
                }
            }
        }
        mAppUpdateManager?.registerListener(listener)
    }

}