package kr.kro.fatcats.bookscanner.util

import android.app.ActionBar
import android.util.Log
import android.view.Gravity
import android.view.Gravity.END
import android.view.Gravity.START
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.model.BookInfo
import kr.kro.fatcats.bookscanner.model.Doc
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

@BindingAdapter("bindTitle")
fun bindViewTitle(view: TextView, values: BookInfo?) {
    if(!values?.docs.isNullOrEmpty()){
        view.text = "${values?.docs?.get(0)?.title}"
    }else{
        view.text =""
    }
}
@BindingAdapter("bindAuthor")
fun bindViewAuthor(view: TextView, values: BookInfo?) {
    if(!values?.docs.isNullOrEmpty()){
        view.text = "${values?.docs?.get(0)?.author}"
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


