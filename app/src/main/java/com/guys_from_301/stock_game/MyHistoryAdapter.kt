package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameSet

class MyHistoryAdapter(val context: Context, var game: List<GameNormal>, val itemClick: (GameNormal)->Unit): RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View, itemClick: (GameNormal) -> Unit): RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val tv_date = itemView.findViewById<TextView>(R.id.tv_recyclerview_date)
        val tv_tradeday = itemView.findViewById<TextView>(R.id.tv_recyclerview_tradeday)
        val tv_profitrate = itemView.findViewById<TextView>(R.id.tv_recyclerview_profitrate)

        fun bind(gameUnit : GameNormal){
            if(gameUnit.id.length>10) tv_date.text = gameUnit.id.slice(IntRange(0,3))+"년 "+gameUnit.id.slice(IntRange(5,6))+"월 "+gameUnit.id.slice(IntRange(8,9))+"일"
            else tv_date.text = "-"
            tv_profitrate.text = per.format(gameUnit.profitrate).toString()+"%"
            tv_tradeday.text = "진행 거래일: "+gameUnit.item1able.toString()+"일"
            itemView.setOnClickListener{ itemClick(gameUnit) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_history_past, parent, false), itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentgame = game[position]
        holder.apply {
            bind(currentgame)
        }
    }

    override fun getItemCount(): Int {
        return game.size
    }

    fun getItem(): List<GameNormal>{
        return game
    }
}