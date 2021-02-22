package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.Quest

class QuestAdapter(val context: Context, val questList: List<Quest>):RecyclerView.Adapter<QuestAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tv_quest = itemView.findViewById<TextView>(R.id.quest)
        private val check_achievement = itemView.findViewById<CheckBox>(R.id.achievement)
        fun bind(quest: Quest){
            if(quest.achievement==0) check_achievement.isChecked = true
            tv_quest.text = quest.questcontents
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.quest_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentquest = questList[position]
        holder.apply {
            bind(currentquest)
        }
    }

    override fun getItemCount(): Int {
        return questList.size
    }
}