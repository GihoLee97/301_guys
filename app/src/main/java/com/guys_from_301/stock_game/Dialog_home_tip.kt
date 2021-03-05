package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class Dialog_home_tip
constructor(context: Context) : Dialog(context){
    init {
        val random = Random()
        val num : Int = random.nextInt(61)
        setCanceledOnTouchOutside(false)
        setCancelable(false)

        setContentView(R.layout.dialog_home_tip)
//        getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        findViewById<TextView>(R.id.tv_tip_title).text = tip_title[num]
        findViewById<TextView>(R.id.tv_tip_information).text = tip_information[num]

        findViewById<LinearLayout>(R.id.ll_tip_admin).setOnClickListener{
            val random_1 = Random()
            val num_1 : Int = random_1.nextInt(61)
            findViewById<TextView>(R.id.tv_tip_title).text = tip_title[num_1]
            findViewById<TextView>(R.id.tv_tip_information).text = tip_information[num_1]
        }
        findViewById<ImageButton>(R.id.ib_cancel).setOnClickListener{
            dismiss()
        }
    }
}
