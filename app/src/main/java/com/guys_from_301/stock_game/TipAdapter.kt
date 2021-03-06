package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TipRowAdapter (val context: Context, var tip: ArrayList<MyPost>, val itemClick: (MyPost)->Unit): RecyclerView.Adapter<TipRowAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View, itemClick: (MyPost) -> Unit): RecyclerView.ViewHolder(itemView){
        var view: View = itemView
        val tv_notice_title = itemView.findViewById<TextView>(R.id.tv_tip_title)
        fun bind(tip : MyPost){
            itemView.setOnClickListener{ itemClick(tip) }
            tv_notice_title.text = tip.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.tip_row, parent, false), itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenttip = tip[position]
        holder.apply {
            bind(currenttip)
        }
    }

    override fun getItemCount(): Int {
        return tip.size
    }
}