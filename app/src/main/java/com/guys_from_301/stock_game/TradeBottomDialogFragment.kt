package com.guys_from_301.stock_game

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB
import kotlin.math.roundToInt

class TradeBottomDialogFragment(context: Context) : BottomSheetDialogFragment() {
    var mContext: Context? = context
    private var buyOrsell: Int = 0 // 0: buy, 1: sell
    private var select: Int = 0 // 0, 1, 2, 3,
    private var depth: Int = 0 // 0, 1

    private var quant: Int = 0
    private var price: Float = 0F
    private var temptradecom: Float = 0F

    //Roomdata관련
    private var gameNormalDb: GameNormalDB? = null

    // 매수한계 수량
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
    private lateinit var cl_tradetomain: ConstraintLayout
    private lateinit var v_left: View
    private lateinit var tv_tradetitle: TextView
    private lateinit var tv_tradeprice: TextView
    private lateinit var btn_trademinus: Button
    private lateinit var tv_tradequant: TextView
    private lateinit var btn_tradeplus: Button
    private lateinit var np_ratio: NumberPicker
    private lateinit var btn_tradeok: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.bottom_dialog_fragment_trade, container, false)

        gameNormalDb = GameNormalDB.getInstace(view.context)

        // View 연결
        ll_tradeclose = view.findViewById(R.id.ll_tradeclose)!!
        ll_tradebefore = view.findViewById(R.id.ll_tradebefore)!!
        ll_tradeafter = view.findViewById(R.id.ll_tradeafter)!! // visibility 변경
        btn_buy = view.findViewById(R.id.btn_buy)!!
        btn_sell = view.findViewById(R.id.btn_sell)!!
        btn_trade1x = view.findViewById(R.id.btn_trade1x)!!
        btn_trade3x = view.findViewById(R.id.btn_trade3x)!!// 언락시 text 및 배경 변경
        btn_tradeinv1x = view.findViewById(R.id.btn_tradeinv1x)!!
        btn_tradeinv3x = view.findViewById(R.id.btn_tradeinv3x)!! // 언락시 text 및 배경 변경
        cl_tradetomain = view.findViewById(R.id.cl_tradetomain)!!
        v_left = view.findViewById(R.id.v_left)!!
        tv_tradetitle = view.findViewById(R.id.tv_tradetitle)!!
        tv_tradeprice = view.findViewById(R.id.tv_tradeprice)!!
        btn_trademinus = view.findViewById(R.id.btn_trademinus)!!
        tv_tradequant = view.findViewById(R.id.tv_tradequant)!!
        btn_tradeplus = view.findViewById(R.id.btn_tradeplus)!!
        np_ratio = view.findViewById(R.id.np_ratio)!!
        btn_tradeok = view.findViewById(R.id.btn_tradeok)!!

        // 초기화
        buyOrsell = 0
        btn_tradeok.isEnabled = false
        btn_tradeok.setBackgroundColor(Color.parseColor("#B7B1B3"))
        ll_tradebefore.visibility = VISIBLE
        ll_tradeafter.visibility = GONE

        if (item4Active) {
            btn_trade3x.isEnabled = true
            btn_trade3x.text = "x3\n레버리지"
            btn_trade3x.setTextAppearance(R.style.trade_optionbuy)
            btn_trade3x.setBackgroundResource(R.drawable.trade_option)

            btn_tradeinv3x.isEnabled = true
            btn_tradeinv3x.text = "x3\n인버스"
            btn_tradeinv3x.setTextAppearance(R.style.trade_optionbuy)
            btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option)
        } else {
            btn_trade3x.isEnabled = false
            btn_trade3x.text = "잠김"
            btn_trade3x.setTextAppearance(R.style.trade_lock)
            btn_trade3x.setBackgroundResource(R.drawable.trade_option_lock)

            btn_tradeinv3x.isEnabled = false
            btn_tradeinv3x.text = "잠김"
            btn_tradeinv3x.setTextAppearance(R.style.trade_lock)
            btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option_lock)
        }

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


        ll_tradeclose.setOnClickListener {
            dismiss()
        }

        cl_tradetomain.setOnClickListener {
            when (buyOrsell) {
                0 -> {
                    ll_tradebefore.visibility = VISIBLE
                    ll_tradeafter.visibility = GONE
                    btn_tradeok.isEnabled = false
                    btn_tradeok.setBackgroundColor(Color.parseColor("#B7B1B3"))
                    buyOrsell = 0
                    btn_buy.setBackgroundResource(R.drawable.trade_buy)
                    btn_buy.setTextAppearance(R.style.trade_choosen)
                    btn_sell.setBackgroundResource(R.drawable.trade_unchoosen)
                    btn_sell.setTextAppearance(R.style.trade_unchoosen)

                    btn_trade1x.isEnabled = true
                    btn_trade1x.setTextAppearance(R.style.trade_optionbuy)

                    btn_tradeinv1x.isEnabled = true
                    btn_tradeinv1x.setTextAppearance(R.style.trade_optionbuy)

                    if (item4Active) {
                        btn_trade3x.isEnabled = true
                        btn_trade3x.text = "x3\n레버리지"
                        btn_trade3x.setTextAppearance(R.style.trade_optionbuy)
                        btn_trade3x.setBackgroundResource(R.drawable.trade_option)

                        btn_tradeinv3x.isEnabled = true
                        btn_tradeinv3x.text = "x3\n인버스"
                        btn_tradeinv3x.setTextAppearance(R.style.trade_optionbuy)
                        btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option)
                    } else {
                        btn_trade3x.isEnabled = false
                        btn_trade3x.text = "잠김"
                        btn_trade3x.setTextAppearance(R.style.trade_lock)
                        btn_trade3x.setBackgroundResource(R.drawable.trade_option_lock)

                        btn_tradeinv3x.isEnabled = false
                        btn_tradeinv3x.text = "잠김"
                        btn_tradeinv3x.setTextAppearance(R.style.trade_lock)
                        btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option_lock)
                    }
                    depth = 0
                }
                1 -> {
                    ll_tradebefore.visibility = VISIBLE
                    ll_tradeafter.visibility = GONE
                    btn_tradeok.isEnabled = false
                    btn_tradeok.setBackgroundColor(Color.parseColor("#B7B1B3"))
                    buyOrsell = 1
                    btn_buy.setBackgroundResource(R.drawable.trade_unchoosen)
                    btn_buy.setTextAppearance(R.style.trade_unchoosen)
                    btn_sell.setBackgroundResource(R.drawable.trade_sell)
                    btn_sell.setTextAppearance(R.style.trade_choosen)

                    if (quant1x > 0) {
                        btn_trade1x.isEnabled = true
                        btn_trade1x.setTextAppearance(R.style.trade_optionsell)
                    } else {
                        btn_trade1x.isEnabled = false
                        btn_trade1x.setTextAppearance(R.style.trade_optionsellnone)
                    }

                    if ((quant3x > 0) && item4Active) {
                        btn_trade3x.isEnabled = true
                        btn_trade3x.text = "x3\n레버리지"
                        btn_trade3x.setTextAppearance(R.style.trade_optionsell)
                        btn_trade3x.setBackgroundResource(R.drawable.trade_option)
                    } else if (quant3x == 0 && item4Active) {
                        btn_trade3x.isEnabled = false
                        btn_trade3x.setTextAppearance(R.style.trade_optionsellnone)
                    } else {
                        btn_trade3x.isEnabled = false
                        btn_trade3x.text = "잠김"
                        btn_trade3x.setTextAppearance(R.style.trade_lock)
                        btn_trade3x.setBackgroundResource(R.drawable.trade_option_lock)
                    }

                    if (quantinv1x > 0) {
                        btn_tradeinv1x.setTextAppearance(R.style.trade_optionsell)
                    } else {
                        btn_tradeinv1x.isEnabled = false
                        btn_tradeinv1x.setTextAppearance(R.style.trade_optionsellnone)
                    }

                    if ((quantinv3x > 0) && item4Active) {
                        btn_tradeinv3x.isEnabled = true
                        btn_tradeinv3x.text = "x3\n인버스"
                        btn_tradeinv3x.setTextAppearance(R.style.trade_optionsell)
                        btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option)
                    } else if ((quantinv3x == 0) && item4Active) {
                        btn_tradeinv3x.isEnabled = false
                        btn_tradeinv3x.setTextAppearance(R.style.trade_optionsellnone)
                    } else {
                        btn_tradeinv3x.isEnabled = false
                        btn_tradeinv3x.text = "잠김"
                        btn_tradeinv3x.setTextAppearance(R.style.trade_lock)
                        btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option_lock)
                    }
                    depth = 0
                }
            }
        }

        // 매수 매도 선택
        btn_buy.setOnClickListener {
            buyOrsell = 0
            btn_buy.setBackgroundResource(R.drawable.trade_buy)
            btn_buy.setTextAppearance(R.style.trade_choosen)
            btn_sell.setBackgroundResource(R.drawable.trade_unchoosen)
            btn_sell.setTextAppearance(R.style.trade_unchoosen)

            btn_trade1x.isEnabled = true
            btn_trade1x.setTextAppearance(R.style.trade_optionbuy)

            btn_tradeinv1x.isEnabled = true
            btn_tradeinv1x.setTextAppearance(R.style.trade_optionbuy)

            if (item4Active) {
                btn_trade3x.isEnabled = true
                btn_trade3x.text = "x3\n레버리지"
                btn_trade3x.setTextAppearance(R.style.trade_optionbuy)
                btn_trade3x.setBackgroundResource(R.drawable.trade_option)

                btn_tradeinv3x.isEnabled = true
                btn_tradeinv3x.text = "x3\n인버스"
                btn_tradeinv3x.setTextAppearance(R.style.trade_optionbuy)
                btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option)
            } else {
                btn_trade3x.isEnabled = false
                btn_trade3x.text = "잠김"
                btn_trade3x.setTextAppearance(R.style.trade_lock)
                btn_trade3x.setBackgroundResource(R.drawable.trade_option_lock)

                btn_tradeinv3x.isEnabled = false
                btn_tradeinv3x.text = "잠김"
                btn_tradeinv3x.setTextAppearance(R.style.trade_lock)
                btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option_lock)
            }
        }
        btn_sell.setOnClickListener {
            buyOrsell = 1
            btn_buy.setBackgroundResource(R.drawable.trade_unchoosen)
            btn_buy.setTextAppearance(R.style.trade_unchoosen)
            btn_sell.setBackgroundResource(R.drawable.trade_sell)
            btn_sell.setTextAppearance(R.style.trade_choosen)

            if (quant1x > 0) {
                btn_trade1x.isEnabled = true
                btn_trade1x.setTextAppearance(R.style.trade_optionsell)
            } else {
                btn_trade1x.isEnabled = false
                btn_trade1x.setTextAppearance(R.style.trade_optionsellnone)
            }

            if ((quant3x > 0) && item4Active) {
                btn_trade3x.isEnabled = true
                btn_trade3x.text = "x3\n레버리지"
                btn_trade3x.setTextAppearance(R.style.trade_optionsell)
                btn_trade3x.setBackgroundResource(R.drawable.trade_option)
            } else if (quant3x == 0 && item4Active) {
                btn_trade3x.isEnabled = false
                btn_trade3x.setTextAppearance(R.style.trade_optionsellnone)
            } else {
                btn_trade3x.isEnabled = false
                btn_trade3x.text = "잠김"
                btn_trade3x.setTextAppearance(R.style.trade_lock)
                btn_trade3x.setBackgroundResource(R.drawable.trade_option_lock)
            }

            if (quantinv1x > 0) {
                btn_tradeinv1x.setTextAppearance(R.style.trade_optionsell)
            } else {
                btn_tradeinv1x.isEnabled = false
                btn_tradeinv1x.setTextAppearance(R.style.trade_optionsellnone)
            }

            if ((quantinv3x > 0) && item4Active) {
                btn_tradeinv3x.isEnabled = true
                btn_tradeinv3x.text = "x3\n인버스"
                btn_tradeinv3x.setTextAppearance(R.style.trade_optionsell)
                btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option)
            } else if ((quantinv3x == 0) && item4Active) {
                btn_tradeinv3x.isEnabled = false
                btn_tradeinv3x.setTextAppearance(R.style.trade_optionsellnone)
            } else {
                btn_tradeinv3x.isEnabled = false
                btn_tradeinv3x.text = "잠김"
                btn_tradeinv3x.setTextAppearance(R.style.trade_lock)
                btn_tradeinv3x.setBackgroundResource(R.drawable.trade_option_lock)
            }
        }

        // 거래할 ETF 선택
        btn_trade1x.setOnClickListener {
            depth = 1
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 0
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100

            if (buyOrsell == 0) {
                tv_tradetitle.text = "x1 일반 매수하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ " + dec.format(price1x)
                np_ratio.value = (100 * (price1x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                tv_tradetitle.text = "x1 일반 매도하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ " + dec.format(price1x)
                np_ratio.value = (100 * quant / quant1x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_trade3x.setOnClickListener {
            depth = 1
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 1
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100

            if (buyOrsell == 0) {
                tv_tradetitle.text = "x3 레버리지 매수하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ " + dec.format(price3x)
                np_ratio.value = (100 * (price3x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                tv_tradetitle.text = "x3 레버리지 매도하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ " + dec.format(price3x)
                np_ratio.value = (100 * quant / quant3x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_tradeinv1x.setOnClickListener {
            depth = 1
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 2
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100

            if (buyOrsell == 0) {
                tv_tradetitle.text = "x1 인버스 매수하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ " + dec.format(priceinv1x)
                np_ratio.value = (100 * (priceinv1x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                tv_tradetitle.text = "x1 인버스 매도하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ " + dec.format(priceinv1x)
                np_ratio.value = (100 * quant / quantinv1x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_tradeinv3x.setOnClickListener {
            depth = 1
            ll_tradebefore.visibility = GONE
            ll_tradeafter.visibility = VISIBLE
            select = 3
            quant = 1
            tv_tradequant.text = quant.toString()
            np_ratio.minValue = 1
            np_ratio.maxValue = 100

            if (buyOrsell == 0) {
                tv_tradetitle.text = "x3 인버스 매수하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_orange)
                tv_tradetitle.setTextAppearance(R.style.trade_titlebuy)
                tv_tradeprice.text = "$ " + dec.format(priceinv3x)
                np_ratio.value = (100 * (priceinv3x * quant) / cash).roundToInt()
                btn_tradeok.setBackgroundColor(Color.parseColor("#F4730B"))
            } else {
                tv_tradetitle.text = "x3 인버스 매도하기"
                v_left.setBackgroundResource(R.drawable.ic_path_left_blue)
                tv_tradetitle.setTextAppearance(R.style.trade_titlesell)
                tv_tradeprice.text = "$ " + dec.format(priceinv3x)
                np_ratio.value = (100 * quant / quantinv3x)
                btn_tradeok.setBackgroundColor(Color.parseColor("#3B80FF"))
            }
            btn_tradeok.isEnabled = true
        }

        btn_trademinus.setOnClickListener {
            when (buyOrsell) {
                0 -> {
                    when (select) {
                        0 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price1x)
                                np_ratio.value = (100 * (quant * price1x) / cash).roundToInt()
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        1 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price3x)
                                np_ratio.value = (100 * (quant * price3x) / cash).roundToInt()
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        2 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv1x)
                                np_ratio.value = (100 * (quant * priceinv1x) / cash).roundToInt()
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        3 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv3x)
                                np_ratio.value = (100 * (quant * priceinv3x) / cash).roundToInt()
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매수 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                1 -> {
                    when (select) {
                        0 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price1x)
                                np_ratio.value = 100 * quant / quant1x
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매도 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        1 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price3x)
                                np_ratio.value = 100 * quant / quant3x
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매도 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        2 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv1x)
                                np_ratio.value = 100 * quant / quantinv1x
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매도 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        3 -> {
                            if (quant > 1) {
                                quant -= 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv3x)
                                np_ratio.value = 100 * quant / quantinv3x
                            } else if (quant == 1) {
                                Toast.makeText(view.context, "매도 수량은 1주 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        btn_tradeplus.setOnClickListener {
            when (buyOrsell) {
                0 -> {
                    when (select) {
                        0 -> {
                            if ((quant < limbuy1x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price1x)
                                np_ratio.value = (100 * (quant * price1x) / cash).roundToInt()
                            } else if (quant == limbuy1x) {
                                Toast.makeText(view.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        1 -> {
                            if ((quant < limbuy3x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price3x)
                                np_ratio.value = (100 * (quant * price3x) / cash).roundToInt()
                            } else if (quant == limbuy3x) {
                                Toast.makeText(view.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        2 -> {
                            if ((quant < limbuyinv1x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv1x)
                                np_ratio.value = (100 * (quant * priceinv1x) / cash).roundToInt()
                            } else if (quant == limbuyinv1x) {
                                Toast.makeText(view.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        3 -> {
                            if ((quant < limbuyinv3x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv3x)
                                np_ratio.value = (100 * (quant * priceinv3x) / cash).roundToInt()
                            } else if (quant == limbuyinv3x) {
                                Toast.makeText(view.context, "매수할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                1 -> {
                    when (select) {
                        0 -> {
                            if ((quant < quant1x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price1x)
                                np_ratio.value = 100 * quant / quant1x
                            } else if (quant == quant1x) {
                                Toast.makeText(view.context, "매도할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        1 -> {
                            if ((quant < quant3x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * price3x)
                                np_ratio.value = 100 * quant / quant3x
                            } else if (quant == quant3x) {
                                Toast.makeText(view.context, "매도할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        2 -> {
                            if ((quant < quantinv1x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv1x)
                                np_ratio.value = 100 * quant / quantinv1x
                            } else if (quant == quantinv1x) {
                                Toast.makeText(view.context, "매도할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                        3 -> {
                            if ((quant < quantinv3x)) {
                                quant += 1
                                tv_tradequant.text = quant.toString()
                                tv_tradeprice.text = "$ " + dec.format(quant * priceinv3x)
                                np_ratio.value = 100 * quant / quantinv3x
                            } else if (quant == quantinv3x) {
                                Toast.makeText(view.context, "매도할 수 있는 최대 수량입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        np_ratio.setOnValueChangedListener { numberpicker, oldVal: Int, newVal: Int ->
            when (buyOrsell) {
                0 -> {
                    when (select) {
                        0 -> {
                            if (((newVal / 100F * limbuy1x).roundToInt() - (newVal / 100F * limbuy1x)) > 0) {
                                quant = (newVal / 100F * limbuy1x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * limbuy1x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * price1x)
                        }
                        1 -> {
                            if (((newVal / 100F * limbuy3x).roundToInt() - (newVal / 100F * limbuy3x)) > 0) {
                                quant = (newVal / 100F * limbuy3x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * limbuy3x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * price3x)
                        }
                        2 -> {
                            if (((newVal / 100F * limbuyinv1x).roundToInt() - (newVal / 100F * limbuyinv1x)) > 0) {
                                quant = (newVal / 100F * limbuyinv1x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * limbuyinv1x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * priceinv1x)
                        }
                        3 -> {
                            if (((newVal / 100F * limbuyinv3x).roundToInt() - (newVal / 100F * limbuyinv3x)) > 0) {
                                quant = (newVal / 100F * limbuyinv3x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * limbuyinv3x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * priceinv3x)
                        }
                    }
                }
                1 -> {
                    when (select) {
                        0 -> {
                            if (((newVal / 100F * quant1x).roundToInt()-(newVal / 100F * quant1x)>0)) {
                                quant = (newVal / 100F * quant1x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * quant1x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * price1x)
                        }
                        1 -> {
                            if (((newVal / 100F * quant3x).roundToInt()-(newVal / 100F * quant3x)>0)) {
                                quant = (newVal / 100F * quant3x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * quant3x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * price3x)
                        }
                        2 -> {
                            if (((newVal / 100F * quantinv1x).roundToInt()-(newVal / 100F * quantinv1x)>0)) {
                                quant = (newVal / 100F * quantinv1x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * quantinv1x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * priceinv1x)
                        }
                        3 -> {
                            if (((newVal / 100F * quantinv3x).roundToInt()-(newVal / 100F * quantinv3x)>0)) {
                                quant = (newVal / 100F * quantinv3x).roundToInt() - 1
                                if (quant==0) {
                                    quant = 1
                                }
                            } else {
                                quant = (newVal / 100F * quantinv3x).roundToInt()
                                if (quant==0) {
                                    quant = 1
                                }
                            }
                            tv_tradequant.text = quant.toString()
                            tv_tradeprice.text = "$ " + dec.format(quant * priceinv3x)
                        }
                    }
                }
            }
        }

        btn_tradeok.setOnClickListener {
            when (buyOrsell) {
                0 -> {
                    when (select) {
                        0 -> {
                            cash -= quant * price1x * tradecomrate
                            bought += quant * price1x
                            tradecomtot += quant * price1x * (tradecomrate - 1F)
                            quant1x += quant
                            bought1x += price1x * quant
                            aver1x = bought1x / quant1x

                            price = quant * price1x
                            temptradecom = quant * price1x * (tradecomrate - 1F)
                        }
                        1 -> {
                            cash -= quant * price3x * tradecomrate
                            bought += quant * price3x
                            tradecomtot += quant * price3x * (tradecomrate - 1F)
                            quant3x += quant
                            bought3x += price3x * quant
                            aver3x = bought3x / quant3x

                            price = quant * price3x
                            temptradecom = quant * price3x * (tradecomrate - 1F)
                        }
                        2 -> {
                            cash -= quant * priceinv1x * tradecomrate
                            bought += quant * priceinv1x
                            tradecomtot += quant * priceinv1x * (tradecomrate - 1F)
                            quantinv1x += quant
                            boughtinv1x += priceinv1x * quant
                            averinv1x = boughtinv1x / quantinv1x

                            price = quant * priceinv1x
                            temptradecom = quant * priceinv1x * (tradecomrate - 1F)
                        }
                        3 -> {
                            cash -= quant * priceinv3x * tradecomrate
                            bought += quant * priceinv3x
                            tradecomtot += quant * priceinv3x * (tradecomrate - 1F)
                            quantinv3x += quant
                            boughtinv3x += priceinv3x * quant
                            averinv3x = boughtinv3x / quantinv3x

                            price = quant * priceinv3x
                            temptradecom = quant * priceinv3x * (tradecomrate - 1F)
                        }
                    }

                    val addRunnable = Runnable {
                        val newGameNormalDB = GameNormal()
                        newGameNormalDB.id = localdatatime
                        newGameNormalDB.buyorsell = "매수"
                        newGameNormalDB.select = select
                        newGameNormalDB.price = price / quant
                        newGameNormalDB.volume = price
                        newGameNormalDB.quant = quant
                        newGameNormalDB.tradecom = temptradecom
                        newGameNormalDB.cash = cash
                        newGameNormalDB.setId = setId
                        gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                    }
                    val addThread = Thread(addRunnable)
                    addThread.start()
                }
                1 -> {
                    when (select) {
                        0 -> {
                            cash += quant * price1x * (2F - tradecomrate)// 현금에 매도금액 추가
                            sold += quant * price1x // 총 매도 금액 최신화
                            tradecomtot += quant * price1x * (tradecomrate - 1F) // 총 수수로 최신화
                            if (price1x >= aver1x) {
                                profityear += (price1x - aver1x) * quant
                            }
                            quant1x -= quant
                            bought1x -= aver1x * quant
                            if (quant1x != 0) {
                                aver1x = bought1x / quant1x
                            } else {
                                aver1x = 0F
                            }
                        }
                        1 -> {
                            cash += quant * price3x * (2F - tradecomrate)// 현금에 매도금액 추가
                            sold += quant * price3x // 총 매도 금액 최신화
                            tradecomtot += quant * price3x * (tradecomrate - 1F) // 총 수수로 최신화
                            if (price3x >= aver3x) {
                                profityear += (price3x - aver3x) * quant
                            }
                            quant3x -= quant
                            bought3x -= aver3x * quant
                            if (quant3x != 0) {
                                aver3x = bought3x / quant3x
                            } else {
                                aver3x = 0F
                            }
                        }
                        2 -> {
                            cash += quant * priceinv1x * (2F - tradecomrate)// 현금에 매도금액 추가
                            sold += quant * priceinv1x // 총 매도 금액 최신화
                            tradecomtot += quant * priceinv1x * (tradecomrate - 1F) // 총 수수로 최신화
                            if (priceinv1x >= averinv1x) {
                                profityear += (priceinv1x - averinv1x) * quant
                            }
                            quantinv1x -= quant
                            boughtinv1x -= averinv1x * quant
                            if (quantinv1x != 0) {
                                averinv1x = boughtinv1x / quantinv1x
                            } else {
                                averinv1x = 0F
                            }
                        }
                        3 -> {
                            cash += quant * priceinv3x * (2F - tradecomrate)// 현금에 매도금액 추가
                            sold += quant * priceinv3x // 총 매도 금액 최신화
                            tradecomtot += quant * priceinv3x * (tradecomrate - 1F) // 총 수수로 최신화
                            if (priceinv3x >= averinv3x) {
                                profityear += (priceinv3x - averinv3x) * quant
                            }
                            quantinv3x -= quant
                            boughtinv3x -= averinv3x * quant
                            if (quantinv3x != 0) {
                                averinv3x = boughtinv3x / quantinv3x
                            } else {
                                averinv3x = 0F
                            }
                        }
                    }

                    profittot += profityear // 순손익 최신화

                    val addRunnable = Runnable {
                        val newGameNormalDB = GameNormal()
                        newGameNormalDB.id = localdatatime
                        newGameNormalDB.buyorsell = "매도"
                        newGameNormalDB.select = select
                        newGameNormalDB.price = price / quant
                        newGameNormalDB.volume = price
                        newGameNormalDB.quant = quant
                        newGameNormalDB.tradecom = temptradecom
                        newGameNormalDB.cash = cash
                        newGameNormalDB.setId = setId
                        gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                    }
                    val addThread = Thread(addRunnable)
                    addThread.start()
                }
            }
            dismiss()
        }
        return view
    }

    override fun onDestroy() {
        click = !click /////////////////////////////////////////////////////////////////////////////
        super.onDestroy()
    }
}
