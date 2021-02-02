package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class Dialog_sell(context : Context) {
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var seekBar: SeekBar
    private lateinit var listenter: Dialog_sell.SellDialogClickedListener
    private lateinit var btnmultionep : Button
    private lateinit var btnmultithreep : Button
    private lateinit var btnmultionem : Button
    private lateinit var btnmultithreem : Button


    fun start(evaluation: Float) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.sell_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        // 임시로 설정한 값
        val money:Float = evaluation
        //
        val amount_sell: TextView = dlg.findViewById(R.id.매도금액_숫자)
        val tax : TextView = dlg.findViewById(R.id.수수료_숫자)
        var price: Float = 0F
        var fees: Float = 0F
        //text = dlg.findViewById(R.id.매수금액_숫자)
        seekBar = dlg.findViewById(R.id.seekbar_sell)
        btnOK = dlg.findViewById(R.id.okButton)
        btnCancel = dlg.findViewById(R.id.cancelButton)
        btnmultionep = dlg.findViewById(R.id.btn1)
        btnmultithreep = dlg.findViewById(R.id.btn2)
        btnmultionem = dlg.findViewById(R.id.btn3)
        btnmultithreem = dlg.findViewById(R.id.btn4)


        btnmultionep.setOnClickListener{
            //  "blue" #FF7070E7
            //gray #FF808080
            btnmultionep.setBackgroundColor(Color.parseColor("#FF7070E7"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF808080"))

        }
        btnmultithreep.setOnClickListener{
            //red
            btnmultionep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF7070E7"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF808080"))
        }
        btnmultionem.setOnClickListener{
            //red
            btnmultionep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF7070E7"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF808080"))
        }
        btnmultithreem.setOnClickListener{
            //red
            btnmultionep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF7070E7"))
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                amount_sell.text = "${Math.round(progress*0.01*money)} 원"
                price = Math.round(progress*0.01*money).toFloat()
                // 수수료 0.1퍼센트
                tax.text="${Math.round(progress*0.01*money*0.001)} 원"
                fees = Math.round(progress*0.01*money*0.001).toFloat()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                amount_sell.text = "${Math.round(seekBar!!.progress *0.01*money)} 원"
                price=Math.round(seekBar!!.progress *0.01*money).toFloat()
                tax.text = "${Math.round(seekBar!!.progress *0.01*money*0.001)} 원"
                fees = Math.round(seekBar!!.progress *0.01*money*0.001).toFloat()
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                amount_sell.text = "${Math.round(seekBar!!.progress*0.01*money)} 원"
                price=Math.round(seekBar!!.progress *0.01*money).toFloat()
                tax.text = "${Math.round(seekBar!!.progress*0.01*money*0.001)} 원"
                fees = Math.round(seekBar!!.progress *0.01*money*0.001).toFloat()
            }
        })


        btnOK.setOnClickListener {
            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            if(money>=(price+fees)){
                var result: List<Float> = listOf(price, fees)
                listenter.onBuyClicked(result)
                dlg.dismiss()
                click = !click //////////////////////////////////////////////////////////////////////////
            }
            else{    Toast.makeText(dlg.context,"주식 보유량이 부족하여 매도할 수 없습니다.", Toast.LENGTH_LONG).show() }
        }

        btnCancel.setOnClickListener {
            dlg.dismiss()
            click = !click //////////////////////////////////////////////////////////////////////////
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
