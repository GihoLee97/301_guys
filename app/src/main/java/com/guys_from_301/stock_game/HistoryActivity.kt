package com.guys_from_301.stock_game

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        findViewById<ImageButton>(R.id.ib_go_back).setOnClickListener{
            onBackPressed()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val qRecyclerView = findViewById<RecyclerView>(R.id.qRecyclerView)
        val gridLayoutManager = GridLayoutManager(applicationContext, 1)
        qRecyclerView.layoutManager = gridLayoutManager



    }
}