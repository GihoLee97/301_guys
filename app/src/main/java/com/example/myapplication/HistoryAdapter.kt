package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.GameNormal

class HistoryAdapter(val context: Context, var history: List<GameNormal>): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var view : View = itemView
        val date = itemView.findViewById<TextView>(R.id.date)
        val buyorsell = itemView.findViewById<TextView>(R.id.buyorsell)
        val volume = itemView.findViewById<TextView>(R.id.volume)
        val fees = itemView.findViewById<TextView>(R.id.fees)
        val cash = itemView.findViewById<TextView>(R.id.cash)
        fun bind(history: GameNormal){
            date.text =  history.id.slice(IntRange(0,9))+history.id.slice(IntRange(11,18))
            buyorsell.text = history.buyorsell
            volume.text = history.volume.toString()+"원"
            fees.text = history.fees.toString()+"원"
            cash.text = history.cash.toString()+"원"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.history_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenthistory = history[position]
        holder.apply {
            bind(currenthistory)
        }
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun getItem(): List<GameNormal>{
        return history
    }
}