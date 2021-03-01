package com.guys_from_301.stock_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
        var v : View = inflater.inflate(R.layout.bottom_dialog_fragment_item, container)

        currentLayout = SELECT_ITEM_LAYOUT
        v.findViewById<LinearLayout>(R.id.cl_itemselect).visibility = View.VISIBLE
        v.findViewById<ConstraintLayout>(R.id.cl_item1detail).visibility = View.GONE
        v.findViewById<ConstraintLayout>(R.id.cl_item2detail).visibility = View.GONE
        v.findViewById<ConstraintLayout>(R.id.cl_itemok).background =
                ContextCompat.getDrawable(requireContext(),R.drawable.item_notok)
        v.findViewById<ConstraintLayout>(R.id.cl_itemok).isClickable = false
        v.findViewById<TextView>(R.id.tv_item1day).text = dayTOBack.toString()

        v.findViewById<ConstraintLayout>(R.id.cl_item1).setOnClickListener {
            currentLayout = TURN_BACK_TIME_LAYOUT
            v.findViewById<LinearLayout>(R.id.cl_itemselect).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_item1detail).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_activated)
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).isClickable = true
        }

        v.findViewById<ConstraintLayout>(R.id.cl_item2).setOnClickListener {
            currentLayout = ADJUST_TIME_SPEED_LAYOUT
            v.findViewById<LinearLayout>(R.id.cl_itemselect).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_item2detail).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_dialog_bottom_sheet_item_button_activated)
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).isClickable = true
        }

        v.findViewById<ImageButton>(R.id.ib_item1main).setOnClickListener{
            currentLayout = SELECT_ITEM_LAYOUT
            v.findViewById<LinearLayout>(R.id.cl_itemselect).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_item1detail).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.item_notok)
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).isClickable = false
        }

        v.findViewById<ImageButton>(R.id.ib_item2main).setOnClickListener{
            currentLayout = SELECT_ITEM_LAYOUT
            v.findViewById<LinearLayout>(R.id.cl_itemselect).visibility = View.VISIBLE
            v.findViewById<ConstraintLayout>(R.id.cl_item2detail).visibility = View.GONE
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).background =
                    ContextCompat.getDrawable(requireContext(),R.drawable.item_notok)
            v.findViewById<ConstraintLayout>(R.id.cl_itemok).isClickable = false
        }

        v.findViewById<ImageButton>(R.id.ib_itemclose).setOnClickListener {
            // TODO!! dialog close code
        }

        v.findViewById<ConstraintLayout>(R.id.cl_itemok).setOnClickListener {
            if(currentLayout==TURN_BACK_TIME_LAYOUT){

            }
            else if(currentLayout==ADJUST_TIME_SPEED_LAYOUT){

            }
        }

        v.findViewById<Button>(R.id.btn_item1minus).setOnClickListener {
            if(dayTOBack>0){
                dayTOBack -= 1
                v.findViewById<TextView>(R.id.tv_item1day).text = dayTOBack.toString()
            }
        }

        v.findViewById<Button>(R.id.btn_item1plus).setOnClickListener {
            if(dayTOBack<1000){ // TODO!! set maximum
                dayTOBack += 1
                v.findViewById<TextView>(R.id.tv_item1day).text = dayTOBack.toString()
            }
        }

        v.findViewById<SeekBar>(R.id.sb_item1).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        v.findViewById<SeekBar>(R.id.sb_item2).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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