package kr.kro.fatcats.bookscanner.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import kr.kro.fatcats.bookscanner.listeners.OnFragmentInteractionListener
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.databinding.ActivityMainBinding
import kr.kro.fatcats.bookscanner.fragment.ContentFragment
import kr.kro.fatcats.bookscanner.fragment.DrawerFragment
import kr.kro.fatcats.bookscanner.util.Constants
import kr.kro.fatcats.bookscanner.util.bindViewDrawerType
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private lateinit var binding : ActivityMainBinding
    private var fragment: Fragment? = null
    private var ordSorFrag: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindViewDrawerType(binding.drawerFrame, "start")
        initView(savedInstanceState)
        moveSplash()
    }

    private fun initView(savedInstanceState: Bundle?) {
        val fm: FragmentManager = supportFragmentManager
        if (savedInstanceState == null) {
            fragment = ContentFragment.newInstance()
            val t = fm.beginTransaction()
            t.replace(R.id.content_frame, fragment as ContentFragment)
            t.commit()

            ordSorFrag = DrawerFragment.newInstance()
            val r = fm.beginTransaction()
            r.replace(R.id.drawer_frame, ordSorFrag as DrawerFragment)
            r.commit()
        } else {
            fragment = fm.findFragmentById(R.id.content_frame)
            ordSorFrag = fm.findFragmentById(R.id.drawer_frame)
        }
    }

    private fun moveSplash() {
//        val intent = if (Preferences.first) {
//            Intent(this, IntroActivity::class.java)
//        } else {
//            Intent(this, SplashActivity::class.java)
//        }
//        startActivity(intent) // 화면 생성 후 로딩화면으로 이동
    }

    override fun openDrawer() {
        Log.d(TAG, "openDrawer()...")
        
        layout_drawer.openDrawer(Constants.DRAWER_TYPE)
    }

    override fun closeDrawer() {
        Log.d(TAG, "closeDrawer()...")
        layout_drawer.closeDrawers()
    }

    override fun onBackPressed() {
        finishAlert()
    }

    private fun finishAlert() {
        Log.v(TAG, "drawer2 => ${layout_drawer.isDrawerOpen(Constants.DRAWER_TYPE)}")
        if (layout_drawer.isDrawerOpen(Constants.DRAWER_TYPE)) {
            closeDrawer()
            return
        }
//        val content = (fragment as ContentFragment)
//        Log.v(TAG, "content => ${content.isCurrentHomeFragment()}")
//        if (content.isCurrentHomeFragment().not()) {
//            content.setCurrentHomeFragment()
//            return
//        }
        alert(message = "앱을 종료하시겠습니까?") {
            yesButton { finish() }
            noButton { it.dismiss() }
        }.show()
    }

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }
}