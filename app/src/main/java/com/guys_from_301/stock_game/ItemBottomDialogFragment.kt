package com.guys_from_301.stock_game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt

class ItemBottomDialogFragment(context: Context) : BottomSheetDialogFragment() {

    private var item1ConsumeTemp: Int = 0
    private var item1lim: Int = 0
    private var item1limbyfatigue: Int = 0
    private var item1temp: Int = 0
    private var item2temp: Int = 0
    private var itemSelect: Int = 0

    private lateinit var ib_itemclose: ImageButton
    private lateinit var cl_itemselect: ConstraintLayout
    private lateinit var cl_item1: ConstraintLayout
    private lateinit var cl_item2: ConstraintLayout
    private lateinit var cl_item1detail: ConstraintLayout
    private lateinit var ib_item1main: ImageButton
    private lateinit var tv_item1consume: TextView
    private lateinit var sb_item1: SeekBar
    private lateinit var btn_item1minus: Button
    private lateinit var btn_item1plus: Button
    private lateinit var tv_item1day: TextView
    private lateinit var cl_item2detail: ConstraintLayout
    private lateinit var ib_item2main: ImageButton
    private lateinit var sb_item2: SeekBar
    private lateinit var tv_item2speed: TextView
    private lateinit var cl_itemok: ConstraintLayout
    private lateinit var pb_itemfatigue: ProgressBar
    private lateinit var tv_item4debug: TextView


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_dialog_fragment_item, container, false)

        // view 연결
        ib_itemclose = view.findViewById(R.id.ib_itemclose)

        cl_itemselect = view.findViewById(R.id.cl_itemselect)
        cl_item1 = view.findViewById(R.id.cl_item1)
        cl_item2 = view.findViewById(R.id.cl_item2)

        cl_item1detail = view.findViewById(R.id.cl_item1detail)
        ib_item1main = view.findViewById(R.id.ib_item1main)
        tv_item1consume = view.findViewById(R.id.tv_item1consume)
        sb_item1 = view.findViewById(R.id.sb_item1)
        btn_item1minus = view.findViewById(R.id.btn_item1minus)
        btn_item1plus = view.findViewById(R.id.btn_item1plus)
        tv_item1day = view.findViewById(R.id.tv_item1day)

        cl_item2detail = view.findViewById(R.id.cl_item2detail)
        ib_item2main = view.findViewById(R.id.ib_item2main)
        sb_item2 = view.findViewById(R.id.sb_item2)
        tv_item2speed = view.findViewById(R.id.tv_item2speed)

        cl_itemok = view.findViewById(R.id.cl_itemok)
        pb_itemfatigue = view.findViewById(R.id.pb_itemfatigue)
        tv_item4debug = view.findViewById(R.id.tv_item4debug)

        // 초기화
        cl_itemselect.visibility = VISIBLE
        cl_item1detail.visibility = GONE
        cl_item2detail.visibility = GONE
        cl_itemok.isEnabled = false
        pb_itemfatigue.progress = value1now

        if (((10000 - value1now) / 50F).roundToInt() - ((10000 - value1now) / 50F) > 0) {
            item1limbyfatigue = ((10000 - value1now) / 50F).roundToInt() - 1
        } else {
            item1limbyfatigue = ((10000 - value1now) / 50F).roundToInt()
        }

        if (item1limbyfatigue >= item1Able) {
            item1lim = item1Able
            item1temp = item1Able
            sb_item1.max = item1Able
        } else {
            item1lim = item1limbyfatigue
            item1temp = item1limbyfatigue
            sb_item1.max = item1limbyfatigue
        }

        item2temp = setGamespeed

        // 레버리지 언락 디버그용 코드
        tv_item4debug.setOnClickListener {
            item4Active = !item4Active
            dismiss()
        }

        ib_itemclose.setOnClickListener {
            dismiss()
        }

        cl_item1.setOnClickListener {
            itemSelect = 1
            cl_itemselect.visibility = GONE
            cl_item1detail.visibility = VISIBLE
            sb_item1.progress = item1lim
            item1ConsumeTemp = item1lim * 50
            tv_item1consume.text = "피로도 " + item1ConsumeTemp.toString() + " 증가"
            cl_itemok.isEnabled = true
            cl_itemok.setBackgroundResource(R.drawable.item_ok)
            pb_itemfatigue.progress = value1now
        }

        cl_item2.setOnClickListener {
            itemSelect = 2
            cl_itemselect.visibility = GONE
            cl_item2detail.visibility = VISIBLE
            sb_item2.progress = setGamespeed
            fatigue()
            pb_itemfatigue.progress = value1now
        }

        ib_item1main.setOnClickListener {
            itemSelect = 0
            cl_itemselect.visibility = VISIBLE
            cl_item1detail.visibility = GONE
            cl_itemok.isEnabled = false
            cl_itemok.setBackgroundResource(R.drawable.item_notok)
            item1temp = item1lim
            pb_itemfatigue.progress = value1now
        }

        ib_item2main.setOnClickListener {
            cl_itemselect.visibility = VISIBLE
            cl_item2detail.visibility = GONE
            cl_itemok.isEnabled = false
            cl_itemok.setBackgroundResource(R.drawable.item_notok)
            item2temp = setGamespeed
            pb_itemfatigue.progress = value1now
        }

        // TODO
        btn_item1minus.setOnClickListener {
            if (item1temp>1) {
                item1temp -= 1
                item1ConsumeTemp = item1temp * 50 // 피로도 증가
                tv_item1consume.text = "피로도 " + item1ConsumeTemp.toString() + " 증가"
                sb_item1.progress = item1temp
            } else {
                Toast.makeText(view.context, "되돌아갈 거래일은 하루이상 이어야 합니다", Toast.LENGTH_SHORT).show()
            }
        }

        btn_item1plus.setOnClickListener {
            if (item1temp<item1lim) {
                item1temp += 1
                item1ConsumeTemp = item1temp * 50 // 피로도 증가
                tv_item1consume.text = "피로도 " + item1ConsumeTemp.toString() + " 증가"
                sb_item1.progress = item1temp
            } else {
                Toast.makeText(view.context, "되돌아갈 수 있는 최대 범위입니다", Toast.LENGTH_SHORT).show()
            }
        }

        sb_item1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                item1temp = progress

                item1ConsumeTemp = item1temp * 50 // 피로도 증가
                tv_item1day.text = "$item1temp"
                tv_item1consume.text = "피로도 " + item1ConsumeTemp.toString() + " 증가"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                item1temp = seekBar!!.progress

                item1ConsumeTemp = item1temp * 50 // 피로도 증가
                tv_item1day.text = "$item1temp"
                tv_item1consume.text = "피로도 " + item1ConsumeTemp.toString() + " 증가"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                item1temp = seekBar!!.progress

                item1ConsumeTemp = item1temp * 50 // 피로도 증가
                tv_item1day.text = "$item1temp"
                tv_item1consume.text = "피로도 " + item1ConsumeTemp.toString() + " 증가"
            }
        })

        sb_item2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                item2temp = progress
                fatigue()
                if (item2temp== setGamespeed) {
                    cl_itemok.isEnabled = false
                    cl_itemok.setBackgroundResource(R.drawable.item_notok)
                } else {
                    cl_itemok.isEnabled = true
                    cl_itemok.setBackgroundResource(R.drawable.item_ok)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                item2temp = seekBar!!.progress
                fatigue()
                if (item2temp== setGamespeed) {
                    cl_itemok.isEnabled = false
                    cl_itemok.setBackgroundResource(R.drawable.item_notok)
                } else {
                    cl_itemok.isEnabled = true
                    cl_itemok.setBackgroundResource(R.drawable.item_ok)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                item2temp = seekBar!!.progress
                fatigue()
                if (item2temp== setGamespeed) {
                    cl_itemok.isEnabled = false
                    cl_itemok.setBackgroundResource(R.drawable.item_notok)
                } else {
                    cl_itemok.isEnabled = true
                    cl_itemok.setBackgroundResource(R.drawable.item_ok)
                }
            }
        })

        cl_itemok.setOnClickListener {
            when (itemSelect) {
                1 -> {
                    item1Active = true
                    item1Length = item1temp
                    Toast.makeText(view.context, "$item1temp 거래일을 되돌리고 피로도가 $item1ConsumeTemp 만큼 증가합니다", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    setGamespeed = item2temp
                }
            }
            dismiss()
        }
        return view
    }

    fun fatigue() {
        if (item2temp==0) {
            tv_item2speed.text =
                    "피로도 초당 3 증가, 진행속도 8 sec/day"
        } else if (item2temp==1) {
            tv_item2speed.text =
                    "피로도 초당 2 증가, 진행속도 4 sec/day"
        } else if (item2temp==2) {
            tv_item2speed.text =
                    "피로도 초당 1 증가, 진행속도 2 sec/day"
        } else if (item2temp==3) {
            tv_item2speed.text =
                    "피로도 증가 없음, 진행속도 1 day/sec"
        } else if (item2temp==4) {
            tv_item2speed.text =
                    "피로도 초당 1 증가, 진행속도 2 day/sec"
        } else if (item2temp==5) {
            tv_item2speed.text =
                    "피로도 초당 2 증가, 진행속도 4 day/sec"
        } else if (item2temp==6) {
            tv_item2speed.text =
                    "피로도 초당 3 증가, 진행속도 8 day/sec"
        } else if (item2temp==7) {
            tv_item2speed.text =
                    "피로도 초당 4 증가, 진행속도 10 day/sec"
        }
    }

    override fun onDestroy() {
        click = !click /////////////////////////////////////////////////////////////////////////////
        super.onDestroy()
    }
}