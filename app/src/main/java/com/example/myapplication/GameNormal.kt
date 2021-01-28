package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameNormal : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_normal)
        val buy_btn = findViewById<Button>(R.id.buy_btn)
        buy_btn.setOnClickListener {
            val dlg_buy = Dialog_buy(this)
            val layoutInflater_buy: LayoutInflater = getLayoutInflater()
            val builder_buy = AlertDialog.Builder(this)
            dlg_buy.start()
        }

        val sell_btn = findViewById<Button>(R.id.sell_btn)
        sell_btn.setOnClickListener {
            val dlg_sell = Dialog_sell(this)
            val layoutInflater_sell: LayoutInflater = getLayoutInflater()
            val builder_sell = AlertDialog.Builder(this)
            dlg_sell.start()
        }

        val item_btn = findViewById<Button>(R.id.item_btn)
        item_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.item_pick_dialog,null)
            builder.setView(dialogview).show()
        }
    }
}