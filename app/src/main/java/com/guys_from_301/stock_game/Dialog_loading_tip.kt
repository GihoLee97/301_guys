package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class Dialog_loading_tip
constructor(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen){
    init {
        setContentView(R.layout.dialog_loading_tip)
        val random = Random()
        val num : Int = random.nextInt(61)
        setCanceledOnTouchOutside(false)
        setCancelable(false)


//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(getWindow()?.getAttributes())
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT
//        window.setAttributes(lp)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        findViewById<TextView>(R.id.tv_tip_title).text = tip_title[num]
        findViewById<TextView>(R.id.tv_tip_information).text = tip_information[num]

        findViewById<LinearLayout>(R.id.ll_tip_admin).setOnClickListener{
            val random_1 = Random()
            val num_1 : Int = random_1.nextInt(61)
            findViewById<TextView>(R.id.tv_tip_title).text = tip_title[num_1]
            findViewById<TextView>(R.id.tv_tip_information).text = tip_information[num_1]

        }

        findViewById<View>(R.id.view_progressTardis).background.level = 20
    }
}
