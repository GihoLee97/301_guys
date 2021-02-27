package com.guys_from_301.stock_game

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Dialog_new_buyandsell : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v : View = inflater.inflate(R.layout.dialog_new_buyandsell, container)
        v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.GONE
        v.findViewById<TextView>(R.id.tv_bargain).setOnClickListener{
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_option).visibility = View.GONE
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.VISIBLE
        }
        v.findViewById<TextView>(R.id.tv_x1_plus).setOnClickListener{
            v.findViewById<TextView>(R.id.tv_bargain).setBackgroundColor(Color.parseColor("#F4730B"))
            v.findViewById<TextView>(R.id.tv_bargain_title).text = "x1 일반"
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_option).visibility = View.GONE
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.VISIBLE
        }
        v.findViewById<TextView>(R.id.tv_x3_plus).setOnClickListener{
            v.findViewById<TextView>(R.id.tv_bargain).setBackgroundColor(Color.parseColor("#F4730B"))
            v.findViewById<TextView>(R.id.tv_bargain_title).text = "x3 레버리지"
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_option).visibility = View.GONE
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.VISIBLE
        }
        v.findViewById<TextView>(R.id.tv_x1_minus).setOnClickListener{
            v.findViewById<TextView>(R.id.tv_bargain).setBackgroundColor(Color.parseColor("#F4730B"))
            v.findViewById<TextView>(R.id.tv_bargain_title).text = "x1 인버스"
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_option).visibility = View.GONE
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.VISIBLE
        }
        v.findViewById<TextView>(R.id.tv_x3_minus).setOnClickListener{
            v.findViewById<TextView>(R.id.tv_bargain).setBackgroundColor(Color.parseColor("#F4730B"))
            v.findViewById<TextView>(R.id.tv_bargain_title).text = "x3 인버스"
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_option).visibility = View.GONE
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.VISIBLE
        }
        v.findViewById<Button>(R.id.btn_buy).setOnClickListener{
            v.findViewById<Button>(R.id.btn_sell).setBackgroundResource(R.drawable.game_buyandsell_unchoosen)
            v.findViewById<Button>(R.id.btn_buy).setBackgroundResource(R.drawable.game_buyandsell_choose)
        }
        v.findViewById<Button>(R.id.btn_sell).setOnClickListener{
            v.findViewById<Button>(R.id.btn_buy).setBackgroundResource(R.drawable.game_buyandsell_unchoosen)
            v.findViewById<Button>(R.id.btn_sell).setBackgroundResource(R.drawable.game_buyandsell_choose)
        }
        v.findViewById<ImageView>(R.id.iv_go_option).setOnClickListener{
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_option).visibility = View.VISIBLE
            v.findViewById<FrameLayout>(R.id.fl_choose_buyandsell_number).visibility = View.GONE
            v.findViewById<TextView>(R.id.tv_bargain).setBackgroundColor(Color.parseColor("#B7B1B3"))

        }

        return v
    }
}