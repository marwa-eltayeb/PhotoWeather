package com.marwaeltayeb.photoweather.ui.history

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.marwaeltayeb.photoweather.BuildConfig
import com.marwaeltayeb.photoweather.ui.preview.PreviewActivity
import com.marwaeltayeb.photoweather.databinding.ActivityHistoryBinding
import java.io.File

private const val TAG = "HistoryActivity"

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHistoryBinding

    private lateinit var historyAdapter: HistoryAdapter
    private var historyList: ArrayList<Uri> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imagesFolder = File(cacheDir, "images")
        val lists = imagesFolder.listFiles()
        if(lists!= null) {
            for (item in lists) {
                Log.d(TAG, item.absolutePath + "")
                val uri: Uri = FileProvider.getUriForFile(
                    this,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    item
                )
                historyList.add(uri)
            }
            binding.txtMessage.visibility = View.GONE
        }

        initViews()
    }

    private fun initViews(){
        historyAdapter = HistoryAdapter()
        binding.rcHistoryList.adapter = historyAdapter

        val gridLayoutManager = GridLayoutManager(this, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 4)
        binding.rcHistoryList.layoutManager = gridLayoutManager
        binding.rcHistoryList.setHasFixedSize(true)
        historyAdapter.submitList(historyList)

        historyAdapter.setOnItemClickListener(this)
    }

    override fun onItemClick(imageUri: Uri) {
        intent = Intent(this, PreviewActivity::class.java)
        intent.data = imageUri
        startActivity(intent)
    }
}