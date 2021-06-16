package kr.kro.fatcats.bookscanner.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.databinding.BookInfoCustomDialogBinding
import kr.kro.fatcats.bookscanner.listeners.DialogClickListener
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext


class BookInfoDialog(context:Context)   {
    private val dialog = Dialog(context)
    private lateinit var binding : BookInfoCustomDialogBinding
    private lateinit var mBookViewModel: BookViewModel
    private lateinit var onClickListener : DialogClickListener
    private val mContext = context
    init {
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    fun myDialog(
        title: String?,
        url:String?,
        author:String?,
        publisher:String?,
        isRoomData: String?,
        listener: DialogClickListener
    ){
        dialog.apply {
            binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.book_info_custom_dialog, null, false)
            setContentView(binding.root)
            window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mBookViewModel = ViewModelProvider(ViewModelStore(), BookViewModelFactory(BookRepository())).get(BookViewModel::class.java)
        binding.setVariable(BR.title,title)
        binding.setVariable(BR.url,url)
        binding.setVariable(BR.author,author)
        binding.setVariable(BR.publisher,publisher)
        binding.setVariable(BR.isRoom,isRoomData)

        dialog.show()
        onClickListener = listener

        binding.ivCloseDialog.setOnClickListener {
            closeBtnAnimation()
        }
        binding.tvTimer.setOnClickListener {
            timerBtnAnimation()
            onClickListener.onClick(Constants.DialogType.TIMER)
        }

        binding.tvReg.setOnClickListener {
            regBtnAnimation()
            onClickListener.onClick(Constants.DialogType.REGISTER)
        }

    }


    private fun regBtnAnimation(){
        val rotateMoveFadeOut = AnimationUtils.loadAnimation(mContext,R.anim.rotation_move_fade_out_up_right)
        binding.root.setAnimation(rotateMoveFadeOut)
        binding.root.startAnimation(rotateMoveFadeOut)
        rotateMoveFadeOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                dialog.dismiss()
            }
            override fun onAnimationRepeat(animation: Animation?) {
                dialog.dismiss()
            }
        })
    }

    private fun timerBtnAnimation(){
        val rotateMoveFadeOut = AnimationUtils.loadAnimation(mContext,R.anim.rotation_move_fade_out_bottom_left)
        binding.root.setAnimation(rotateMoveFadeOut)
        binding.root.startAnimation(rotateMoveFadeOut)
        rotateMoveFadeOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                dialog.dismiss()
            }
            override fun onAnimationRepeat(animation: Animation?) {
                dialog.dismiss()
            }
        })
    }
    private fun closeBtnAnimation(){
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