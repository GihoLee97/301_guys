package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
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
    private var auto1xratiotemp: Float = 0F
    private var auto3xratiotemp: Float = 0F


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


        autoratiotemp = autoratio // 현재 값 불러오기
        seekbarAutoratio.progress = autoratiotemp // 현재 상태 Seekbar 에 반영


        // 레버리지 거래 아이템 효과 연동
        if (!item4Active) {
            auto1xtemp = 100
            seekbarAuto1x.progress = auto1xtemp
            seekbarAuto1x.isEnabled = false
            seekbarAuto1x.setBackgroundColor(Color.parseColor("#FF808080"))
            tvAuto1x.setBackgroundColor(Color.parseColor("#FF808080"))
            tvAuto3x.setBackgroundColor(Color.parseColor("#FF808080"))
        }
        else {
            auto1xtemp = auto1x // 현재 값 불러오기
            seekbarAuto1x.progress = auto1xtemp // 현재 상태 Seekbar 에 반영
        }

        auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
        auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

        tvAutoratio.text = "$autoratiotemp %"
        tvAuto1x.text = "1x"
        tvAuto3x.text = "3x"
        if (autoratiotemp==0) {
            tvAuto.text = "자동 매수를 사용하지 않습니다"
        }
        else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
            tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
        }
        else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
            tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
        }
        else {
            tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
        }


        seekbarAutoratio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                autoratiotemp = progress
                auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
                auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

                tvAutoratio.text = "$autoratiotemp %"
                if (autoratiotemp==0) {
                    tvAuto.text = "자동 매수를 사용하지 않습니다"
                }
                else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
                else {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                autoratiotemp = seekBar!!.progress
                auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
                auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

                tvAutoratio.text = "$autoratiotemp %"
                if (autoratiotemp==0) {
                    tvAuto.text = "자동 매수를 사용하지 않습니다"
                }
                else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
                else {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                autoratiotemp = seekBar!!.progress
                auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
                auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

                tvAutoratio.text = "$autoratiotemp %"
                if (autoratiotemp==0) {
                    tvAuto.text = "자동 매수를 사용하지 않습니다"
                }
                else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
                else {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
            }
        })


        seekbarAuto1x.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                auto1xtemp = progress
                auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
                auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

                tvAuto1x.text = auto1xtemp.toString()+" %"
                tvAuto3x.text = (100 - auto1xtemp).toString()+" %"
                if (autoratiotemp==0) {
                    tvAuto.text = "자동 매수를 사용하지 않습니다"
                }
                else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
                else {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                auto1xtemp = seekBar!!.progress
                auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
                auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

                tvAuto1x.text = auto1xtemp.toString()+" %"
                tvAuto3x.text = (100 - auto1xtemp).toString()+" %"
                if (autoratiotemp==0) {
                    tvAuto.text = "자동 매수를 사용하지 않습니다"
                }
                else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
                else {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                auto1xtemp = seekBar!!.progress
                auto1xratiotemp = autoratiotemp * auto1xtemp / 100F
                auto3xratiotemp = autoratiotemp * (100 - auto1xtemp) / 100F

                tvAuto1x.text = "1x"
                tvAuto3x.text = "3x"
                if (autoratiotemp==0) {
                    tvAuto.text = "자동 매수를 사용하지 않습니다"
                }
                else if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수"
                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    tvAuto.text = "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
                else {
                    tvAuto.text = "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수"
                }
            }
        })


        btnAutook.setOnClickListener {
            if (autoratiotemp > 0) {
                autobuy = true
                autoratio = autoratiotemp // 변경사항 전역변수에 저장
                auto1x = auto1xtemp // 변경사항 전역변수에 저장

                if ((auto1xratiotemp!=0F) && (auto3xratiotemp==0F)) {
                    Toast.makeText(dlg.context, "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x 자동 매수", Toast.LENGTH_SHORT).show()

                }
                else if ((auto1xratiotemp==0F) && (auto3xratiotemp!=0F)) {
                    Toast.makeText(dlg.context, "매월 투자금의 "+per.format(auto3xratiotemp)+" % 3x 자동 매수", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(dlg.context, "매월 투자금의 "+per.format(auto1xratiotemp)+" % 1x, "+per.format(auto3xratiotemp)+" % 3x 자동 매수", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                autobuy = false
                autoratiotemp = autoratio // 변경사항 전역변수에 저장
                auto1x = auto1xtemp // 변경사항 전역변수에 저장

                Toast.makeText(dlg.context, "자동 매수 기능을 사용하지 않습니다", Toast.LENGTH_SHORT).show()
            }
                dlg.dismiss()
                click = !click /////////////////////////////////////////////////////////////////////////
        }


        btnAutocancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        dlg.show()
    }
}