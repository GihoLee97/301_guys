package com.guys_from_301.stock_game

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AutoBottomDialogFragment(context: Context) : BottomSheetDialogFragment() {

    private var ontemp: Boolean = true
    private var ratiotemp: Int = 0
    private var selecttemp: Int = 0

    private lateinit var btn_autoclose: ImageButton
    private lateinit var tv_autoper: TextView
    private lateinit var sb_auto: SeekBar
    private lateinit var cl_1x: ConstraintLayout
    private lateinit var cl_3x: ConstraintLayout
    private lateinit var cl_inv1x: ConstraintLayout
    private lateinit var cl_inv3x: ConstraintLayout
    private lateinit var cl_autook: ConstraintLayout
    private lateinit var tv_autook: TextView
    private lateinit var tv_auto3xlock: TextView
    private lateinit var tv_autoinv3xlock: TextView


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_dialog_fragment_auto, container, false)

        // view 연결
        btn_autoclose = view.findViewById(R.id.btn_autoclose)
        tv_autoper = view.findViewById(R.id.tv_autoper)
        sb_auto = view.findViewById(R.id.sb_auto)
        cl_1x = view.findViewById(R.id.cl_1x)
        cl_3x = view.findViewById(R.id.cl_3x)
        cl_inv1x = view.findViewById(R.id.cl_inv1x)
        cl_inv3x = view.findViewById(R.id.cl_inv3x)
        cl_autook = view.findViewById(R.id.cl_autook)
        tv_autook = view.findViewById(R.id.tv_autook)
        tv_auto3xlock = view.findViewById(R.id.tv_auto3xlock)
        tv_autoinv3xlock = view.findViewById(R.id.tv_autoinv3xlock)

        // 초기화
        ontemp = autobuy
        ratiotemp = autoratio
        selecttemp = auto1x
        sb_auto.progress = ratiotemp
        tv_autoper.text = "$ratiotemp %"
        cl_autook.setBackgroundColor(Color.parseColor("#B7B1B3"))
        if (ontemp==true) {
            when (selecttemp) {
                0 -> {
                    cl_1x.setBackgroundResource(R.drawable.auto_choo)
                    cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                }
                1 -> {
                    cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_3x.setBackgroundResource(R.drawable.auto_choo)
                    cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                }
                2 -> {
                    cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_inv1x.setBackgroundResource(R.drawable.auto_choo)
                    cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                }
                3 -> {
                    cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                    cl_inv3x.setBackgroundResource(R.drawable.auto_choo)
                }
            }
            tv_autook.text = "자동 매수 변경하기"
        } else {
            cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
            cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
            cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
            cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
            tv_autook.text = "자동 매수하기"
        }

        lock()

        btn_autoclose.setOnClickListener {
            dismiss()
        }

        cl_1x.setOnClickListener {
            if (ontemp && selecttemp==0) {
                ontemp = false
                sb_auto.progress = 0
                ratiotemp = sb_auto.progress
                cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#B7B1B3"))
                cl_autook.isEnabled = false
                lock()
            } else {
                ontemp = true
                selecttemp = 0
                sb_auto.progress = 100
                ratiotemp = sb_auto.progress
                cl_1x.setBackgroundResource(R.drawable.auto_choo)
                cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#F4730B"))
                cl_autook.isEnabled = true
                lock()
            }
        }

        cl_3x.setOnClickListener {
            if (ontemp && selecttemp==1) {
                ontemp = false
                sb_auto.progress = 0
                ratiotemp = sb_auto.progress
                cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#B7B1B3"))
                cl_autook.isEnabled = false
                lock()
            } else {
                ontemp = true
                selecttemp = 1
                sb_auto.progress = 100
                ratiotemp = sb_auto.progress
                cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_3x.setBackgroundResource(R.drawable.auto_choo)
                cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#F4730B"))
                cl_autook.isEnabled = true
                lock()
            }
        }

        cl_inv1x.setOnClickListener {
            if (ontemp && selecttemp==2) {
                ontemp = false
                sb_auto.progress = 0
                ratiotemp = sb_auto.progress
                cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#B7B1B3"))
                cl_autook.isEnabled = false
                lock()
            } else {
                ontemp = true
                selecttemp = 2
                sb_auto.progress = 100
                ratiotemp = sb_auto.progress
                cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_inv1x.setBackgroundResource(R.drawable.auto_choo)
                cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#F4730B"))
                cl_autook.isEnabled = true
                lock()
            }
        }

        cl_inv3x.setOnClickListener {
            if (ontemp && selecttemp==3) {
                ontemp = false
                sb_auto.progress = 0
                ratiotemp = sb_auto.progress
                cl_inv3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_autook.setBackgroundColor(Color.parseColor("#B7B1B3"))
                cl_autook.isEnabled = false
                lock()
            } else {
                ontemp = true
                selecttemp = 3
                sb_auto.progress = 100
                ratiotemp = sb_auto.progress
                cl_1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_3x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_inv1x.setBackgroundResource(R.drawable.auto_unchoo)
                cl_inv3x.setBackgroundResource(R.drawable.auto_choo)
                cl_autook.setBackgroundColor(Color.parseColor("#F4730B"))
                cl_autook.isEnabled = true
                lock()
            }
        }

        sb_auto.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ratiotemp = progress
                tv_autoper.text = "$ratiotemp %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                ratiotemp = seekBar!!.progress
                tv_autoper.text = "$ratiotemp %"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                ratiotemp = seekBar!!.progress
                tv_autoper.text = "$ratiotemp %"
            }
        })

        cl_autook.setOnClickListener {
            if (!autobuy) {
                Toast.makeText(view.context, "자동 매수를 시작합니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(view.context, "자동 매수 설정을 변경합니다", Toast.LENGTH_SHORT).show()
            }
            autobuy = true
            auto1x = selecttemp
            ratiotemp = sb_auto.progress
            autoratio = ratiotemp
            dismiss()
        }
        return view
    }

    fun lock() {
        if (!item4Active) {
            cl_3x.setBackgroundResource(R.drawable.auto_lock)
            cl_inv3x.setBackgroundResource(R.drawable.auto_lock)
            tv_auto3xlock.text = "잠김"
            tv_autoinv3xlock.text = "잠김"
            tv_auto3xlock.setTextAppearance(R.style.auto_titlelock)
            tv_autoinv3xlock.setTextAppearance(R.style.auto_titlelock)
            cl_3x.isEnabled = false
            cl_inv3x.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        click = false /////////////////////////////////////////////////////////////////////////////
    }
}