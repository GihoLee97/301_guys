package com.guys_from_301.stock_game

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

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

    private fun saveCaptureWithKakao(bitmap: Bitmap) :String{
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
        return strFolderPath+strFilename
    }

    fun captureAndSaveViewWithKakao(view: View, context: Context, activity: Activity) :String{
        requestPermission(context,activity)
        Log.d("Giho","CaptureAndSaveViewStart")
        val path = saveCaptureWithKakao(captureView(view)!!)
        return path
    }


    fun captureAndSaveView(view: View, context: Context, activity: Activity) :Uri{
        requestPermission(context,activity)
        Log.d("Giho","CaptureAndSaveViewStart")
        val uri = saveCapture(captureView(view)!!)
        return uri
    }


}


fun requestPermission(context: Context, activity: Activity){
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }
    else{
        Log.d("Giho","WRITE_EXTERNAL_STORAGE AND READ_EXTERNAL_STORAGE PASS")
    }
}


