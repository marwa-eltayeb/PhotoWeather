package com.marwaeltayeb.photoweather.ui.history

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.marwaeltayeb.photoweather.ui.preview.PreviewActivity
import com.marwaeltayeb.photoweather.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        setUpObserver()

        historyViewModel.loadCachedPhotos(this, cacheDir)
    }

    private fun initViews() {
        historyAdapter = HistoryAdapter()
        binding.rcHistoryList.adapter = historyAdapter

        val gridLayoutManager = GridLayoutManager(this, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 4)
        binding.rcHistoryList.layoutManager = gridLayoutManager
        binding.rcHistoryList.setHasFixedSize(true)

        historyAdapter.setOnItemClickListener(this)
    }

    private fun setUpObserver() {
        historyViewModel.getCachedPhotos().observe(this, { photoUris ->
            binding.txtMessage.visibility = View.GONE
            historyAdapter.submitList(photoUris)
        })
    }

    override fun onItemClick(imageUri: Uri) {
        intent = Intent(this, PreviewActivity::class.java)
        intent.data = imageUri
        startActivity(intent)
    }
}