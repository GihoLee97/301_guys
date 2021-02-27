package com.guys_from_301.stock_game

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.guys_from_301.stock_game.data.ProfileDB
import com.kakao.sdk.user.UserApiClient


var realkakaoplayer = mutableListOf<Dataclass_kakao>()
var arrayll = mutableListOf<Int>()
var arraytvname = mutableListOf<Int>()
var arraytvmoney = mutableListOf<Int>()
var arrayimage = mutableListOf<Int>()

class FragmentRanking : Fragment() {
    val coloron = "#F68A06"
    val coloroff = "#FFFFFF"
    var profileDb : ProfileDB? = null
    private lateinit var tv_my_level: TextView
    private lateinit var tv_my_stack: TextView
    private lateinit var tv_my_nick: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        v.findViewById<TextView>(R.id.tv_kakao_space).setBackgroundColor(Color.parseColor(coloron))
        v.findViewById<TextView>(R.id.tv_local_space).setBackgroundColor(Color.parseColor(coloroff))
        v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.GONE
        v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.VISIBLE

        v.findViewById<TextView>(R.id.tv_kakaoRanking).setOnClickListener{
            v.findViewById<TextView>(R.id.tv_kakao_space).setBackgroundColor(Color.parseColor(coloroff))
            v.findViewById<TextView>(R.id.tv_local_space).setBackgroundColor(Color.parseColor(coloron))
            v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.VISIBLE
            v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.GONE
            v.findViewById<TextView>(R.id.tv_kakaoRanking).setTextAppearance(activity,R.style.ranking_scope_checked)
            v.findViewById<TextView>(R.id.tv_localRanking).setTextAppearance(activity,R.style.ranking_scope_unchecked)
        }
        v.findViewById<TextView>(R.id.tv_localRanking).setOnClickListener {
            v.findViewById<TextView>(R.id.tv_kakao_space).setBackgroundColor(Color.parseColor(coloron))
            v.findViewById<TextView>(R.id.tv_local_space).setBackgroundColor(Color.parseColor(coloroff))
            v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.GONE
            v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.VISIBLE
            v.findViewById<TextView>(R.id.tv_kakaoRanking).setTextAppearance(activity,R.style.ranking_scope_unchecked)
            v.findViewById<TextView>(R.id.tv_localRanking).setTextAppearance(activity,R.style.ranking_scope_checked)
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
        friendsort()
        if(profileDb?.profileDao()?.getLogin() != 4){
//            v.findViewById<ImageView>(R.id.iv_my_image).visibility=View.INVISIBLE TODO!! check
            v.findViewById<TextView>(R.id.tv_local_space).visibility=View.GONE
        }
        else{
            UserApiClient.instance.me { user, error ->
                if (error!=null)
                    Toast.makeText(_MainActivity,"사용자 정보 요청 실패(카카오)", Toast.LENGTH_SHORT)
                else if (user!=null) {
                    Glide.with(_MainActivity!!).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop().into(v.findViewById<ImageView>(R.id.iv_my_image))
                }
            }
        }
        tv_my_level = v.findViewById(R.id.tv_my_level)
        tv_my_nick =  v.findViewById(R.id.tv_my_nick)
        tv_my_stack = v.findViewById(R.id.tv_my_stack)
        tv_my_stack.text = profileDb?.profileDao()?.getMoney()!!.toString()
        tv_my_nick.text = profileDb?.profileDao()?.getNickname()!!
        tv_my_level.text = "레벨 " + profileDb?.profileDao()?.getLevel()!!.toString()
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
                if(i==4){
                    v.findViewById<LinearLayout>(R.id.ll_kakaoUser4to10).visibility = View.VISIBLE
                }
            }
            for(i in realkakaoplayer.size+1..10){
                if(i<4){
                    v.findViewById<ConstraintLayout>(arrayll[i-1]).visibility = View.INVISIBLE
                }
                else{
                    if(i==4){
                        v.findViewById<LinearLayout>(R.id.ll_kakaoUser4to10).visibility = View.GONE
                    }
                    v.findViewById<LinearLayout>(arrayll[i-1]).visibility = View.GONE
                }
            }
        }
        return v
    }

    fun friendsort(){
        if(realkakaoplayer!=null){
            realkakaoplayer.clear()
        }
        for (cnt in 1..friendlevel.size){
            if(friendlevel[cnt-1] != -1){
                var tmp : Dataclass_kakao = Dataclass_kakao("a", "b", 0, 0, "c")
                friendlevel[cnt-1]
                tmp?.NAME = friendname[cnt-1];
                tmp?.NICKNAME = friendnick[cnt-1];
                tmp?.MONEY = friendmoney[cnt-1];
                tmp?.LEVEL = friendlevel[cnt-1]
                tmp?.IMAGE = friendimage[cnt-1]
                realkakaoplayer.add(tmp)
            }
        }

        //sort
        realkakaoplayer.sortByDescending { it.MONEY }

    }
}
