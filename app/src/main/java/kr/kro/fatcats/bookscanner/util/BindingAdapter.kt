package kr.kro.fatcats.bookscanner.util

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity.END
import android.view.Gravity.START
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.model.BookInfo
import java.util.*

@BindingAdapter("bindTimer1")
fun bindViewTimer1(view: TextView, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    view.animate().rotationX(360F).rotationXBy(360F).setDuration(400).start()
    view.text = "$timeLeftFormatted"
}

@BindingAdapter("bindTimer2")
fun bindViewTimer2(view: TextView, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    view.animate().rotationX(360F).rotationXBy(360F).setDuration(400).start()
    view.text = "$timeLeftFormatted"
}

@BindingAdapter("bindTimer3")
fun bindViewTimer3(view: TextView, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    view.animate().rotationX(360F).rotationXBy(360F).setDuration(400).start()
    view.text = "$timeLeftFormatted"
}

@BindingAdapter("bindTimer4")
fun bindViewTimer4(view: TextView, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    view.animate().rotationX(360F).rotationXBy(360F).setDuration(400).start()
    view.text = "$timeLeftFormatted"
}

@BindingAdapter("bindTimer5")
fun bindViewTimer5(view: TextView, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    view.animate().rotationX(360F).rotationXBy(360F).setDuration(400).start()
    view.text = "$timeLeftFormatted"
}

@BindingAdapter("bindTimer6")
fun bindViewTimer6(view: TextView, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    view.animate().rotationX(360F).rotationXBy(360F).setDuration(400).start()
    view.text = "$timeLeftFormatted"
}


@SuppressLint("SetTextI18n")
@BindingAdapter("bindDrawerTime")
fun bindViewDrawerTime(view: TextView, values: Long?) {
    val minutes = ((values?.div(1000))?.div(60))
    var timeLeftFormatted = String.format(Locale.getDefault(), "약%03d분", minutes)
    values?.let {  view.text = timeLeftFormatted  }
}
@BindingAdapter("bindTimerButtonText")
fun bindViewTimerButtonText(view: TextView, values: Boolean?) {
    values?.let {
        if(values){
            view.text = Constants.MainText.START
        }else{
            view.text = Constants.MainText.PAUSE
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTotalTime")
fun bindViewTotalTime(view: TextView, values: Long?) {
    val hour    = ((values?.div(1000))?.div(60))?.div(60)
    val minutes = ((values?.div(1000))?.div(60))?.rem(60)
    val seconds = ((values?.div(1000))?.rem(60))
    var timeLeftFormatted = String.format(Locale.getDefault(),  "%02d:%02d:%02d",hour, minutes, seconds)
    if(seconds == null){
        timeLeftFormatted = "00:00:00"
    }
    view.text =  Constants.MainText.TIME_VIEW_TEXT + timeLeftFormatted
}

//@SuppressLint("SetTextI18n")
//@BindingAdapter("bindTimer")
//fun bindViewTimer(view: TextView, values: Long?) {
//    val hour    = ((values?.div(1000))?.div(60))?.div(60)
//    val minutes = ((values?.div(1000))?.div(60))
//    val seconds = ((values?.div(1000))?.rem(60))
//    var timeLeftFormatted = String.format(Locale.getDefault(),  "%02d:%02d:%02d",hour, minutes, seconds)
//    if(seconds == null){
//        timeLeftFormatted = "00:00:00"
//    }
//        view.text =  timeLeftFormatted
//}

@BindingAdapter("bindDrawerType")
fun bindViewDrawerType(view: FrameLayout, values: String?) {
    values?.let{
        Log.d("bindAdapter", " => $values")
        when(values) {
            "end" -> {
                val test = DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT,DrawerLayout.LayoutParams.MATCH_PARENT)
                test.gravity = START
                view.layoutParams = test
                Log.d("bindAdapter", " => End 진입")
            }
            "start" -> {
                val test = DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT,DrawerLayout.LayoutParams.MATCH_PARENT)
                test.gravity = END
                view.layoutParams = test
                Log.d("bindAdapter", " => start 진입")
            }
            else -> {}
        }
    }
}

@BindingAdapter("bindTitle")
fun bindViewTitle(view: TextView, values: BookInfo?) {
    if(!values?.items.isNullOrEmpty()){
        view.text = "${values?.items?.get(0)?.title}"
    }else{
        view.text =""
    }
}
@BindingAdapter("bindAuthor")
fun bindViewAuthor(view: TextView, values: BookInfo?) {
    if(!values?.items.isNullOrEmpty()){
        view.text = "${values?.items?.get(0)?.author}"
    }else{
        view.text =""
    }
}
@BindingAdapter("bindBookImage")
fun bindViewBookImage(view: ImageView, values: String?) {
    if(values != null){
        Glide.with(view.context)
            .load("$values")
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(R.drawable.ic_book)
            .into(view)
    }else{
        Log.d("도서정보","URL없음")
        view.setImageResource(R.drawable.ic_book)
    }
}


