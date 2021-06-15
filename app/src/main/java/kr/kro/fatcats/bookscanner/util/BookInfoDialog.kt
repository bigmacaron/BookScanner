package kr.kro.fatcats.bookscanner.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.databinding.BookInfoCustomDialogBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory


class BookInfoDialog(context:Context)   {
    private val dialog = Dialog(context)
    private lateinit var binding : BookInfoCustomDialogBinding
    private lateinit var mBookViewModel: BookViewModel
    fun myDialog(title: String?,url:String?,author :String?,publisher:String?){
        dialog.apply {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.book_info_custom_dialog, null, false)
            setContentView(binding.root)
            window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        }
        mBookViewModel = ViewModelProvider(ViewModelStore(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
        binding.setVariable(BR.title,title)
        binding.setVariable(BR.url,url)
        binding.setVariable(BR.author,author)
        binding.setVariable(BR.publisher,publisher)

        dialog.show()

        binding.ivCloseDialog.setOnClickListener {
            timerBtnAnimation()
        }
        binding.btnReg.setOnClickListener {
            regBtnAnimation()
        }
        binding.btnTimer.setOnClickListener {
            timerBtnAnimation()
        }
    }

//    interface ButtonClickListener{
//        fun onClick(type : String)
//    }
//    private lateinit var onClickListener: ButtonClickListener
//
//    fun setOnClickedListener(listener: ButtonClickListener){
//        onClickListener = listener
//    }

    private fun regBtnAnimation(){
        val fadeOut = AlphaAnimation(1F, 0F)
        fadeOut.duration = 800
        val rotateOut = RotateAnimation(0f,360F,50F,50F)
        rotateOut.duration = 800
        binding.root.setAnimation(rotateOut)
        binding.root.startAnimation(rotateOut)
        rotateOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                dialog.dismiss()
            }
            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }
    private fun timerBtnAnimation(){
        val fadeOut = AlphaAnimation(1F, 0F)
        fadeOut.duration = 800
        binding.root.setAnimation(fadeOut)
        binding.root.startAnimation(fadeOut)
        fadeOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                dialog.dismiss()
            }
            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }

}