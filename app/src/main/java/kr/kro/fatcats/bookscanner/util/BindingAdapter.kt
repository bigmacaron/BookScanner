package kr.kro.fatcats.bookscanner.util

import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("bindDrawerType")
fun bindViewDrawerType(view: FrameLayout, values: String?) {
    values?.let{
        when(values) {
            "start" -> {
                Log.d("bindAdapter", " => start")
            }
            "end" -> {
                Log.d("bindAdapter", " => End")
            }
            else -> {}
        }
    }
}