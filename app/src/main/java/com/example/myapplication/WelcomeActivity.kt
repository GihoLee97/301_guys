package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.util.Log
import android.view.GestureDetector
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import java.lang.invoke.ConstantCallSite

class WelcomeActivity : AppCompatActivity() {

    private val welcomeViewModel: WelcomViewModel by viewModels()
    private lateinit var mGestureDetector : GestureDetectorCompat
    private var click = 0

    private lateinit var layout_welcome : ConstraintLayout
    private lateinit var layout_welcome1 : ConstraintLayout
    private lateinit var layout_welcome2 : ConstraintLayout
    private lateinit var layout_welcome3 : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        mGestureDetector = GestureDetectorCompat(this,GestureListener())

        layout_welcome = findViewById(R.id.Layout_welcome)
        layout_welcome1 = findViewById(R.id.Layout_welcome1)
        layout_welcome2 = findViewById(R.id.Layout_welcome2)
        layout_welcome3 = findViewById(R.id.Layout_welcome3)

        layout_welcome.visibility = View.VISIBLE
        layout_welcome1.visibility = View.VISIBLE
        layout_welcome2.visibility = View.INVISIBLE
        layout_welcome3.visibility = View.INVISIBLE

        val clickObserver = Observer<Int> { click ->
            // Update the UI, in this case, a TextView.
            if(click==0){
                layout_welcome1.visibility = View.INVISIBLE
                layout_welcome2.visibility = View.VISIBLE
                layout_welcome3.visibility = View.INVISIBLE
            } else if(click==1){
                layout_welcome1.visibility = View.INVISIBLE
                layout_welcome2.visibility = View.INVISIBLE
                layout_welcome3.visibility = View.VISIBLE
            } else if(click==2){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }

        welcomeViewModel.currClick.observe(this,clickObserver)
    }

    override fun onTouchEvent(event: MotionEvent?) : Boolean{
        mGestureDetector.onTouchEvent(event)
        val action : Int = MotionEventCompat.getActionMasked(event)
        val DEBUG_TAG = "Giho"
        when(action){
            MotionEvent.ACTION_DOWN -> {
                Log.d(DEBUG_TAG, "Action was DOWN")
                click()
                true
            }
//            MotionEvent.ACTION_MOVE -> {
//                Log.d(DEBUG_TAG, "Action was MOVE")
//                click()
//                true
//            }
//            MotionEvent.ACTION_UP -> {
//                Log.d(DEBUG_TAG, "Action was UP")
//                click()
//                true
//            }
//            MotionEvent.ACTION_CANCEL -> {
//                Log.d(DEBUG_TAG, "Action was CANCEL")
//                click()
//                true
//            }
//            MotionEvent.ACTION_OUTSIDE -> {
//                Log.d(DEBUG_TAG, "Movement occurred outside bounds of current screen element")
//                click()
//                true
//            }
            else -> super.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private fun click(){
        Log.d("Giho",click.toString())
        welcomeViewModel.click(click)
        click+=1
    }

    private class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.d("Giho","SingleTapUp")
            return super.onSingleTapUp(e)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            Log.d("Giho","Down")
            return super.onDown(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.d("Giho","Fling")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d("Giho","DoubleTap")
            return super.onDoubleTap(e)
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.d("Giho","onScroll")
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onContextClick(e: MotionEvent?): Boolean {
            Log.d("Giho","SingleTapUp")
            return super.onContextClick(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.d("Giho","SingleTapConfirmed")
            return super.onSingleTapConfirmed(e)
        }

        override fun onShowPress(e: MotionEvent?) {
            Log.d("Giho","ShowPress")
            super.onShowPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            Log.d("Giho","DoubleTapEvent")
            return super.onDoubleTapEvent(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            Log.d("Giho","LongPress")
            super.onLongPress(e)
        }
    }
}

