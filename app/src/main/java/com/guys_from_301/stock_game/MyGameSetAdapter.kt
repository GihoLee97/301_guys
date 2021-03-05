package com.guys_from_301.stock_game

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet

class MyGameSetAdapter(val context: Context, var game: List<GameSet>, val itemClick: (GameSet) -> Unit): RecyclerView.Adapter<MyGameSetAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View, itemClick: (GameSet) -> Unit): RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val endtime = itemView.findViewById<TextView>(R.id.endtime)
        val gameName = itemView.findViewById<TextView>(R.id.gameName)
        val profitRate = itemView.findViewById<TextView>(R.id.profitrate)
        val lc_chart = itemView.findViewById<LineChart>(R.id.lc_chart)
        val tv_addGame = itemView.findViewById<TextView>(R.id.tv_addGame)
        val tv_currentProfitTitle = itemView.findViewById<TextView>(R.id.tv_currentProfitTitle)

        fun bind(gameUnit : GameSet){
            // set
            itemView.layoutParams.width = (dpWidth - (60/540.0*dpWidth).toInt())*2
            if(gameUnit.endtime == "") {
                lc_chart.visibility = View.GONE
                tv_addGame.visibility = View.VISIBLE
                endtime.text = ""
                gameName.text = "새로운 게임 만들기"
                profitRate.text = ""
                tv_currentProfitTitle.text = "최대 3개까지 생성 가능"
            }
            else {
                lc_chart.visibility = View.VISIBLE
                tv_addGame.visibility = View.GONE
                //endtime gamenormal 기준
                if(gameUnit.endtime.length>10)endtime.text = gameUnit.endtime.slice(IntRange(0,3))+"."+gameUnit.endtime.slice(IntRange(5,6))+"."+gameUnit.endtime.slice(IntRange(8,9))+" "+gameUnit.endtime.slice(IntRange(11,15))
                else endtime.text = gameUnit.endtime
                gameName.text = "투자공간" + gameUnit.id.last()
                profitRate.text = per.format(gameUnit.profitrate).toString()+"%"
            }
            itemView.setOnClickListener{ itemClick(gameUnit) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_games_item, parent, false), itemClick)
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

    fun getItem(): List<GameSet>{
        return game
    }
}