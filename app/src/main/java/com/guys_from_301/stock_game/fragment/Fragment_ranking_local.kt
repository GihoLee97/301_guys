package com.guys_from_301.stock_game.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.guys_from_301.stock_game.R
import com.guys_from_301.stock_game.*
import org.w3c.dom.Text

class Fragment_ranking_local : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_ranking_local, container, false)
        v.findViewById<TextView>(R.id.tv_user1_nickname).text = rank1_nick
        v.findViewById<TextView>(R.id.tv_user2_nickname).text = rank2_nick
        v.findViewById<TextView>(R.id.tv_user3_nickname).text = rank3_nick
        v.findViewById<TextView>(R.id.tv_user4_nickname).text = rank4_nick
        v.findViewById<TextView>(R.id.tv_user5_nickname).text = rank5_nick
        v.findViewById<TextView>(R.id.tv_user6_nickname).text = rank6_nick
        v.findViewById<TextView>(R.id.tv_user7_nickname).text = rank7_nick
        v.findViewById<TextView>(R.id.tv_user8_nickname).text = rank8_nick
        v.findViewById<TextView>(R.id.tv_user9_nickname).text = rank9_nick
        v.findViewById<TextView>(R.id.tv_user10_nickname).text = rank10_nick

        v.findViewById<TextView>(R.id.tv_user1_money).text = rank1_money
        v.findViewById<TextView>(R.id.tv_user2_money).text = rank2_money
        v.findViewById<TextView>(R.id.tv_user3_money).text = rank3_money
        v.findViewById<TextView>(R.id.tv_user4_money).text = rank4_money
        v.findViewById<TextView>(R.id.tv_user5_money).text = rank5_money
        v.findViewById<TextView>(R.id.tv_user6_money).text = rank6_money
        v.findViewById<TextView>(R.id.tv_user7_money).text = rank7_money
        v.findViewById<TextView>(R.id.tv_user8_money).text = rank8_money
        v.findViewById<TextView>(R.id.tv_user9_money).text = rank9_money
        v.findViewById<TextView>(R.id.tv_user10_money).text = rank10_money

        return v
    }


}