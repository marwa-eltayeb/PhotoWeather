package com.marwaeltayeb.photoweather.ui.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.photoweather.R

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyList: List<Uri> = ArrayList()

    private lateinit var mItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(imageUri: Uri?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentImageUri: Uri = historyList[position]
        holder.photoWeather.setImageURI(currentImageUri)

        if(::mItemClickListener.isInitialized){
            holder.itemView.setOnClickListener {
                mItemClickListener.onItemClick(currentImageUri)
            }
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }


    fun setHistoryList(images: List<Uri>){
        this.historyList = images
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mItemClickListener = listener
    }

    class HistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var photoWeather: ImageView = itemView.findViewById(R.id.imgWeatherImage)
    }
}