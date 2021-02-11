package com._301_guys.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com._301_guys.stock_game.data.GameNormal
import com._301_guys.stock_game.data.GameNormalDB
import kotlin.math.roundToInt

class Dialog_buy(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private var select: Int = 1 // 기본 선택상태 = 1x

    private var price: Float = 0F
    private var temptradecom: Float = 0F
    private var quant: Int = 0

    //Roomdata관련
    private var gameNormalDb: GameNormalDB? = null

    private lateinit var listenter: BuyDialogClickedListener
    private lateinit var seekbarBuy: SeekBar
    private lateinit var btnBuyoK: Button
    private lateinit var btnBuycancel: Button
    private lateinit var btnBuy1x: Button
    private lateinit var btnBuy3x: Button
    private lateinit var btnBuyinv1x: Button
    private lateinit var btnBuyinv3x: Button
    private lateinit var tvBuycash: TextView
    private lateinit var tvBuyquant: TextView
    private lateinit var tvBuyprice: TextView
    private lateinit var tvBuytradecom: TextView
    private val colorOn: String = "#FFE77070" // 버튼 선택시 색깔 Red
    private val colorOff: String = "#FF808080" // 미선택 버튼 색깔 Gray


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   // 타이틀바 제거
        dlg.setContentView(R.layout.dialog_buy)     // 다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        gameNormalDb = GameNormalDB.getInstace(dlg.context)
        seekbarBuy = dlg.findViewById(R.id.seekbar_buy)
        btnBuyoK = dlg.findViewById(R.id.btn_buyok)
        btnBuycancel = dlg.findViewById(R.id.btn_buycancel)
        btnBuy1x = dlg.findViewById(R.id.btn_buy1x)
        btnBuy3x = dlg.findViewById(R.id.btn_buy3x)
        btnBuyinv1x = dlg.findViewById(R.id.btn_buyinv1x)
        btnBuyinv3x = dlg.findViewById(R.id.btn_buyinv3x)
        tvBuycash = dlg.findViewById(R.id.tv_buycash)
        tvBuyquant = dlg.findViewById(R.id.tv_buyquant)
        tvBuyprice= dlg.findViewById(R.id.tv_buyprice)
        tvBuytradecom = dlg.findViewById(R.id.tv_buytradecom)


        // 기본 선택상태 = 1x
        tvBuycash.text = dec.format(cash)+" 원"
        btnBuy1x.setBackgroundColor(Color.parseColor(colorOn))
        btnBuy3x.setBackgroundColor(Color.parseColor(colorOff))
        btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
        btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOff))


        btnBuy1x.setOnClickListener {
            select = 1
            seekbarBuy!!.progress = 0
            btnBuy1x.setBackgroundColor(Color.parseColor(colorOn))
            btnBuy3x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnBuy3x.setOnClickListener {
            select = 2
            seekbarBuy!!.progress = 0
            btnBuy1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuy3x.setBackgroundColor(Color.parseColor(colorOn))
            btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnBuyinv1x.setOnClickListener {
            select = 3
            seekbarBuy!!.progress = 0
            btnBuy1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuy3x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOn))
            btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnBuyinv3x.setOnClickListener {
            select = 4
            seekbarBuy!!.progress = 0
            btnBuy1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuy3x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOn))
        }


        seekbarBuy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (select == 1) {
                    if (((progress * buylim1x / 100F) - (progress * buylim1x / 100F).roundToInt()) < 0F) {
                        quant = (progress * buylim1x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * buylim1x / 100F).roundToInt()
                    }

                    price = price1x * quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    if (((progress * buylim3x / 100F) - (progress * buylim3x / 100F).roundToInt()) < 0F) {
                        quant = (progress * buylim3x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * buylim3x / 100F).roundToInt()
                    }

                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 3) {
                    if (((progress * buyliminv1x / 100F) - (progress * buyliminv1x / 100F).roundToInt()) < 0F) {
                        quant = (progress * buyliminv1x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * buyliminv1x / 100F).roundToInt()
                    }

                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else {
                    if (((progress * buyliminv3x / 100F) - (progress * buyliminv3x / 100F).roundToInt()) < 0F) {
                        quant = (progress * buyliminv3x / 100F).roundToInt() - 1
                    } else {
                        quant = (progress * buyliminv3x / 100F).roundToInt()
                    }

                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (select == 1) {
                    if (((seekBar!!.progress * buylim1x / 100F) - (seekBar!!.progress * buylim1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buylim1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buylim1x / 100F).roundToInt()
                    }

                    price = price1x * quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    if (((seekBar!!.progress * buylim3x / 100F) - (seekBar!!.progress * buylim3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buylim3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buylim3x / 100F).roundToInt()
                    }

                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 3) {
                    if (((seekBar!!.progress * buyliminv1x / 100F) - (seekBar!!.progress * buyliminv1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buyliminv1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buyliminv1x / 100F).roundToInt()
                    }

                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else {
                    if (((seekBar!!.progress * buyliminv3x / 100F) - (seekBar!!.progress * buyliminv3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buyliminv3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buyliminv3x / 100F).roundToInt()
                    }

                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (select == 1) {
                    if (((seekBar!!.progress * buylim1x / 100F) - (seekBar!!.progress * buylim1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buylim1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buylim1x / 100F).roundToInt()
                    }

                    price = price1x* quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    if (((seekBar!!.progress * buylim3x / 100F) - (seekBar!!.progress * buylim3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buylim3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buylim3x / 100F).roundToInt()
                    }

                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"


                } else if (select == 3) {
                    if (((seekBar!!.progress * buyliminv1x / 100F) - (seekBar!!.progress * buyliminv1x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buyliminv1x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buyliminv1x / 100F).roundToInt()
                    }

                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"


                } else {
                    if (((seekBar!!.progress * buyliminv3x / 100F) - (seekBar!!.progress * buyliminv3x / 100F).roundToInt()) < 0F) {
                        quant = (seekBar!!.progress * buyliminv3x / 100F).roundToInt() - 1
                    } else {
                        quant = (seekBar!!.progress * buyliminv3x / 100F).roundToInt()
                    }

                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)+" 주"
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                }

            }
        })

        btnBuyoK.setOnClickListener {
            if (quant >= 1) {
                cash -= (price + temptradecom) // 현금에서 매수금액 차감
                bought += price // 총 매수 금액 최신화
                tradecomtot += temptradecom // 총 수수로 최신화

                if (select == 1) {
                    quant1x += quant
                    bought1x += price1x * quant
                    aver1x = bought1x / quant1x
                } else if (select == 2) {
                    quant3x += quant
                    bought3x += price3x * quant
                    aver3x = bought3x / quant3x
                } else if (select == 3) {
                    quantinv1x += quant
                    boughtinv1x += priceinv1x * quant
                    averinv1x = boughtinv1x / quantinv1x
                } else {
                    quantinv3x += quant
                    boughtinv3x += priceinv3x * quant
                    averinv3x = boughtinv3x / quantinv3x
                }

                val addRunnable = Runnable {
                    val newGameNormalDB = GameNormal()
                    newGameNormalDB.id = localdatatime
                    newGameNormalDB.buyorsell = "매수"
                    newGameNormalDB.select = select
                    newGameNormalDB.price = price/quant
                    newGameNormalDB.volume = price
                    newGameNormalDB.quant = quant
                    newGameNormalDB.tradecom = temptradecom
                    newGameNormalDB.cash = cash
                    gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()


                dlg.dismiss()
                click = !click /////////////////////////////////////////////////////////////////////
            } else {
                Toast.makeText(dlg.context, "매수 수량을 설정하세요", Toast.LENGTH_SHORT).show()
            }
        }

        btnBuycancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        dlg.show()
    }

    fun setOnBuyClickedListener(listener: (List<Float>) -> Unit) {
        this.listenter = object : BuyDialogClickedListener {
            override fun onBuyClicked(content: List<Float>) {
                listener(content)
            }
        }
    }

    interface BuyDialogClickedListener {
        fun onBuyClicked(content: List<Float>)
    }
}
