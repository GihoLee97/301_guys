package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlin.math.roundToInt

class Dialog_sell(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private var select: Int = 1 // 기본 선택상태 = 1x

    private var price: Float = 0F
    private var temptradecom: Float = 0F
    private var quant: Int = 0

    private lateinit var listenter: Dialog_sell.SellDialogClickedListener
    private lateinit var seekbarSell: SeekBar
    private lateinit var btnSelloK: Button
    private lateinit var btnSellcancel: Button
    private lateinit var btnSell1x: Button
    private lateinit var btnSell3x: Button
    private lateinit var btnSellinv1x: Button
    private lateinit var btnSellinv3x: Button
    private lateinit var tvSelleval: TextView
    private lateinit var tvSellhave: TextView
    private lateinit var tvSellquant: TextView
    private lateinit var tvSellprice: TextView
    private lateinit var tvSelltradecom: TextView
    private val colorOn: String = "#FF7070E7" // 버튼 선택시 색깔 Blue
    private val colorOff: String = "#FF808080" // 미선택 버튼 색깔 Gray


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   // 타이틀바 제거
        dlg.setContentView(R.layout.dialog_sell)     // 다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        seekbarSell = dlg.findViewById(R.id.seekbar_sell)
        btnSelloK = dlg.findViewById(R.id.btn_sellok)
        btnSellcancel = dlg.findViewById(R.id.btn_sellcancel)
        btnSell1x = dlg.findViewById(R.id.btn_sell1x)
        btnSell3x = dlg.findViewById(R.id.btn_sell3x)
        btnSellinv1x = dlg.findViewById(R.id.btn_sellinv1x)
        btnSellinv3x = dlg.findViewById(R.id.btn_sellinv3x)
        tvSelleval = dlg.findViewById(R.id.tv_selleval)
        tvSellhave = dlg.findViewById(R.id.tv_sellhave)
        tvSellquant = dlg.findViewById(R.id.tv_sellquant)
        tvSellprice= dlg.findViewById(R.id.tv_sellprice)
        tvSelltradecom = dlg.findViewById(R.id.tv_selltradecom)

        // 기본 선택상태 = 1x
        tvSelleval.text = dec.format(val1x)+" 원"
        tvSellhave.text = dec.format(quant1x)+" 원"
        btnSell1x.setBackgroundColor(Color.parseColor(colorOn))
        btnSell3x.setBackgroundColor(Color.parseColor(colorOff))
        btnSellinv1x.setBackgroundColor(Color.parseColor(colorOff))
        btnSellinv3x.setBackgroundColor(Color.parseColor(colorOff))


        btnSell1x.setOnClickListener {
            select = 1
            seekbarSell!!.progress = 0
            tvSelleval.text = dec.format(val1x)+" 원"
            tvSellhave.text = dec.format(quant1x)+" 원"
            btnSell1x.setBackgroundColor(Color.parseColor(colorOn))
            btnSell3x.setBackgroundColor(Color.parseColor(colorOff))
            btnSellinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnSellinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnSell3x.setOnClickListener {
            select = 2
            seekbarSell!!.progress = 0
            tvSelleval.text = dec.format(val3x)+" 원"
            tvSellhave.text = dec.format(quant3x)+" 원"
            btnSell1x.setBackgroundColor(Color.parseColor(colorOff))
            btnSell3x.setBackgroundColor(Color.parseColor(colorOn))
            btnSellinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnSellinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnSellinv1x.setOnClickListener {
            select = 3
            seekbarSell!!.progress = 0
            tvSelleval.text = dec.format(valinv1x)+" 원"
            tvSellhave.text = dec.format(quantinv1x)+" 원"
            btnSell1x.setBackgroundColor(Color.parseColor(colorOff))
            btnSell3x.setBackgroundColor(Color.parseColor(colorOff))
            btnSellinv1x.setBackgroundColor(Color.parseColor(colorOn))
            btnSellinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnSellinv3x.setOnClickListener {
            select = 4
            seekbarSell!!.progress = 0
            tvSelleval.text = dec.format(valinv3x)+" 원"
            tvSellhave.text = dec.format(quantinv3x)+" 원"
            btnSell1x.setBackgroundColor(Color.parseColor(colorOff))
            btnSell3x.setBackgroundColor(Color.parseColor(colorOff))
            btnSellinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnSellinv3x.setBackgroundColor(Color.parseColor(colorOn))
        }


        seekbarSell.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (select == 1) {
                    if (((progress * quant1x / 100F) - (progress * quant1x / 100F).roundToInt()) < 0F) {
                        quant = (progress * quant1x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * quant1x / 100F).roundToInt()
                    }

                    price = price1x * quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    if (((progress * quant3x / 100F) - (progress * quant3x / 100F).roundToInt()) < 0F) {
                        quant = (progress * quant3x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * quant3x / 100F).roundToInt()
                    }

                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 3) {
                    if (((progress * quantinv1x / 100F) - (progress * quantinv1x / 100F).roundToInt()) < 0F) {
                        quant = (progress * quantinv1x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * quantinv1x / 100F).roundToInt()
                    }

                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else {
                    if (((progress * quantinv3x / 100F) - (progress * quantinv3x / 100F).roundToInt()) < 0F) {
                        quant = (progress * quantinv3x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * quantinv3x / 100F).roundToInt()
                    }

                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (select == 1) {
                    if (((seekBar!!.progress * quant1x / 100F) - (seekBar!!.progress * quant1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quant1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quant1x / 100F).roundToInt()
                    }

                    price = price1x * quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    if (((seekBar!!.progress * quant3x / 100F) - (seekBar!!.progress * quant3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quant3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quant3x / 100F).roundToInt()
                    }

                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 3) {
                    if (((seekBar!!.progress * quantinv1x / 100F) - (seekBar!!.progress * quantinv1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quantinv1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quantinv1x / 100F).roundToInt()
                    }

                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else {
                    if (((seekBar!!.progress * quantinv3x / 100F) - (seekBar!!.progress * quantinv3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quantinv3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quantinv3x / 100F).roundToInt()
                    }

                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (select == 1) {
                    if (((seekBar!!.progress * quant1x / 100F) - (seekBar!!.progress * quant1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quant1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quant1x / 100F).roundToInt()
                    }

                    price = price1x * quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    if (((seekBar!!.progress * quant3x / 100F) - (seekBar!!.progress * quant3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quant3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quant3x / 100F).roundToInt()
                    }

                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"


                } else if (select == 3) {
                    if (((seekBar!!.progress * quantinv1x / 100F) - (seekBar!!.progress * quantinv1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quantinv1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quantinv1x / 100F).roundToInt()
                    }

                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"


                } else {
                    if (((seekBar!!.progress * quantinv3x / 100F) - (seekBar!!.progress * quantinv3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * quantinv3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * quantinv3x / 100F).roundToInt()
                    }

                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvSellquant.text = dec.format(quant)+" 주"
                    tvSellprice.text = dec.format(price)+" 원"
                    tvSelltradecom.text = dec.format(temptradecom)+" 원"

                }

            }
        })

        btnSelloK.setOnClickListener {
            if (quant >= 1) {
                cash += (price - temptradecom)// 현금에 매도금액 추가
                sold += price // 총 매도 금액 최신화
                tradecomtot += temptradecom // 총 수수로 최신화

                if (select == 1) {
                    if (price1x >= aver1x) {
                        profityear += (price1x - aver1x) * quant
                    }
                    quant1x -= quant
                    bought1x -= aver1x * quant
                    if (quant1x != 0) {
                        aver1x = bought1x / quant1x
                    }
                    else {
                        aver1x = 0F
                    }

                } else if (select == 2) {
                    if (price3x >= aver3x) {
                        profityear += (price3x - aver3x) * quant
                    }
                    quant3x -= quant
                    bought3x -= aver3x * quant
                    if (quant3x != 0) {
                        aver3x = bought3x / quant3x
                    }
                    else {
                        aver3x = 0F
                    }

                } else if (select == 3) {
                    if (priceinv1x >= averinv1x) {
                        profityear += (priceinv1x - averinv1x) * quant
                    }
                    quantinv1x -= quant
                    boughtinv1x -= averinv1x * quant
                    if (quantinv1x != 0) {
                        averinv1x = boughtinv1x / quantinv1x
                    }
                    else {
                        averinv1x = 0F
                    }

                } else {
                    if (priceinv3x >= averinv3x) {
                        profityear += (priceinv3x - averinv3x) * quant
                    }
                    quantinv3x -= quant
                    boughtinv3x -= averinv3x * quant

                    if (quantinv3x != 0) {
                        averinv3x = boughtinv3x / quantinv3x
                    }
                    else {
                        averinv3x = 0F
                    }
                }

                profittot += profityear // 순손익 최신화

                dlg.dismiss()
                click = !click /////////////////////////////////////////////////////////////////////
            } else {
                Toast.makeText(dlg.context, "매도 수량을 설정하세요", Toast.LENGTH_SHORT).show()
            }
        }



        btnSellcancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        dlg.show()
    }

    fun setOnSellClickedListener(listener: (List<Float>) -> Unit) {
        this.listenter = object : SellDialogClickedListener {
            override fun onSellClicked(content: List<Float>) {
                listener(content)
            }
        }
    }

    interface SellDialogClickedListener {
        fun onSellClicked(content: List<Float>)
    }
}
