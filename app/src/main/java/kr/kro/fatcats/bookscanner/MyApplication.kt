package kr.kro.fatcats.bookscanner

import android.app.Application
import kr.kro.fatcats.bookscanner.util.Preferences

class MyApplication : Application(){
    companion object {
        private val TAG = MyApplication::class.java.simpleName
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
        Preferences.certKey = "3a9a9ce9afd7ffc5aae440e6e58f9f38deb2788d689eb7d69edddbbaaed6817f"
//        KakaoSdk.init(this, getString(R.string.kakao_app_key)) // Kakao AppKey 초기화
        instance = this
    }
}