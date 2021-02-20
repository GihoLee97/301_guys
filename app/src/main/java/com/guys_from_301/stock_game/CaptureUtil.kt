package com.guys_from_301.stock_game

import android.app.usage.ExternalStorageStats
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.EnvironmentCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URI

class CaptureUtil(){

    private val CAPTURE_PATH = "/Tardis"
    private val strFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+CAPTURE_PATH

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
        }
        // return the bitmap
        return capturedView
    }

    private fun saveCapture(bitmap: Bitmap) :Uri{
        val path = File(strFolderPath)
        if(!path.exists()){
            path.mkdirs()
            Log.d("Giho","Folder created")
        }
        val strFilename = "/" + System.currentTimeMillis() + ".png"
        val fileCacheItem = File(path,strFilename)

        try {
            var fos = FileOutputStream(fileCacheItem)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
        } catch (e : FileNotFoundException){
            Log.d("Giho","Failed to save file because:" + e.message)
        }
        Log.d("Giho","Url : "+fileCacheItem.toURL().toString())
        return fileCacheItem.toUri()
    }

    fun captureAndSaveView(view: View) :Uri{
        Log.d("Giho","CaptureAndSaveViewStart")
        val uri = saveCapture(captureView(view)!!)
        return uri
    }


}

