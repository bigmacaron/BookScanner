package kr.kro.fatcats.bookscanner.fragment.drawer

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kr.kro.fatcats.bookscanner.activites.MainActivity
import kr.kro.fatcats.bookscanner.activites.MainActivity.Companion.mBookViewModel
import kr.kro.fatcats.bookscanner.api.BookRepository
import kr.kro.fatcats.bookscanner.api.RoomBookInfoDao
import kr.kro.fatcats.bookscanner.databinding.ItemBookListBinding
import kr.kro.fatcats.bookscanner.fragment.ContentFragment.Companion.contentFragment
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.ListInfo
import kr.kro.fatcats.bookscanner.util.Constants
import org.jetbrains.anko.toast

class DrawerAdapter (private val db : RoomBookInfoDao,context : Context):RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {
    private val items : ArrayList<ListInfo> = arrayListOf()
    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerAdapter.ViewHolder {

        val binding = ItemBookListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item,View.OnClickListener {
                runBlocking(Dispatchers.IO) {
                    mBookViewModel.mainBookInfo.postValue( db.getDataForIsbn(item.isbn!!.toLong()))
                    mBookViewModel.fragment.postValue(Constants.DialogType.TIMER)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: ArrayList<ListInfo>){
        items.addAll(item)
        notifyDataSetChanged()
    }
    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }
    fun refresh(){
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        val alert : AlertDialog.Builder = AlertDialog.Builder(context)
        alert.setTitle("삭제")
        alert.setMessage("삭제한 데이터는 복구가 불가능 합니다. 삭제하시겠습니까?")
        alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            runBlocking(Dispatchers.IO) {
                Log.d("items","item =>${items[position].isbn}")
                items[position].isbn?.let { db.deleteItem(it) }
            }
            items.removeAt(position)
            notifyDataSetChanged()
            dialog.dismiss()
        })
        alert.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
            notifyDataSetChanged()
            dialog.dismiss()
        })
        alert.show()
    }


    inner class ViewHolder (private val binding : ItemBookListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListInfo, listener: View.OnClickListener) {
            binding.apply {
                this.data = data
                itemView.setOnClickListener(listener)
            }
        }

    }


}