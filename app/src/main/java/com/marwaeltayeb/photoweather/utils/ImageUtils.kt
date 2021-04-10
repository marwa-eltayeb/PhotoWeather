package com.marwaeltayeb.photoweather.utils

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.core.content.ContextCompat
import com.marwaeltayeb.photoweather.R

object ImageUtils {

    fun shareImageUri(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "preview/png"
        context.startActivity(intent)
    }

    fun drawTextOnBitmap(context: Context, photo: Bitmap, state: String): Bitmap {
        var bitmap = photo
        var bitmapConfig = bitmap.config
        if (bitmapConfig == null) bitmapConfig = Bitmap.Config.ARGB_8888

        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE

        val relation = Math.sqrt((canvas.width * canvas.height).toDouble()) / 250
        paint.textSize = (17 * relation).toFloat()

        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

        val bounds = Rect()
        var numberOfLines = 0

        val textLines = state.split("\n".toRegex()).toTypedArray()

        for (line in textLines) numberOfLines++

        paint.getTextBounds(state, 0, state.length, bounds)
        val x = 20
        var y = bitmap.height - bounds.height() * numberOfLines
        val mPaint = Paint()
        mPaint.color = ContextCompat.getColor(context, R.color.transparentBlack)

        val left = 0
        val top = bitmap.height - bounds.height() * (numberOfLines.plus(1))
        val right = bitmap.width
        val bottom = bitmap.height
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        for (line in textLines) {
            canvas.drawText(line, x.toFloat(), y.toFloat(), paint)
            y += (paint.descent() - paint.ascent()).toInt()
        }
        return bitmap
    }
}