package kr.kro.fatcats.bookscanner.util

import android.app.ActionBar
import android.util.Log
import android.view.Gravity
import android.view.Gravity.END
import android.view.Gravity.START
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent

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