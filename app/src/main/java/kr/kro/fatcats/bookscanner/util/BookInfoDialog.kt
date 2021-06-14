package kr.kro.fatcats.bookscanner.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.BR
import kr.kro.fatcats.bookscanner.databinding.BookInfoCustomDialogBinding
import kr.kro.fatcats.bookscanner.model.BookViewModel

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
        binding.setVariable(BR.title,title)
        binding.setVariable(BR.url,url)
        binding.setVariable(BR.author,author)
        binding.setVariable(BR.publisher,publisher)

        dialog.show()



//        val photoButton = dialog.findViewById<ImageView>(R.id.iv_select_photo)
//        val videoButton = dialog.findViewById<ImageView>(R.id.iv_select_video)
//        val closeButton = dialog.findViewById<ImageView>(R.id.iv_close)
//        photoButton.setOnClickListener {
//            type = "IMAGE"
//            onClickListener.onClick(type)
//            dialog.dismiss()
//        }
//        videoButton.setOnClickListener {
//            type = "VIDEO"
//            onClickListener.onClick(type)
//            dialog.dismiss()
//        }
//        closeButton.setOnClickListener {
//            dialog.dismiss()
//        }
    }

//    interface ButtonClickListener{
//        fun onClick(type : String)
//    }
//    private lateinit var onClickListener: ButtonClickListener
//
//    fun setOnClickedListener(listener: ButtonClickListener){
//        onClickListener = listener
//    }
}