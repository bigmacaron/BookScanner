package kr.kro.fatcats.bookscanner.fragment.drawer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.kro.fatcats.bookscanner.databinding.ItemBookListBinding
import kr.kro.fatcats.bookscanner.model.ListInfo

class DrawerAdapter :RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {
    private val items : ArrayList<ListInfo> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerAdapter.ViewHolder {
        Log.d("data2" , "onCreateViewHolder")
        val binding = ItemBookListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Log.d("data3" , "$item")
        (holder as ViewHolder).apply {
            bind(item,View.OnClickListener {  })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: ArrayList<ListInfo>){
        Log.d("data1","$item")
        items.addAll(item)
        notifyDataSetChanged()
    }
    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding : ItemBookListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListInfo, listener: View.OnClickListener) {
            Log.d("data4" , "$data")
            binding.apply {
                this.data = data
            }
        }
    }


}