package kr.kro.fatcats.bookscanner.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import kr.kro.fatcats.bookscanner.R
import kr.kro.fatcats.bookscanner.databinding.BookInfoCustomDialogBinding
import kr.kro.fatcats.bookscanner.databinding.FragmentSubBinding

class BookInfoDialog(context: Context) : Dialog(context) {
    private val dialog = Dialog(context)
    private lateinit var binding: BookInfoCustomDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = BookInfoCustomDialogBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info_custom_dialog)
    }
    fun myDialog(){
        dialog.apply {
            window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        }
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