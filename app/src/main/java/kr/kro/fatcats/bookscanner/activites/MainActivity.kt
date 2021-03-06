package kr.kro.fatcats.bookscanner.activites

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
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
import org.jetbrains.anko.*
import java.util.*

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener, SensorEventListener {

    private lateinit var sensorXyzManager: SensorManager
    private lateinit var sensorLightManager : SensorManager
//    private lateinit var sensorProximityManager : SensorManager
//    private lateinit var powerManager : PowerManager
//    private lateinit var mWakeLock: PowerManager.WakeLock
    private lateinit var binding : ActivityMainBinding
    private var fragment: Fragment? = null
    private var ordSorFrag: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.sensorXyzManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        this.sensorProximityManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.sensorLightManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        super.onCreate(savedInstanceState)
        moveSplash()
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            this@MainActivity.let {
                mBookViewModel = ViewModelProvider(this@MainActivity,BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
                viewModel = mBookViewModel
                lifecycleOwner = this@MainActivity
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        powerManager = (applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager?)!!
//        mWakeLock = powerManager.newWakeLock(PROXIMITY_SCREEN_OFF_WAKE_LOCK,TAG)
//        mWakeLock.acquire()
        setContentView(binding.root)
        initView(savedInstanceState)
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

        initLiveData()

    }

    private fun moveSplash() {

        startActivity(Intent(this, SplashActivity::class.java)) // ?????? ?????? ??? ?????????????????? ??????
    }

    private fun initLiveData(){
        mBookViewModel.showToast.observe(this, {
            it.getContentIfNotHandled()?.let { msg ->
                showToast(msg)
            }
        })
    }

    private fun showToast(msg: String) {
        toast(msg)
    }

    override fun openDrawer() {
        if(mBookViewModel.timer.value == null){
            binding.layoutDrawer.openDrawer(Constants.DRAWER_TYPE)
        }else{
            showToast("????????? ???????????? ???????????? ????????? ??????????????????")
        }
    }
    override fun closeDrawer() {
        if(mBookViewModel.timer.value == null){
            binding.layoutDrawer.closeDrawers()
        }else{
            showToast("????????? ???????????? ???????????? ????????? ??????????????????")
        }

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

        alert(message = "?????? ?????????????????????????") {
            yesButton { finish() }
            noButton { it.dismiss() }
        }.show()
    }

    companion object{
        private val TAG = MainActivity::class.java.simpleName
        lateinit var mBookViewModel: BookViewModel
    }

    override fun onResume() {
        super.onResume()
//        sensorProximityManager.registerListener(this,sensorProximityManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_NORMAL)
        sensorXyzManager.registerListener(this,sensorXyzManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
        sensorLightManager.registerListener(this,sensorLightManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_NORMAL)


    }

    override fun onPause() {
        super.onPause()
//        sensorProximityManager.unregisterListener(this)
        sensorXyzManager.unregisterListener(this)
        sensorLightManager.unregisterListener(this)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val x : Float = event?.values?.get(0) as Float
            val y : Float = event?.values?.get(1) as Float
            val z : Float = event?.values?.get(2) as Float
            Handler(Looper.getMainLooper()).postDelayed({
                mBookViewModel.sensorXyzData.postValue(intArrayOf(x.toInt(),y.toInt(),z.toInt()))
            }, 2000)
        }
        else if (event!!.sensor.type == Sensor.TYPE_LIGHT) {
            val light : Float = event?.values?.get(0) as Float
            Handler(Looper.getMainLooper()).postDelayed({
                mBookViewModel.sensorLightData.postValue(light.toInt())
                Log.e("light","In : ${light.toInt()}")
            }, 2000)
        }

//        else if (event!!.sensor.type == Sensor.TYPE_PROXIMITY) {
//            val proximity : Float = event?.values?.get(0) as Float
//            Handler(Looper.getMainLooper()).postDelayed({
//                mBookViewModel.sensorProximityData.postValue(proximity.toInt())
//                Log.e("sensorProximityData","In : ${proximity.toInt()}")
//            }, 2000)
//        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}