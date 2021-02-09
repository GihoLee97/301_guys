package com.example.myapplication

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import com.example.myapplication.data.GameNormal
import com.example.myapplication.data.GameNormalDB

class Dialog_game_exit (context : Context)  {
    var mContext: Context? = context

    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnsave : Button
    private lateinit var btncancel : Button
    private lateinit var btnexit : Button
    private var gameNormalDb: GameNormalDB? = null

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_game_exit)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        gameNormalDb = GameNormalDB.getInstace(dlg.context)
        btnsave = dlg.findViewById(R.id.btn_save)
        btnsave.setOnClickListener{
            gameend = !gameend /////////////////////////////////////////////////////////////////////
        }


        btnsave.setOnClickListener {

            val addRunnable = Runnable {
                val newGameNormalDB = GameNormal(localdatatime, asset, cash, 0F, evaluation, profit, profittot,"저장", 0F,0F,0,0, profityear, quant1x, quant3x, quantinv1x, quantinv3x, aver1x, aver3x, averinv1x, averinv3x, val1x, val3x, valinv1x, valinv3x,
                        tradecomtot,0F, dividendtot, taxtot,"nothing", 0,0,0)
                gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
            }
            val addThread = Thread(addRunnable)
            addThread.start()
            dlg.dismiss()
//            val intent = Intent(mContext, MainActivity::class.java)
            (mContext as Activity).finish()
          }

        btnexit = dlg.findViewById(R.id.btn_exit)
        btnexit.setOnClickListener {
//            Toast.makeText(context, "메인 액티비티 종료", Toast.LENGTH_SHORT).show()
//            dlg.dismiss()
//            (context as GameNormalActivity).finish()
            dlg.dismiss()
            gameend = !gameend /////////////////////////////////////////////////////////////////////
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
