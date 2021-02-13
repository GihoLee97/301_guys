package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.*
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.GameNormalDB

class Dialog_auto(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    //Roomdata관련
    private var gameNormalDb: GameNormalDB? = null

    private var autoratiotemp: Int = 0
    private var auto1xtemp: Int = 0

    private lateinit var seekbarAutoratio: SeekBar
    private lateinit var seekbarAuto1x: SeekBar
    private lateinit var tvAutoratio: TextView
    private lateinit var tvAuto1x: TextView
    private lateinit var tvAuto3x: TextView
    private lateinit var tvAuto: TextView
    private lateinit var btnAutook: Button
    private lateinit var btnAutocancel: Button


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   // 타이틀바 제거
        dlg.setContentView(R.layout.dialog_auto)     // 다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        gameNormalDb = GameNormalDB.getInstace(dlg.context)
        seekbarAutoratio = dlg.findViewById(R.id.seekbar_autoratio)
        seekbarAuto1x = dlg.findViewById(R.id.seekbar_auto1x)
        tvAutoratio = dlg.findViewById(R.id.tv_autoratio)
        tvAuto1x = dlg.findViewById(R.id.tv_auto1x)
        tvAuto3x = dlg.findViewById(R.id.tv_auto3x)
        tvAuto = dlg.findViewById(R.id.tv_auto)
        btnAutook = dlg.findViewById(R.id.btn_autook)
        btnAutocancel = dlg.findViewById(R.id.btn_autocancel)


        auto1xtemp = auto1x // 현재 값 불러오기
        autoratiotemp = autoratiotemp // 현재 값 불러오기

        seekbarAutoratio.progress = auto1xtemp // 현재 상태 Seekbar 에 반영
        seekbarAuto1x.progress = auto1xtemp // 현재 상태 Seekbar 에 반영

        tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"


        seekbarAutoratio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                auto1xtemp = progress
                tvAuto1x.text = "$auto1xtemp %"
                tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                auto1xtemp = seekBar!!.progress
                tvAuto1x.text = "$auto1xtemp %"
                tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                auto1xtemp = seekBar!!.progress
                tvAuto1x.text = "$auto1xtemp %"
                tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"
            }
        })


        seekbarAuto1x.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                auto3xtemp = progress
                tvAuto3x.text = "$auto3xtemp %"
                tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                auto3xtemp = seekBar!!.progress
                tvAuto3x.text = "$auto3xtemp %"
                tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                auto3xtemp = seekBar!!.progress
                tvAuto3x.text = "$auto3xtemp %"
                tvAuto.text = "매월 월급의 $auto1xtemp% 만큼 1x, $auto3xtemp% 만큼 3x 자동 매수"
            }
        })


        btnAutook.setOnClickListener {
            if ((auto1xtemp > 0) || (auto3xtemp > 0)) {
                autobuy = true
                auto1x = auto1xtemp // 변경사항 전역변수에 저장
                autoratiotemp = autoratiotemp // 변경사항 전역변수에 저장

                Toast.makeText(dlg.context, "아이템을 이용하여 레버리지 ETF 거래를 언락해야합니다", Toast.LENGTH_SHORT).show()
            }

            else {
                autobuy = false
                auto1x = auto1xtemp // 변경사항 전역변수에 저장
                auto3x = auto3xtemp // 변경사항 전역변수에 저장

                Toast.makeText(dlg.context, "자동 매수 기능을 사용하지 않습니다", Toast.LENGTH_SHORT).show()
            }

                dlg.dismiss()
                click = !click /////////////////////////////////////////////////////////////////////////
        }


        btnAutocancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }







    }
}