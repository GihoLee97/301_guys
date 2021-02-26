package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet

class MyGameSetAdapter(val context: Context, var game: List<GameSet>, var gameNormalDB: GameNormalDB, val itemClick: (GameSet) -> Unit): RecyclerView.Adapter<MyGameSetAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View, itemClick: (GameSet) -> Unit): RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val endtime = itemView.findViewById<TextView>(R.id.endtime)
        val gameName = itemView.findViewById<TextView>(R.id.gameName)
        val profitRate = itemView.findViewById<TextView>(R.id.profitrate)
        fun bind(gameUnit : GameSet){
            if(gameNormalDB?.gameNormalDao()?.getSetWithNormal(gameUnit.id).isNullOrEmpty()) {
                endtime.text = ""
                gameName.text = "새 게임 추가하기"
                profitRate.text = ""
            }
            else {
                endtime.text = gameUnit.endtime.slice(IntRange(0,3))+"."+gameUnit.endtime.slice(IntRange(5,6))+"."+gameUnit.endtime.slice(IntRange(8,9))+" "+gameUnit.endtime.slice(IntRange(11,15))
                gameName.text = "투자공간" + gameUnit.id
                profitRate.text = gameUnit.profitrate.toString()+"%"
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