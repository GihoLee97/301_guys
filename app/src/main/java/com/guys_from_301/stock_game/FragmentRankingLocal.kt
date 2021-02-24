package com.guys_from_301.stock_game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.guys_from_301.stock_game.data.ProfileDB

class FragmentRankingLocal : Fragment() {
    var profileDb : ProfileDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v : View = inflater.inflate(R.layout.fragment_rankinglocal, container, false)
        profileDb = ProfileDB.getInstace(_MainActivity!!)
        if(profileDb?.profileDao()?.getLogin()!! != 4 ){
            v.findViewById<TextView>(R.id.tv_kakaoRanking).visibility = View.GONE
        }
        v.findViewById<TextView>(R.id.tv_kakaoRanking).setOnClickListener{(activity as MainActivity).goRankingKaKao()}
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRankingLocal().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
