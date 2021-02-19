package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameSet

class PickAdapter (val context: Context, var game: List<GameSet>, val itemClick: (GameSet) -> Unit): RecyclerView.Adapter<PickAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View, itemClick: (GameSet) -> Unit): RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val name = itemView.findViewById<TextView>(R.id.gameName)
        fun bind(game : GameSet){
            name.text = game.id.toString()
            itemView.setOnClickListener{ itemClick(game) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.game_row, parent, false), itemClick)
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