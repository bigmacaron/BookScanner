package kr.kro.fatcats.bookscanner.fragment.drawer

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
import kr.kro.fatcats.bookscanner.model.BookViewModel
import kr.kro.fatcats.bookscanner.model.BookViewModelFactory
import kr.kro.fatcats.bookscanner.model.ListInfo

class DrawerAdapter (private val db : RoomBookInfoDao):RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {
    private val items : ArrayList<ListInfo> = arrayListOf()

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

    inner class ViewHolder (private val binding : ItemBookListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListInfo, listener: View.OnClickListener) {
            binding.apply {
                this.data = data
                itemView.setOnClickListener(listener)
            }
        }
    }


}