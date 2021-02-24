package com.guys_from_301.stock_game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.guys_from_301.stock_game.data.ProfileDB


var realkakaoplayer = mutableListOf<Dataclass_kakao>()
var arrayll = mutableListOf<Int>()
var arraytvname = mutableListOf<Int>()
var arraytvmoney = mutableListOf<Int>()
var arrayimage = mutableListOf<Int>()

class FragmentRanking : Fragment() {
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
        var v : View = inflater.inflate(R.layout.fragment_ranking, container, false)
        profileDb = ProfileDB.getInstace(_MainActivity!!)
        if(profileDb?.profileDao()?.getLogin()!! != 4 ){
            v.findViewById<TextView>(R.id.tv_kakaoRanking).visibility = View.GONE
        }
        v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.GONE
        v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.VISIBLE
        v.findViewById<TextView>(R.id.tv_kakaoRanking).setOnClickListener{
            v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.VISIBLE
            v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.GONE
        }
        v.findViewById<TextView>(R.id.tv_localRanking).setOnClickListener {
            v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.GONE
            v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.VISIBLE
        }
        // local ranking
        v.findViewById<TextView>(R.id.tv_user1_nickname_local).text = rank1_nick
        v.findViewById<TextView>(R.id.tv_user2_nickname_local).text = rank2_nick
        v.findViewById<TextView>(R.id.tv_user3_nickname_local).text = rank3_nick
        v.findViewById<TextView>(R.id.tv_user4_nickname_local).text = rank4_nick
        v.findViewById<TextView>(R.id.tv_user5_nickname_local).text = rank5_nick
        v.findViewById<TextView>(R.id.tv_user6_nickname_local).text = rank6_nick
        v.findViewById<TextView>(R.id.tv_user7_nickname_local).text = rank7_nick
        v.findViewById<TextView>(R.id.tv_user8_nickname_local).text = rank8_nick
        v.findViewById<TextView>(R.id.tv_user9_nickname_local).text = rank9_nick
        v.findViewById<TextView>(R.id.tv_user10_nickname_local).text = rank10_nick

        v.findViewById<TextView>(R.id.tv_user1_money_local).text = rank1_money
        v.findViewById<TextView>(R.id.tv_user2_money_local).text = rank2_money
        v.findViewById<TextView>(R.id.tv_user3_money_local).text = rank3_money
        v.findViewById<TextView>(R.id.tv_user4_money_local).text = rank4_money
        v.findViewById<TextView>(R.id.tv_user5_money_local).text = rank5_money
        v.findViewById<TextView>(R.id.tv_user6_money_local).text = rank6_money
        v.findViewById<TextView>(R.id.tv_user7_money_local).text = rank7_money
        v.findViewById<TextView>(R.id.tv_user8_money_local).text = rank8_money
        v.findViewById<TextView>(R.id.tv_user9_money_local).text = rank9_money
        v.findViewById<TextView>(R.id.tv_user10_money_local).text = rank10_money

        // kakao ranking
        if(arrayll !=null || arraytvname != null || arraytvmoney != null){
            arrayll.clear()
            arraytvmoney.clear()
            arraytvname.clear()
        }
        arrayll.add(R.id.ll_kakaoUser1)
        arrayll.add(R.id.ll_kakaoUser2)
        arrayll.add(R.id.ll_kakaoUser3)
        arrayll.add(R.id.ll_kakaoUser4)
        arrayll.add(R.id.ll_kakaoUser5)
        arrayll.add(R.id.ll_kakaoUser6)
        arrayll.add(R.id.ll_kakaoUser7)
        arrayll.add(R.id.ll_kakaoUser8)
        arrayll.add(R.id.ll_kakaoUser9)
        arrayll.add(R.id.ll_kakaoUser10)
        arraytvname.add(R.id.tv_user1_nickname_kakao)
        arraytvname.add(R.id.tv_user2_nickname_kakao);
        arraytvname.add(R.id.tv_user3_nickname_kakao);
        arraytvname.add(R.id.tv_user4_nickname_kakao);
        arraytvname.add(R.id.tv_user5_nickname_kakao);
        arraytvname.add(R.id.tv_user6_nickname_kakao);
        arraytvname.add(R.id.tv_user7_nickname_kakao);
        arraytvname.add(R.id.tv_user8_nickname_kakao);
        arraytvname.add(R.id.tv_user9_nickname_kakao);
        arraytvname.add(R.id.tv_user10_nickname_kakao);
        arraytvmoney.add(R.id.tv_user1_money_kakao)
        arraytvmoney.add(R.id.tv_user2_money_kakao)
        arraytvmoney.add(R.id.tv_user3_money_kakao)
        arraytvmoney.add(R.id.tv_user4_money_kakao)
        arraytvmoney.add(R.id.tv_user5_money_kakao)
        arraytvmoney.add(R.id.tv_user6_money_kakao)
        arraytvmoney.add(R.id.tv_user7_money_kakao)
        arraytvmoney.add(R.id.tv_user8_money_kakao)
        arraytvmoney.add(R.id.tv_user9_money_kakao)
        arraytvmoney.add(R.id.tv_user10_money_kakao)
        arrayimage.add(R.id.iv_user1_image_kakao)
        arrayimage.add(R.id.iv_user2_image_kakao)
        arrayimage.add(R.id.iv_user3_image_kakao)
        arrayimage.add(R.id.iv_user4_image_kakao)
        arrayimage.add(R.id.iv_user5_image_kakao)
        arrayimage.add(R.id.iv_user6_image_kakao)
        arrayimage.add(R.id.iv_user7_image_kakao)
        arrayimage.add(R.id.iv_user8_image_kakao)
        arrayimage.add(R.id.iv_user9_image_kakao)
        arrayimage.add(R.id.iv_user10_image_kakao)
        if(realkakaoplayer.size>=10){
            for(i in 1..10){
                v.findViewById<TextView>(arraytvmoney[i-1]).text = realkakaoplayer[i-1].MONEY.toString()
                v.findViewById<TextView>(arraytvname[i-1]).text = realkakaoplayer[i-1].NAME
                Glide.with(v).load(realkakaoplayer[i-1].IMAGE).circleCrop().into(v.findViewById<ImageView>(arrayimage[i-1]))
            }
        }
        else if(realkakaoplayer == null || realkakaoplayer.size ==0){
            for(i in 1..10){
                v.findViewById<LinearLayout>(arrayll[i-1]).visibility = View.GONE
            }
        }
        else{
            for(i in 1..realkakaoplayer.size){
                v.findViewById<TextView>(arraytvmoney[i-1]).text = realkakaoplayer[i-1].MONEY.toString()
                v.findViewById<TextView>(arraytvname[i-1]).text = realkakaoplayer[i-1].NAME
                Glide.with(v).load(realkakaoplayer[i-1].IMAGE).circleCrop().into(v.findViewById<ImageView>(arrayimage[i-1]))
            }
            for(i in realkakaoplayer.size+1..10){
                v.findViewById<LinearLayout>(arrayll[i-1]).visibility = View.GONE
            }
        }
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
            FragmentRanking().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
