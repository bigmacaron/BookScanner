package kr.kro.fatcats.bookscanner

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import kr.kro.fatcats.bookscanner.util.Preferences

class MyApplication : Application(){
    companion object {
        private val TAG = MyApplication::class.java.simpleName
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
        Preferences.id = "8eztkpjWpxH0nyI0ljuF"
        Preferences.secret = "8x7z5sgVle"
//        KakaoSdk.init(this, getString(R.string.kakao_app_key)) // Kakao AppKey 초기화
        instance = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  //다크모드 안되도록
    }
}