package com.guys_from_301.stock_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

        return v
    }
}