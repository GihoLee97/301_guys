package com.guys_from_301.stock_game

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.widget.Button
//import androidx.core.content.ContextCompat.startActivity
import android.widget.Toast
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSetDB

class Dialog_game_exit (context : Context)  {
    var mContext: Context? = context

    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnsave : Button
    private lateinit var btncancel : Button
    private lateinit var btnexit : Button
    private var gameNormalDb: GameNormalDB? = null
    private var gameSetDb: GameSetDB? = null

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_game_exit)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        gameNormalDb = GameNormalDB.getInstace(dlg.context)
        gameSetDb = GameSetDB.getInstace(dlg.context)
        btnsave = dlg.findViewById(R.id.btn_save)
        btnsave.setOnClickListener{
            gameend = !gameend /////////////////////////////////////////////////////////////////////
            
        }


        btnsave.setOnClickListener {
            if (item1Active) {
                Toast.makeText(dlg.context, "시간 역행 중에는 게임을 저장할 수 없습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val addRunnable = Runnable {
                    bought = bought1x * aver1x + bought3x * aver3x + boughtinv1x * averinv1x + boughtinv3x * averinv3x
                    val newGameNormalDB = GameNormal(localdatatime, asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear,"저장", 0F,0F,0, 0 , quant1x, quant3x, quantinv1x, quantinv3x,
                        bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
                        pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot,0F, dividendtot, taxtot , "nothing", item1Active, item1Length, item1Able, item2Active, item3Active, item4Active, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff)
                    gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()
                dlg.dismiss()
                gameend = !gameend /////////////////////////////////////////////////////////////////////
                endsuccess = false
                val intent = Intent(mContext, MainActivity::class.java)
//                (mContext as Activity).finish()
                mContext?.startActivity(intent)
            }

          }

        btnexit = dlg.findViewById(R.id.btn_exit)
        btnexit.setOnClickListener {
//            Toast.makeText(context, "메인 액티비티 종료", Toast.LENGTH_SHORT).show()
//            dlg.dismiss()
//            (context as GameNormalActivity).finish()
            val deleteRunnable = Runnable {
                gameNormalDb?.gameNormalDao()?.deleteAll()
                gameSetDb?.gameSetDao()?.deleteAll()
            }
            val deleteThread = Thread(deleteRunnable)
            deleteThread.start()
            dlg.dismiss()
            gameend = !gameend /////////////////////////////////////////////////////////////////////
            endsuccess = true
            (mContext as Activity).finish()
        }
        btncancel = dlg.findViewById(R.id.btn_cancel)
        btncancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////
        }
        dlg.show()
    }

//    fun setOnBuyClickedListener(listener: (List<Float>)->Unit){
//        this.listenter = object : BuyDialogClickedListener{
//            override fun onBuyClicked(content: List<Float>) {
//                listener(content)
//            }
//        }
//    }
//
//    interface BuyDialogClickedListener{
//        fun onBuyClicked(content: List<Float>)
//    }

}