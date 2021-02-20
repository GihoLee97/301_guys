package com.guys_from_301.stock_game

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.widget.Button
//import androidx.core.content.ContextCompat.startActivity
import android.widget.Toast
import com.guys_from_301.stock_game.data.*
import java.time.LocalDateTime

var relativeprofitrate: Float = 0F//시장대비수익률

class Dialog_game_exit (context : Context)  {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnsave : Button
    private lateinit var btncancel : Button
    private lateinit var btnexit : Button
    private var gameNormalDb: GameNormalDB? = null
    private var gameSetDb: GameSetDB? = null
    private var profileDb: ProfileDB? = null
    private lateinit var localDateTime: LocalDateTime

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_game_exit)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        relativeprofitrate = (profitrate+100) / marketrate*100-100

        profileDb = ProfileDB.getInstace(dlg.context)
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
                eventCount = 0
                val addRunnable = Runnable {
                    val newProfile = Profile()
                    newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                    newProfile.rank = profileDb?.profileDao()?.getRank()!!
                    newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
                    newProfile.level = profileDb?.profileDao()?.getLevel()!!
                    newProfile.exp = profileDb?.profileDao()?.getExp()!!
                    newProfile.login = profileDb?.profileDao()?.getLogin()!!
                    newProfile.money = profileDb?.profileDao()?.getMoney()!!
                    newProfile.profit = (profileDb?.profileDao()?.getProfit()!!*profileDb?.profileDao()?.getHistory()!!+relativeprofitrate* tradeday)/(profileDb?.profileDao()?.getHistory()!!+ tradeday)
                    newProfile.history = profileDb?.profileDao()?.getHistory()!!+ tradeday
                    newProfile.roundcount = profileDb?.profileDao()?.getRoundCount()!!
                    newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                    newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                    profileDb?.profileDao()?.update(newProfile)
                    bought = bought1x * aver1x + bought3x * aver3x + boughtinv1x * averinv1x + boughtinv3x * averinv3x
                    localDateTime = LocalDateTime.now()
                    val newGameNormalDB = GameNormal(localdatatime, asset, cash, input, bought, sold, evaluation, profit, profitrate, profittot, profityear,"저장", 0F,0F,0, 0 , quant1x, quant3x, quantinv1x, quantinv3x,
                        bought1x, bought3x, boughtinv1x, boughtinv3x, aver1x, aver3x, averinv1x, averinv3x, buylim1x, buylim3x, buyliminv1x, buyliminv3x, price1x, price3x, priceinv1x, priceinv3x, val1x, val3x, valinv1x, valinv3x,
                        pr1x, pr3x, prinv1x, prinv3x, setMonthly, monthToggle, tradecomtot,0F, dividendtot, taxtot , "nothing", item1Active, item1Length, item1Able, item2Active, item3Active, item4Active, autobuy, autoratio, auto1x, endpoint, countYear, countMonth, snpNowdays, snpNowVal, snpDiff, setId, relativeprofitrate, localdatatime)
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
            eventCount = 0
//            Toast.makeText(context, "메인 액티비티 종료", Toast.LENGTH_SHORT).show()
//            dlg.dismiss()
//            (context as GameNormalActivity).finish()
            val deleteRunnable = Runnable {
                val newProfile = Profile()
                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
                newProfile.level = profileDb?.profileDao()?.getLevel()!!
                newProfile.exp = profileDb?.profileDao()?.getExp()!!
                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                newProfile.money = profileDb?.profileDao()?.getMoney()!!
                newProfile.profit = (profileDb?.profileDao()?.getProfit()!!*profileDb?.profileDao()?.getHistory()!!+relativeprofitrate* tradeday)/(profileDb?.profileDao()?.getHistory()!!+ tradeday)
                newProfile.history = profileDb?.profileDao()?.getHistory()!!+ tradeday
                newProfile.roundcount = profileDb?.profileDao()?.getRoundCount()!!+1
                newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                profileDb?.profileDao()?.update(newProfile)
                gameSetDb?.gameSetDao()?.deleteId(setId)
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
            eventCount = 0
            dlg.dismiss()
            if (click== true){
                click = !click /////////////////////////////////////////////////////////////////////
            }

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
