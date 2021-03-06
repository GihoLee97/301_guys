package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlin.math.roundToInt

class Dialog_asset(context: Context, monthlylast:Float, divlast:Float, taxlast:Float) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private var cashratio = 0F
    private var assettemp = asset
    private var cashtemp = cash
    private var initcashtemp = setCash
    private var divlatetemp = divlast
    private var divtottemp = dividendtot
    private var taxlatetemp = taxlast
    private var taxtottemp = taxtot
    private var comlatetemp = comlast
    private var comtottemp = tradecomtot


    private var monthlytemp = monthlylast

    private lateinit var pb_asset: ProgressBar
    private lateinit var tv_assetcash: TextView
    private lateinit var tv_assetetf: TextView
    private lateinit var tv_assetmonthlylate: TextView
    private lateinit var tv_assetmonthlytotal: TextView
    private lateinit var tv_assetdivlate: TextView
    private lateinit var tv_assetdivtotal: TextView
    private lateinit var tv_assettaxlate: TextView
    private lateinit var tv_assettaxtotal: TextView
    private lateinit var tv_assetcomlate: TextView
    private lateinit var tv_assetcomtotal: TextView
    private lateinit var btn_assetclose: Button

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_asset)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        pb_asset = dlg.findViewById(R.id.pb_asset)
        tv_assetcash = dlg.findViewById(R.id.tv_assetcash)
        tv_assetetf = dlg.findViewById(R.id.tv_assetetf)
        tv_assetmonthlylate = dlg.findViewById(R.id.tv_assetmonthlylate)
        tv_assetmonthlytotal = dlg.findViewById(R.id.tv_assetmonthlytotal)
        tv_assetdivlate = dlg.findViewById(R.id.tv_assetdivlate)
        tv_assetdivtotal = dlg.findViewById(R.id.tv_assetdivtotal)
        tv_assettaxlate = dlg.findViewById(R.id.tv_assettaxlate)
        tv_assettaxtotal = dlg.findViewById(R.id.tv_assettaxtotal)
        tv_assetcomlate = dlg.findViewById(R.id.tv_assetcomlate)
        tv_assetcomtotal = dlg.findViewById(R.id.tv_assetcomtotal)
        btn_assetclose = dlg.findViewById(R.id.btn_assetclose)

        cashratio = 100F * cashtemp / assettemp


        pb_asset.progress = cashratio.roundToInt()

        tv_assetcash.text = per.format(cashratio)+" %"
        tv_assetetf.text = per.format(100F - cashratio)+" %"

        tv_assetmonthlylate.text = "$ "+dec.format(monthlytemp)
        tv_assetmonthlytotal.text = "$ "+dec.format(input - initcashtemp)

        tv_assetdivlate.text = "$ "+dec.format(divlatetemp)
        tv_assetdivtotal.text = "$ "+dec.format(divtottemp)

        tv_assettaxlate.text = "$ "+dec.format(taxlatetemp)
        tv_assettaxtotal.text = "$ "+dec.format(taxtottemp)

        tv_assetcomlate.text = "$ "+dec.format(comlatetemp)
        tv_assetcomtotal.text = "$ "+dec.format(comtottemp)



        btn_assetclose.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////////
        }

        dlg.show()
    }
}