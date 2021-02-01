package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class Dialog_sell(context : Context) {
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var seekBar: SeekBar
    private lateinit var listenter: Dialog_sell.SellDialogClickedListener


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.sell_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        // 임시로 설정한 값
        val money = 1000000
        //
        val amount_sell: TextView = dlg.findViewById(R.id.매도금액_숫자)
        val tax : TextView = dlg.findViewById(R.id.수수료_숫자)
        var price: Float = 0F
        var fees: Float = 0F
        //text = dlg.findViewById(R.id.매수금액_숫자)
        seekBar = dlg.findViewById(R.id.seekbar_sell)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                amount_sell.text = "${Math.round(progress*0.01*money)/100f} 원"
                price = Math.round(progress*0.01*money)/100f
                // 수수료 0.1퍼센트
                tax.text="${Math.round(progress*0.01*money*0.001)/100f} 원"
                fees = Math.round(progress*0.01*money*0.001)/100f
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                amount_sell.text = "${Math.round(seekBar!!.progress *0.01*money)/100f} 원"
                tax.text = "${Math.round(seekBar!!.progress *0.01*money*0.001)/100f} 원"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                amount_sell.text = "${Math.round(seekBar!!.progress*0.01*money)/100f} 원"
                tax.text = "${Math.round(seekBar!!.progress*0.01*money*0.001)/100f} 원"
            }
        })

        btnOK = dlg.findViewById(R.id.okButton)
        btnOK.setOnClickListener {
            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            var result: List<Float> = listOf(price, fees)
            listenter.onBuyClicked(result)
            dlg.dismiss()
        }

        btnCancel = dlg.findViewById(R.id.cancelButton)
        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    fun setOnSellClickedListener(listener: (List<Float>)->Unit){
        this.listenter = object : SellDialogClickedListener{
            override fun onBuyClicked(content: List<Float>) {
                listener(content)
            }
        }
    }

    interface SellDialogClickedListener{
        fun onBuyClicked(content: List<Float>)
    }
}
