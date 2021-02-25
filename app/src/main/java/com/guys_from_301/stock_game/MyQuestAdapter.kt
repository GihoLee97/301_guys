package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.Quest

class MyQuestAdapter(val context: Context, val questList: List<Quest>, val achievementList: List<Int>):RecyclerView.Adapter<MyQuestAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tv_questContents = itemView.findViewById<TextView>(R.id.tv_questContents)
        private val pb_achievement = itemView.findViewById<ProgressBar>(R.id.pb_achivement)
        private val tv_theme = itemView.findViewById<TextView>(R.id.tv_theme)
        fun bind(quest: Quest, achievement: Int){
            tv_theme.text = quest.theme
            pb_achievement.setProgress(achievement)
            tv_questContents.text = quest.questcontents
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_quest_gradual_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentquest = questList[position]
        val currentachievement = achievementList[position]
        holder.apply {
            bind(currentquest, currentachievement)
        }
    }

    override fun getItemCount(): Int {
        return questList.size
    }

}