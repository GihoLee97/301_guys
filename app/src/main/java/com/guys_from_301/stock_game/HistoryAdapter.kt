package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.GameNormal

class HistoryAdapter(val context: Context, var history: List<GameNormal>): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var view : View = itemView
        val date = itemView.findViewById<TextView>(R.id.date)
        val buyorsell = itemView.findViewById<TextView>(R.id.buyorsell)
        val volume = itemView.findViewById<TextView>(R.id.volume)
        val fees = itemView.findViewById<TextView>(R.id.fees)
        val cash = itemView.findViewById<TextView>(R.id.cash)
        val price = itemView.findViewById<TextView>(R.id.price)
        val quant = itemView.findViewById<TextView>(R.id.quant)
        fun bind(history: GameNormal){
            date.text =  history.id
            buyorsell.text = history.buyorsell
            price.text = history.price.toString()+"원"
            quant.text = history.quant.toString()+"주"
            volume.text = history.volume.toString()+"원"
            fees.text = history.tradecom.toString()+"원"
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