package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB
import kotlin.math.roundToInt

class Dialog_trade(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private var buyorsell: Int = 0 // 0: buy, 1: sell
    private var select: Int = 0 // 0, 1, 2, 3,

    private var price: Float = 0F
    private var temptradecom: Float = 0F
    private var quant: Int = 0

    //Roomdata관련
    private var gameNormalDb: GameNormalDB? = null

    private var limbuy1x: Int = 0
    private var limbuy3x: Int = 0
    private var limbuyinv1x: Int = 0
    private var limbuyinv3x: Int = 0

    // UI
    private lateinit var ll_tradeclose: LinearLayout
    private lateinit var ll_tradebefore: LinearLayout
    private lateinit var ll_tradeafter: LinearLayout // visibility 변경
    private lateinit var btn_buy: Button
    private lateinit var btn_sell: Button
    private lateinit var btn_trade1x: Button
    private lateinit var btn_trade3x: Button // 언락시 text 및 배경 변경
    private lateinit var btn_tradeinv1x: Button
    private lateinit var btn_tradeinv3x: Button // 언락시 text 및 배경 변경
    private lateinit var v_tradetomain: View
    private lateinit var tv_tradetitle: TextView
    private lateinit var tv_tradeprice: TextView
    private lateinit var btn_trademinus: Button
    private lateinit var tv_tradequant: TextView
    private lateinit var btn_tradeplus: Button
    private lateinit var np_ratio: NumberPicker
    private lateinit var btn_tradeok: Button


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   // 타이틀바 제거
        dlg.setContentView(R.layout.dialog_new_buyandsell)     // 다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        gameNormalDb = GameNormalDB.getInstace(dlg.context)

        // View 연결
        ll_tradeclose = dlg.findViewById(R.id.ll_tradeclose)
        ll_tradebefore = dlg.findViewById(R.id.ll_tradebefore)
        ll_tradeafter = dlg.findViewById(R.id.ll_tradeafter) // visibility 변경
        btn_buy = dlg.findViewById(R.id.btn_buy)
        btn_sell = dlg.findViewById(R.id.btn_sell)
        btn_trade1x = dlg.findViewById(R.id.btn_trade1x)
        btn_trade3x = dlg.findViewById(R.id.btn_trade3x)// 언락시 text 및 배경 변경
        btn_tradeinv1x = dlg.findViewById(R.id.btn_tradeinv1x)
        btn_tradeinv3x = dlg.findViewById(R.id.btn_tradeinv3x) // 언락시 text 및 배경 변경
        v_tradetomain = dlg.findViewById(R.id.v_tradetomain)
        tv_tradetitle = dlg.findViewById(R.id.tv_tradetitle)
        tv_tradeprice = dlg.findViewById(R.id.tv_tradeprice)
        btn_trademinus = dlg.findViewById(R.id.btn_trademinus)
        tv_tradequant = dlg.findViewById(R.id.tv_tradequant)
        btn_tradeplus = dlg.findViewById(R.id.btn_tradeplus)
        np_ratio = dlg.findViewById(R.id.np_ratio)
        btn_tradeok = dlg.findViewById(R.id.btn_tradeok)

        // 초기화
        buyorsell = 0
        btn_tradeok.isEnabled = false
        btn_tradeok.setBackgroundColor(Color.parseColor("#B7B1B3"))
        ll_tradebefore.visibility = VISIBLE
        ll_tradeafter.visibility = GONE

        // 매수 한계
        if ((cash / (price1x * tradecomrate)).roundToInt() > (cash / (price1x * tradecomrate))) {
            limbuy1x = (cash / (price1x * tradecomrate)).roundToInt() - 1
        } else {
            limbuy1x = (cash / (price1x * tradecomrate)).roundToInt()
        }

        if ((cash / (price3x * tradecomrate)).roundToInt() > (cash / (price3x * tradecomrate))) {
            limbuy3x = (cash / (price3x * tradecomrate)).roundToInt() - 1
        } else {
            limbuy3x = (cash / (price3x * tradecomrate)).roundToInt()
        }

        if ((cash / (priceinv1x * tradecomrate)).roundToInt() > (cash / (priceinv1x * tradecomrate))) {
            limbuyinv1x = (cash / (priceinv1x * tradecomrate)).roundToInt() - 1
        } else {
            limbuyinv1x = (cash / (priceinv1x * tradecomrate)).roundToInt()
        }

        if ((cash / (priceinv3x * tradecomrate)).roundToInt() > (cash / (priceinv3x * tradecomrate))) {
            limbuyinv3x = (cash / (priceinv3x * tradecomrate)).roundToInt() - 1
        } else {
            limbuyinv3x = (cash / (priceinv3x * tradecomrate)).roundToInt()
        }

        // TODO 레버리지 허용 여부 판별


        ll_tradeclose.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }

        // 매수 매도 선택
        btn_buy.setOnClickListener {
            buyorsell = 0
            btn_buy.setBackgroundResource(R.drawable.trade_buy)
            btn_buy.setTextAppearance(R.style.trade_choosen)
            btn_sell.setBackgroundResource(R.drawable.trade_unchoosen)
            btn_sell.setTextAppearance(R.style.trade_unchoosen)

            btn_trade1x.setTextAppearance(R.style.trade_optionbuy)
            btn_trade3x.setTextAppearance(R.style.trade_optionbuy)
            btn_tradeinv1x.setTextAppearance(R.style.trade_optionbuy)
            btn_tradeinv3x.setTextAppearance(R.style.trade_optionbuy)
        }
        btn_sell.setOnClickListener {
            buyorsell = 1
            btn_buy.setBackgroundResource(R.drawable.trade_unchoosen)
            btn_buy.setTextAppearance(R.style.trade_unchoosen)
            btn_sell.setBackgroundResource(R.drawable.trade_sell)
            btn_sell.setTextAppearance(R.style.trade_choosen)

            if (quant1x > 0) {
                btn_trade1x.setTextAppearance(R.style.trade_optionsell)
            } else {
                btn_trade1x.isEnabled = false
                btn_trade1x.setTextAppearance(R.style.trade_optionsellnone)
            }

            if (quant3x > 0) {
                btn_trade3x.setTextAppearance(R.style.trade_optionsell)
            } else {
                btn_trade3x.isEnabled = false
                btn_trade3x.setTextAppearance(R.style.trade_optionsellnone)
            }

            if (quantinv1x > 0) {
                btn_tradeinv1x.setTextAppearance(R.style.trade_optionsell)
            } else {
                btn_tradeinv1x.isEnabled = false
                btn_tradeinv1x.setTextAppearance(R.style.trade_optionsellnone)
            }

            if (quantinv3x > 0) {
                btn_tradeinv3x.setTextAppearance(R.style.trade_optionsell)
            } else {
                btn_tradeinv3x.isEnabled = false
                btn_tradeinv3x.setTextAppearance(R.style.trade_optionsellnone)
            }
        }

        // 거래할 ETF 선택
        btn_trade1x.setOnClickListener {
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 0
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100
            np_ratio.wrapSelectorWheel = false

            if (buyorsell==0) {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ "+dec.format(price1x)
                np_ratio.value = (100 * (price1x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ "+dec.format(price1x)
                np_ratio.value = (100 * quant / quant1x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_trade3x.setOnClickListener {
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 1
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100
            np_ratio.wrapSelectorWheel = false

            if (buyorsell==0) {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ "+dec.format(price3x)
                np_ratio.value = (100 * (price3x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ "+dec.format(price3x)
                np_ratio.value = (100 * quant / quant3x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_tradeinv1x.setOnClickListener {
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 2
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100
            np_ratio.wrapSelectorWheel = false

            if (buyorsell==0) {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ "+dec.format(priceinv1x)
                np_ratio.value = (100 * (priceinv1x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ "+dec.format(priceinv1x)
                np_ratio.value = (100 * quant / quantinv1x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_tradeinv3x.setOnClickListener {
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 3
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100
            np_ratio.wrapSelectorWheel = false

            if (buyorsell==0) {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ "+dec.format(priceinv3x)
                np_ratio.value = (100 * (priceinv3x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                v_tradetomain.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ "+dec.format(priceinv3x)
                np_ratio.value = (100 * quant / quantinv3x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_trademinus.setOnClickListener {
            when (select) {
                0 -> {
                    if (quant > 1 ) {
                        quant -= 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == 1) {
                        Toast.makeText(dlg.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                    }
                }
                1 -> {
                    if (quant > 1 ) {
                        quant -= 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == 1) {
                        Toast.makeText(dlg.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                    }
                }
                2 -> {
                    if (quant > 1 ) {
                        quant -= 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == 1) {
                        Toast.makeText(dlg.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                    }
                }
                3 -> {
                    if (quant > 1 ) {
                        quant -= 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == 1) {
                        Toast.makeText(dlg.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btn_tradeplus.setOnClickListener {
            when (select) {
                0 -> {
                    if ((quant < limbuy1x)) {
                        quant += 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == limbuy1x) {
                        Toast.makeText(dlg.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                    }
                }
                1 -> {
                    if ((quant < limbuy3x)) {
                        quant += 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == limbuy3x) {
                        Toast.makeText(dlg.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                    }
                }
                2 -> {
                    if ((quant < limbuyinv1x)) {
                        quant += 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == limbuyinv1x) {
                        Toast.makeText(dlg.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                    }
                }
                3 -> {
                    if ((quant < limbuyinv3x)) {
                        quant += 1
                        tv_tradequant.text = quant.toString()
                    } else if (quant == limbuyinv3x) {
                        Toast.makeText(dlg.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        np_ratio.setOnValueChangedListener{ numberpicker, oldVal: Int, newVal: Int ->
            when (buyorsell) {
                0 -> {

                }
            }
            quant = np_ratio.value
            tv_tradequant.text = quant.toString()
        }

        btnBuy1x.setOnClickListener {
            select = 1
            seekbarBuy!!.progress = 0
            btnBuy1x.setBackgroundColor(Color.parseColor(colorOn))
            btnBuy3x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOff))
        }
        btnBuy3x.setOnClickListener {
            if (!item4Active) {
                Toast.makeText(dlg.context, "아이템을 이용하여 레버리지 ETF 거래를 언락해야합니다", Toast.LENGTH_SHORT).show()
            }
            else {
                select = 2
                seekbarBuy!!.progress = 0
                btnBuy1x.setBackgroundColor(Color.parseColor(colorOff))
                btnBuy3x.setBackgroundColor(Color.parseColor(colorOn))
                btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
                btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOff))
            }
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
            if (!item4Active) {
                Toast.makeText(dlg.context, "아이템을 이용하여 레버리지 ETF 거래를 언락해야합니다", Toast.LENGTH_SHORT).show()
            }
            else {
            select = 4
            seekbarBuy!!.progress = 0
            btnBuy1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuy3x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv1x.setBackgroundColor(Color.parseColor(colorOff))
            btnBuyinv3x.setBackgroundColor(Color.parseColor(colorOn))
            }
        }
        btnplus.setOnClickListener {
            if (select == 1) {
                if(quant+1<= buylim1x) quant = quant+1
                price = price1x * quant
                temptradecom = price1x * (tradecomrate - 1F) * quant

                tvBuyquant.text = dec.format(quant)
                tvBuyprice.text = dec.format(price)+" 원"
                tvBuytradecom.text = dec.format(temptradecom)+" 원"

            } else if (select == 2) {
                if(quant+1<= buylim3x) quant = quant+1
                price = price3x * quant
                temptradecom = price3x * (tradecomrate - 1F) * quant

                tvBuyquant.text = dec.format(quant)
                tvBuyprice.text = dec.format(price)+" 원"
                tvBuytradecom.text = dec.format(temptradecom)+" 원"

            } else if (select == 3) {
                if(quant+1<= buyliminv1x) quant = quant+1
                price = priceinv1x * quant
                temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                tvBuyquant.text = dec.format(quant)
                tvBuyprice.text = dec.format(price)+" 원"
                tvBuytradecom.text = dec.format(temptradecom)+" 원"

            } else {
                if(quant+1<= buyliminv3x) quant = quant+1
                price = priceinv3x * quant
                temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                tvBuyquant.text = dec.format(quant)
                tvBuyprice.text = dec.format(price)+" 원"
                tvBuytradecom.text = dec.format(temptradecom)+" 원"

            }

        }
        btnminus.setOnClickListener {
            if(quant>0){
                if (select == 1) {
                    quant = quant-1
                    price = price1x * quant
                    temptradecom = price1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 2) {
                    quant = quant-1
                    price = price3x * quant
                    temptradecom = price3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else if (select == 3) {
                    quant = quant-1
                    price = priceinv1x * quant
                    temptradecom = priceinv1x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                } else {
                    quant = quant-1
                    price = priceinv3x * quant
                    temptradecom = priceinv3x * (tradecomrate - 1F) * quant

                    tvBuyquant.text = dec.format(quant)
                    tvBuyprice.text = dec.format(price)+" 원"
                    tvBuytradecom.text = dec.format(temptradecom)+" 원"

                }
            }
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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

                    tvBuyquant.text = dec.format(quant)
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
                    newGameNormalDB.setId = setId
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
