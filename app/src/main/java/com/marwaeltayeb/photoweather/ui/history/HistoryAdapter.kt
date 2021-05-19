package com.marwaeltayeb.photoweather.ui.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.photoweather.R
import com.marwaeltayeb.photoweather.databinding.ListItemHistoryBinding

class HistoryAdapter : ListAdapter<Uri, RecyclerView.ViewHolder>(UriDiffCallback()) {

    private lateinit var mItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(imageUri: Uri)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentImageUri: Uri = getItem(position)
        (holder as HistoryViewHolder).bind(currentImageUri)

        if(::mItemClickListener.isInitialized){
            holder.itemView.setOnClickListener {
                mItemClickListener.onItemClick(currentImageUri)
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mItemClickListener = listener
    }

    class HistoryViewHolder(private var binding: ListItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image : Uri) {
            binding.imgWeatherImage.setImageURI(image)
        }
    }
}

private class UriDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }
}