package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameNormal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_normal)

        val buy_btn = findViewById<Button>(R.id.buy_btn)
        buy_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.buy_dialog,null)
            builder.setView(dialogview).show()
        }

        val sell_btn = findViewById<Button>(R.id.sell_btn)
        sell_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.sell_dialog,null)
            builder.setView(dialogview).show()
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