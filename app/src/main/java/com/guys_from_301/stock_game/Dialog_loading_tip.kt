package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.postDelayed
import com.guys_from_301.stock_game.data.GameSetDB
import java.util.*

class Dialog_loading_tip
constructor(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen){
    init {
        setContentView(R.layout.dialog_loading_tip)
        val random = Random()
        val mContext : Context = context
        val num : Int = random.nextInt(61)
        setCanceledOnTouchOutside(false)
        setCancelable(false)

        var gamesetDB : GameSetDB? = null
        gamesetDB = GameSetDB.getInstace(mContext)


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
        findViewById<TextView>(R.id.tv_initial_asset).text =
                "$"+dec.format(SET_CASH_STEP[gamesetDB?.gameSetDao()?.getSetCash(accountID!!)!!]).toString()
        findViewById<TextView>(R.id.tv_initial_monthly).text =
                "$"+dec.format(SET_MONTHLY_STEP[gamesetDB?.gameSetDao()?.getSetMonthly(accountID!!)!!]).toString()
        findViewById<TextView>(R.id.tv_initial_salary_raise).text =
                SET_SALARY_RAISE_STEP[gamesetDB?.gameSetDao()?.getSetSalaryRaise(accountID!!)!!].toString()+"%"

        for(i in 0..10000){
            Handler().postDelayed({
                findViewById<View>(R.id.view_progressTardis).getBackground().setLevel(i)
            }, 1L+i * 1)
        }




    }
}
