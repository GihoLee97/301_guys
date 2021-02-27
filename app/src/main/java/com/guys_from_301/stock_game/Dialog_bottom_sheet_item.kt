package com.guys_from_301.stock_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt

class Dialog_bottom_sheet_item : BottomSheetDialogFragment() {

    // 0 : select item layout / 1 : turn back time layout / 2 : adjust time speed layout
    var currentLayout = 0
    val SELECT_ITEM_LAYOUT = 0
    val TURN_BACK_TIME_LAYOUT = 1
    val ADJUST_TIME_SPEED_LAYOUT = 2
    var dayTOBack = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var v : View = inflater.inflate(R.layout.dialog_bottom_sheet_item, container)

        currentLayout = SELECT_ITEM_LAYOUT
        v.findViewById<LinearLayout>(R.id.ll_selectItemToUse).visibility = View.VISIBLE
        v.findViewById<ConstraintLayout>(R.id.cl_turnBackTime).visibility = View.GONE
        v.findViewById<ConstraintLayout>(R.id.cl_adjustTimeSpeed).visibility = View.GONE
        v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).background =
                ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_unactivated)
        v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).isClickable = false
        v.findViewById<TextView>(R.id.tv_day).text = dayTOBack.toString()

        v.findViewById<ConstraintLayout>(R.id.cl_goToTurnBackTime).setOnClickListener {
            currentLayout = TURN_BACK_TIME_LAYOUT
            v.findViewById<LinearLayout>(R.id.ll_selectItemToUse).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_turnBackTime).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_activated)
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).isClickable = true
        }

        v.findViewById<ConstraintLayout>(R.id.cl_goToAdjustTimeSpeed).setOnClickListener {
            currentLayout = ADJUST_TIME_SPEED_LAYOUT
            v.findViewById<LinearLayout>(R.id.ll_selectItemToUse).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_adjustTimeSpeed).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_activated)
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).isClickable = true
        }

        v.findViewById<ImageButton>(R.id.ib_backToSelectItem_turnBackTime).setOnClickListener{
            currentLayout = SELECT_ITEM_LAYOUT
            v.findViewById<LinearLayout>(R.id.ll_selectItemToUse).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_turnBackTime).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_unactivated)
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).isClickable = false
        }

        v.findViewById<ImageButton>(R.id.ib_backToSelectItem_adjustTimeSpeed).setOnClickListener{
            currentLayout = SELECT_ITEM_LAYOUT
            v.findViewById<LinearLayout>(R.id.ll_selectItemToUse).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_adjustTimeSpeed).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_unactivated)
            v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).isClickable = false
        }

        v.findViewById<ImageButton>(R.id.ib_closeItemUseDialog).setOnClickListener {
            // TODO!! dialog close code
        }

        v.findViewById<ConstraintLayout>(R.id.cl_useItem_time).setOnClickListener {
            if(currentLayout==TURN_BACK_TIME_LAYOUT){

            }
            else if(currentLayout==ADJUST_TIME_SPEED_LAYOUT){

            }
        }

        v.findViewById<Button>(R.id.btn_minus).setOnClickListener {
            if(dayTOBack>0){
                dayTOBack -= 1
                v.findViewById<TextView>(R.id.tv_day).text = dayTOBack.toString()
            }
        }

        v.findViewById<Button>(R.id.btn_plus).setOnClickListener {
            if(dayTOBack<1000){ // TODO!! set maximum
                dayTOBack += 1
                v.findViewById<TextView>(R.id.tv_day).text = dayTOBack.toString()
            }
        }

        v.findViewById<SeekBar>(R.id.sb_turnBackTime).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        v.findViewById<SeekBar>(R.id.sb_adjustTimeSpeed).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        return v
    }
}