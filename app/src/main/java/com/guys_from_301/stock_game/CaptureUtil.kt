package com.guys_from_301.stock_game

import android.app.usage.ExternalStorageStats
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.os.EnvironmentCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class CaptureUtil(){

    private val CAPTURE_PATH = "/Tardis/capture"
    private val strFolderPath = Environment.getExternalStorageDirectory().absolutePath+CAPTURE_PATH

    private fun captureView(view: View) :Bitmap?{
        var capturedView : Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            capturedView = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(capturedView)
            view.draw(canvas)
        } catch (e: Exception) {
            Log.d("Giho","Failed to capture screenshot because:" + e.message)
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return capturedView
    }

    private fun saveCapture(bitmap: Bitmap){
        val folder = File(strFolderPath)
        if(!folder.exists()){
            folder.mkdirs()
        }
        val strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png"
        val fileCacheItem = File(strFilePath)
        if (fileCacheItem.exists()) fileCacheItem.delete()
        try {
            var fos = FileOutputStream(fileCacheItem)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
        } catch (e : FileNotFoundException){
            Log.d("Giho","Failed to save file because:" + e.message)
        }
    }

    fun captureAndSaveView(view: View){
        Log.d("Giho","CaptureAndSaveViewStart")
        saveCapture(captureView(view)!!)

    }


}

