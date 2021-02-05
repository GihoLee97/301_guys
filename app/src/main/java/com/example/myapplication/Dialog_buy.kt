package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class Dialog_buy(context : Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var seekBar: SeekBar
    private lateinit var listenter: BuyDialogClickedListener
    private lateinit var btnmultionep : Button
    private lateinit var btnmultithreep : Button
    private lateinit var btnmultionem : Button
    private lateinit var btnmultithreem : Button
    private lateinit var txtdeposit: TextView
    private lateinit var txtquantity: TextView
    private lateinit var btnplus: Button
    private lateinit var btnminus: Button

    fun start(cash: Float, pricenow: Float) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_buy)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        val money:Float = cash
        //
        val amount_buy: TextView = dlg.findViewById(R.id.price)
        val tax : TextView = dlg.findViewById(R.id.fees)
        //text = dlg.findViewById(R.id.매수금액_숫자)
        var volume: Float = 0F
        var fees: Float = 0F
        var quantity: Int = 0
        seekBar = dlg.findViewById(R.id.seekbar_buy)
        btnOK = dlg.findViewById(R.id.okButton)
        btnCancel = dlg.findViewById(R.id.cancelButton)
        btnmultionep = dlg.findViewById(R.id.btn1)
        btnmultithreep = dlg.findViewById(R.id.btn2)
        btnmultionem = dlg.findViewById(R.id.btn3)
        btnmultithreem = dlg.findViewById(R.id.btn4)
        txtdeposit = dlg.findViewById(R.id.cash)
        txtquantity = dlg.findViewById(R.id.quantity)
        btnplus = dlg.findViewById(R.id.plus)
        btnminus = dlg.findViewById(R.id.minus)
        txtdeposit.text = cash.toString()+" 원"


        btnmultionep.setOnClickListener{
            //red #FFE77070
            //gray #FF808080
            btnmultionep.setBackgroundColor(Color.parseColor("#FFE77070"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF808080"))

        }
        btnmultithreep.setOnClickListener{
            //red
            btnmultionep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FFE77070"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF808080"))
        }
        btnmultionem.setOnClickListener{
            //red
            btnmultionep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FFE77070"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FF808080"))
        }
        btnmultithreem.setOnClickListener{
            //red
            btnmultionep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreep.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultionem.setBackgroundColor(Color.parseColor("#FF808080"))
            btnmultithreem.setBackgroundColor(Color.parseColor("#FFE77070"))
        }

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtquantity.text = "${Math.floor(progress*0.01*money/pricenow).toInt()} 주"
                quantity = Math.floor(progress*0.01*money/pricenow).toInt()
                amount_buy.text = "${Math.floor((quantity*pricenow).toDouble())} 원"
                volume = Math.floor((quantity*pricenow).toDouble()).toFloat()
                // 수수료 0.1퍼센트
                tax.text="${Math.floor(quantity*pricenow*0.001)} 원"
                fees = Math.floor(quantity*pricenow*0.001).toFloat()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                amount_buy.text = "${Math.floor(Math.floor(seekBar!!.progress *0.01*money/pricenow)*pricenow)} 원"
                tax.text = "${Math.floor(Math.floor(seekBar!!.progress *0.01*money/pricenow)*pricenow*0.001)} 원"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                amount_buy.text = "${Math.floor(Math.floor(seekBar!!.progress *0.01*money/pricenow)*pricenow)} 원"
                tax.text = "${Math.floor(Math.floor(seekBar!!.progress *0.01*money/pricenow)*pricenow*0.001)} 원"
            }
        })

        btnplus.setOnClickListener{
            quantity = quantity+1
            txtquantity.text = quantity.toString()+" 주"
            amount_buy.text = "${Math.floor((quantity*pricenow).toDouble())} 원"
            tax.text="${Math.floor(quantity*pricenow*0.001)} 원"
            volume = Math.floor((quantity*pricenow).toDouble()).toFloat()
            fees = Math.floor(quantity*pricenow*0.001).toFloat()
        }

        btnminus.setOnClickListener{
            if(quantity>0)  quantity = quantity-1
            txtquantity.text = quantity.toString()+" 주"
            amount_buy.text = "${Math.floor((quantity*pricenow).toDouble())} 원"
            tax.text="${Math.floor(quantity*pricenow*0.001)} 원"
            volume = Math.floor((quantity*pricenow).toDouble()).toFloat()
            fees = Math.floor(quantity*pricenow*0.001).toFloat()
        }

        btnOK.setOnClickListener {
            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            if(money>=(volume+fees)){
                var result: List<Float> = listOf(pricenow, quantity.toFloat(), fees)
                listenter.onBuyClicked(result)
                dlg.dismiss()
                click = !click /////////////////////////////////////////////////////////////////////
            }
            else{    Toast.makeText(dlg.context,"현금이 부족하여 매수할 수 없습니다.", Toast.LENGTH_LONG).show() }
        }

        btnCancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        dlg.show()
    }

    fun setOnBuyClickedListener(listener: (List<Float>)->Unit){
        this.listenter = object : BuyDialogClickedListener{
            override fun onBuyClicked(content: List<Float>) {
                listener(content)
            }
        }
    }

    interface BuyDialogClickedListener{
        fun onBuyClicked(content: List<Float>)
    }

}
