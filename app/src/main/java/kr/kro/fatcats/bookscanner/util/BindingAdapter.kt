package kr.kro.fatcats.bookscanner.util

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity.END
import android.view.Gravity.START
import android.view.View
import android.view.animation.*
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

@BindingAdapter("bindTimer")
fun bindViewTimer(view: View, values: Long?) {
    var timeLeftFormatted : Long? = values
    if(timeLeftFormatted == null){
        timeLeftFormatted = 0L
    }
    if(view is TextView){
        Handler(Looper.getMainLooper()).postDelayed({
            view.text = "$timeLeftFormatted"
        }, 200)
    }else if(view is ImageView){
        view.animate().rotationX(0F).rotationXBy(180f).setDuration(200).start()
    }
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


