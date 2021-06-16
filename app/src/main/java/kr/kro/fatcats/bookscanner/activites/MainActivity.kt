package kr.kro.fatcats.bookscanner.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import kr.kro.fatcats.bookscanner.listeners.OnFragmentInteractionListener
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.databinding.ActivityMainBinding
import kr.kro.fatcats.bookscanner.fragment.ContentFragment
import kr.kro.fatcats.bookscanner.fragment.drawer.DrawerFragment
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.util.Constants
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private lateinit var binding : ActivityMainBinding
    private var fragment: Fragment? = null
    private var ordSorFrag: Fragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            this@MainActivity.let {
                mBookViewModel = ViewModelProvider(this@MainActivity,BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
                viewModel = mBookViewModel
                lifecycleOwner = this@MainActivity
            }
        }

        setContentView(binding.root)
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

        binding.layoutDrawer.openDrawer(Constants.DRAWER_TYPE)
    }

    override fun closeDrawer() {
        Log.d(TAG, "closeDrawer()...")
        binding.layoutDrawer.closeDrawers()
    }

    override fun onBackPressed() {
        finishAlert()
    }

    private fun finishAlert() {
        Log.v(TAG, "drawer2 => ${binding.layoutDrawer.isDrawerOpen(Constants.DRAWER_TYPE)}")
        if (binding.layoutDrawer.isDrawerOpen(Constants.DRAWER_TYPE)) {
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
        lateinit var mBookViewModel: BookViewModel
    }
}