package com.marwaeltayeb.photoweather.ui.preview

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.marwaeltayeb.photoweather.R
import com.marwaeltayeb.photoweather.databinding.ActivityPreviewBinding
import com.marwaeltayeb.photoweather.utils.ImageUtils

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private lateinit var currentImageURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageURI = intent.data!!
        binding.imagePreview.setImageURI(currentImageURI)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.preview_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_action -> {
                ImageUtils.shareImageUri(this, currentImageURI)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}