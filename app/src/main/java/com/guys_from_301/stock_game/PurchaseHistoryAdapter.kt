package com.guys_from_301.stock_game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.Notice

class PurchaseHistoryAdapter(val context: Context, var purchaseList: List<MyPost>): RecyclerView.Adapter<PurchaseHistoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var view: View = itemView
        val tv_purchase_history_date = itemView.findViewById<TextView>(R.id.tv_purchase_history_date)
        val tv_purchase_history_price = itemView.findViewById<TextView>(R.id.tv_purchase_history_price)
        val tv_purchase_history_stack = itemView.findViewById<TextView>(R.id.tv_purchase_history_stack)
        fun bind(purchase : MyPost){
            tv_purchase_history_date.text = purchase.date
            tv_purchase_history_stack.text = purchase.title
            tv_purchase_history_price.text = purchase.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.purchase_history_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentpurchase = purchaseList[position]
        holder.apply {
            bind(currentpurchase)
        }
    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }





}