package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context

class Dialog_loading
constructor(context: Context) : Dialog(context){
    init {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setContentView(R.layout.dialog_loading)
    }
}
