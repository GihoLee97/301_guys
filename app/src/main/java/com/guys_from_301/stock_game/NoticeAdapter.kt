package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.Notice

class NoticeAdapter(val context: Context, var notice: List<Notice>, val itemClick: (Notice)->Unit):RecyclerView.Adapter<NoticeAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View, itemClick: (Notice) -> Unit): RecyclerView.ViewHolder(itemView){
        var view: View = itemView
        val tv_notice_title = itemView.findViewById<TextView>(R.id.tv_notice_title)
        val tv_notice_date = itemView.findViewById<TextView>(R.id.tv_notice_date)
        fun bind(notice : Notice){
            itemView.setOnClickListener{ itemClick(notice) }
            tv_notice_date.text = notice.date
            tv_notice_title.text = notice.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_notice, parent, false), itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentgame = notice[position]
        holder.apply {
            bind(currentgame)
        }
    }

    override fun getItemCount(): Int {
        return notice.size
    }
}