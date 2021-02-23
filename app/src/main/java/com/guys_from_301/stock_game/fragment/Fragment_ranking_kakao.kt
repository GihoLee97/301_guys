package com.guys_from_301.stock_game.fragment

import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.guys_from_301.stock_game.*
import com.guys_from_301.stock_game.data.Quest

var realkakaoplayer = mutableListOf<Dataclass_kakao>()
var arrayll = mutableListOf<Int>()
var arraytvname = mutableListOf<Int>()
var arraytvmoney = mutableListOf<Int>()
var arrayimage = mutableListOf<Int>()
class Fragment_ranking_kakao : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_ranking_kakao, container, false)

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
        arraytvname.add(R.id.tv_user1_nickname);
        arraytvname.add(R.id.tv_user2_nickname);
        arraytvname.add(R.id.tv_user3_nickname);
        arraytvname.add(R.id.tv_user4_nickname);
        arraytvname.add(R.id.tv_user5_nickname);
        arraytvname.add(R.id.tv_user6_nickname);
        arraytvname.add(R.id.tv_user7_nickname);
        arraytvname.add(R.id.tv_user8_nickname);
        arraytvname.add(R.id.tv_user9_nickname);
        arraytvname.add(R.id.tv_user10_nickname);
        arraytvmoney.add(R.id.tv_user1_money)
        arraytvmoney.add(R.id.tv_user2_money)
        arraytvmoney.add(R.id.tv_user3_money)
        arraytvmoney.add(R.id.tv_user4_money)
        arraytvmoney.add(R.id.tv_user5_money)
        arraytvmoney.add(R.id.tv_user6_money)
        arraytvmoney.add(R.id.tv_user7_money)
        arraytvmoney.add(R.id.tv_user8_money)
        arraytvmoney.add(R.id.tv_user9_money)
        arraytvmoney.add(R.id.tv_user10_money)
        arrayimage.add(R.id.iv_user1_image)
        arrayimage.add(R.id.iv_user2_image)
        arrayimage.add(R.id.iv_user3_image)
        arrayimage.add(R.id.iv_user4_image)
        arrayimage.add(R.id.iv_user5_image)
        arrayimage.add(R.id.iv_user6_image)
        arrayimage.add(R.id.iv_user7_image)
        arrayimage.add(R.id.iv_user8_image)
        arrayimage.add(R.id.iv_user9_image)
        arrayimage.add(R.id.iv_user10_image)
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

}