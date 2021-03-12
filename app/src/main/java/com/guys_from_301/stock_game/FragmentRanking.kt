package com.guys_from_301.stock_game

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.delay

val profileImageId = arrayOf(R.drawable.ic_profile_image_templete1,
        R.drawable.ic_profile_image_templete2,R.drawable.ic_profile_image_templete3,
        R.drawable.ic_profile_image_templete4,R.drawable.ic_profile_image_templete5)

var realkakaoplayer = mutableListOf<Dataclass_kakao>()
var arrayll = mutableListOf<Int>()
var arraytvname = mutableListOf<Int>()
var arraytvmoney = mutableListOf<Int>()
var arrayimage = mutableListOf<Int>()
// 친구 정보
var friendid = mutableListOf<String>()
var friendmoney = mutableListOf<Int>()
var friendlevel = mutableListOf<Int>()
var friendnick = mutableListOf<String>()
var friendname = mutableListOf<String>()
var frienduuid = mutableListOf<String>()
var friendimage = mutableListOf<String>()


class FragmentRanking : Fragment() {
    val coloron = "#ED9D0B"
    val coloroff = "#FFFFFF"
//    var profileDb : ProfileDB? = null
    private lateinit var tv_my_level: TextView
    private lateinit var tv_my_stack: TextView
    private lateinit var tv_my_nick: TextView

    private var myLocalImageViewId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v : View = inflater.inflate(R.layout.fragment_ranking, container, false)
//        profileDb = ProfileDB.getInstace(_MainActivity!!)
        activity?.window?.statusBarColor = resources.getColor(R.color.themeFragment)

        if(profileDbManager!!.getLogin()!! != 4 ){
            v.findViewById<TextView>(R.id.tv_friend_invite).visibility = View.GONE
            v.findViewById<TextView>(R.id.tv_kakaoRanking).visibility = View.GONE
        }
        v.findViewById<TextView>(R.id.tv_kakao_space).setBackgroundColor(Color.parseColor(coloron))
        v.findViewById<TextView>(R.id.tv_local_space).setBackgroundColor(Color.parseColor(coloroff))
        v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.GONE
        v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.VISIBLE
        v.findViewById<ImageView>(R.id.iv_my_image_kakao).visibility = View.GONE
        v.findViewById<ImageView>(R.id.iv_my_image_local).visibility = View.VISIBLE

        v.findViewById<TextView>(R.id.tv_kakaoRanking).setOnClickListener{
            v.findViewById<TextView>(R.id.tv_kakao_space).setBackgroundColor(Color.parseColor(coloroff))
            v.findViewById<TextView>(R.id.tv_local_space).setBackgroundColor(Color.parseColor(coloron))
            v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.VISIBLE
            v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.GONE
            v.findViewById<TextView>(R.id.tv_kakaoRanking).setTextAppearance(activity,R.style.ranking_scope_checked)
            v.findViewById<TextView>(R.id.tv_localRanking).setTextAppearance(activity,R.style.ranking_scope_unchecked)
            v.findViewById<ImageView>(R.id.iv_my_image_kakao).visibility = View.VISIBLE
            v.findViewById<ImageView>(R.id.iv_my_image_local).visibility = View.GONE
        }
        v.findViewById<TextView>(R.id.tv_localRanking).setOnClickListener {
            v.findViewById<TextView>(R.id.tv_kakao_space).setBackgroundColor(Color.parseColor(coloron))
            v.findViewById<TextView>(R.id.tv_local_space).setBackgroundColor(Color.parseColor(coloroff))
            v.findViewById<ScrollView>(R.id.sv_ranking_kakao).visibility = View.GONE
            v.findViewById<ScrollView>(R.id.sv_ranking_local).visibility = View.VISIBLE
            v.findViewById<TextView>(R.id.tv_kakaoRanking).setTextAppearance(activity,R.style.ranking_scope_unchecked)
            v.findViewById<TextView>(R.id.tv_localRanking).setTextAppearance(activity,R.style.ranking_scope_checked)
            v.findViewById<ImageView>(R.id.iv_my_image_kakao).visibility = View.GONE
            v.findViewById<ImageView>(R.id.iv_my_image_local).visibility = View.VISIBLE
        }


        v.findViewById<ImageView>(R.id.iv_my_image_local).setOnClickListener {
            val dlg = Dialog_imageChange(_MainActivity!!)
            dlg.start()
        }

        // local ranking
        val ranker_list : MutableList<String> = mutableListOf(rank1_nick, rank2_nick, rank3_nick, rank4_nick,
                rank5_nick,rank6_nick,rank7_nick,rank8_nick,rank9_nick,rank10_nick)
        var rank_number : Int = 0
        var ranker_local_nick = mutableListOf<Int>()
        var ranker_local_image = mutableListOf<Int>()

        for(i in 1..10){
            if(ranker_list[i-1] == profileDbManager!!.getNickname()!!){
                rank_number = i
            }
        }

        v.findViewById<TextView>(R.id.tv_user1_nickname_local).text = rank1_nick; ranker_local_nick.add(R.id.tv_user1_nickname_local)
        v.findViewById<TextView>(R.id.tv_user2_nickname_local).text = rank2_nick; ranker_local_nick.add(R.id.tv_user2_nickname_local)
        v.findViewById<TextView>(R.id.tv_user3_nickname_local).text = rank3_nick; ranker_local_nick.add(R.id.tv_user3_nickname_local)
        v.findViewById<TextView>(R.id.tv_user4_nickname_local).text = rank4_nick; ranker_local_nick.add(R.id.tv_user4_nickname_local)
        v.findViewById<TextView>(R.id.tv_user5_nickname_local).text = rank5_nick; ranker_local_nick.add(R.id.tv_user5_nickname_local)
        v.findViewById<TextView>(R.id.tv_user6_nickname_local).text = rank6_nick; ranker_local_nick.add(R.id.tv_user6_nickname_local)
        v.findViewById<TextView>(R.id.tv_user7_nickname_local).text = rank7_nick; ranker_local_nick.add(R.id.tv_user7_nickname_local)
        v.findViewById<TextView>(R.id.tv_user8_nickname_local).text = rank8_nick; ranker_local_nick.add(R.id.tv_user8_nickname_local)
        v.findViewById<TextView>(R.id.tv_user9_nickname_local).text = rank9_nick; ranker_local_nick.add(R.id.tv_user9_nickname_local)
        v.findViewById<TextView>(R.id.tv_user10_nickname_local).text = rank10_nick; ranker_local_nick.add(R.id.tv_user10_nickname_local)

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

        v.findViewById<ImageView>(R.id.iv_user1_image_local).setImageResource(profileImageId[ranker_image[0]]); ranker_local_image.add(R.id.iv_user1_image_local)
        v.findViewById<ImageView>(R.id.iv_user2_image_local).setImageResource(profileImageId[ranker_image[1]]); ranker_local_image.add(R.id.iv_user2_image_local)
        v.findViewById<ImageView>(R.id.iv_user3_image_local).setImageResource(profileImageId[ranker_image[2]]); ranker_local_image.add(R.id.iv_user3_image_local)
        v.findViewById<ImageView>(R.id.iv_user4_image_local).setImageResource(profileImageId[ranker_image[3]]); ranker_local_image.add(R.id.iv_user4_image_local)
        v.findViewById<ImageView>(R.id.iv_user5_image_local).setImageResource(profileImageId[ranker_image[4]]); ranker_local_image.add(R.id.iv_user5_image_local)
        v.findViewById<ImageView>(R.id.iv_user6_image_local).setImageResource(profileImageId[ranker_image[5]]); ranker_local_image.add(R.id.iv_user6_image_local)
        v.findViewById<ImageView>(R.id.iv_user7_image_local).setImageResource(profileImageId[ranker_image[6]]); ranker_local_image.add(R.id.iv_user7_image_local)
        v.findViewById<ImageView>(R.id.iv_user8_image_local).setImageResource(profileImageId[ranker_image[7]]); ranker_local_image.add(R.id.iv_user8_image_local)
        v.findViewById<ImageView>(R.id.iv_user9_image_local).setImageResource(profileImageId[ranker_image[8]]); ranker_local_image.add(R.id.iv_user9_image_local)
        v.findViewById<ImageView>(R.id.iv_user10_image_local).setImageResource(profileImageId[ranker_image[9]]); ranker_local_image.add(R.id.iv_user10_image_local)

        myLocalImageViewId = ranker_local_image[rank_number-1]

        if(rank_number==1 ||rank_number==2||rank_number==3){
            v.findViewById<TextView>(ranker_local_nick!![rank_number-1]).setTextAppearance(R.style.ranking_rank_nickname_highlight_1to3)
        }
        else if(rank_number>=4 && rank_number<=10){
              v.findViewById<TextView>(ranker_local_nick!![rank_number-1]).setTextAppearance(R.style.ranking_rank_nickname_highlight_1to3)
        }
        // kakao ranking

        Glide.with(_MainActivity!!).load(profileImageId[profileDbManager!!.getImageNum()!!]).circleCrop().into(v.findViewById(R.id.iv_my_image_local))


        friendsort()
        if(profileDbManager!!.getLogin() != 4){
                v.findViewById<TextView>(R.id.tv_local_space).visibility = View.GONE
        }
        else{
            UserApiClient.instance.me { user, error ->
                if (error!=null)
                    Toast.makeText(_MainActivity,"사용자 정보 요청 실패(카카오)", Toast.LENGTH_SHORT)
                else if (user!=null) {
                    Glide.with(_MainActivity!!).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop().into(v.findViewById(R.id.iv_my_image_kakao))
                }
            }

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
                    if(realkakaoplayer[i-1].ID == getHash(profileDbManager!!.getLoginId()!!) ){
                        if(i==1 || i==2 || i==3){
                            v.findViewById<TextView>(arraytvname[i-1]).setTextAppearance(R.style.ranking_rank_nickname_highlight_1to3)
                        }
                        else if(i>=4 && i<=10){
                            //TODO: 아직
                        }
                        v.findViewById<TextView>(arraytvmoney[i-1]).text = realkakaoplayer[i-1].MONEY.toString()
                        v.findViewById<TextView>(arraytvname[i-1]).text = realkakaoplayer[i-1].NAME
                        Glide.with(v).load(realkakaoplayer[i-1].IMAGE).circleCrop().into(v.findViewById<ImageView>(arrayimage[i-1]))
                    }
                    else{
                        v.findViewById<TextView>(arraytvmoney[i-1]).text = realkakaoplayer[i-1].MONEY.toString()
                        v.findViewById<TextView>(arraytvname[i-1]).text = realkakaoplayer[i-1].NAME
                        Glide.with(v).load(realkakaoplayer[i-1].IMAGE).circleCrop().into(v.findViewById<ImageView>(arrayimage[i-1]))
                    }
                }
            }
            else if(realkakaoplayer == null || realkakaoplayer.size ==0){
                for(i in 1..10){
                    v.findViewById<LinearLayout>(arrayll[i-1]).visibility = View.GONE
                }
            }
            else{
                for(i in 1..realkakaoplayer.size){
                    if(realkakaoplayer[i-1].ID == getHash(profileDbManager!!.getLoginId()!!)){
                        if(i==1 || i==2 || i==3){
                            v.findViewById<TextView>(arraytvname[i-1]).setTextAppearance(R.style.ranking_rank_nickname_highlight_1to3)
                        }
                        else if(i>=4 && i<=10){
                            //TODO: 아직
                        }
                        v.findViewById<TextView>(arraytvmoney[i-1]).text = realkakaoplayer[i-1].MONEY.toString()
                        v.findViewById<TextView>(arraytvname[i-1]).text = realkakaoplayer[i-1].NAME
                        Glide.with(v).load(realkakaoplayer[i-1].IMAGE).circleCrop().into(v.findViewById<ImageView>(arrayimage[i-1]))
                    }
                    else{
                        v.findViewById<TextView>(arraytvmoney[i-1]).text = realkakaoplayer[i-1].MONEY.toString()
                        v.findViewById<TextView>(arraytvname[i-1]).text = realkakaoplayer[i-1].NAME
                        Glide.with(v).load(realkakaoplayer[i-1].IMAGE).circleCrop().into(v.findViewById<ImageView>(arrayimage[i-1]))
                    }
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
        }
        tv_my_level = v.findViewById(R.id.tv_my_level)
        tv_my_nick =  v.findViewById(R.id.tv_my_nick)
        tv_my_stack = v.findViewById(R.id.tv_my_stack)
        tv_my_stack.text = profileDbManager!!.getMoney()!!.toString()
        tv_my_nick.text = profileDbManager!!.getNickname()!!
        tv_my_level.text = "레벨 " + profileDbManager!!.getLevel()!!.toString()
        v.findViewById<TextView>(R.id.tv_friend_invite).setOnClickListener{
            val dialog = Dialog_friend_invite(_MainActivity!!)
            dialog.start()
        }
        return v
    }

    fun friendsort(){
        if(realkakaoplayer!=null){
            realkakaoplayer.clear()
        }
        for (cnt in 1..friendlevel.size){
            if(friendlevel[cnt-1] != -1){
                var tmp : Dataclass_kakao = Dataclass_kakao("a", "b", "c", 0, 0, "d")
                tmp?.NAME = friendname[cnt-1];
                tmp?.NICKNAME = friendnick[cnt-1];
                tmp?.ID = friendid[cnt-1]
                tmp?.MONEY = friendmoney[cnt-1];
                tmp?.LEVEL = friendlevel[cnt-1]
                tmp?.IMAGE = friendimage[cnt-1]
                realkakaoplayer.add(tmp)
            }
        }

        //sort
        realkakaoplayer.sortByDescending { it.MONEY }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().runOnUiThread {
            Glide.with(_MainActivity!!).load(profileImageId[profileDbManager!!.getImageNum()!!]).circleCrop().into(requireView().findViewById(R.id.iv_my_image_local))
            Glide.with(_MainActivity!!).load(profileImageId[profileDbManager!!.getImageNum()!!]).circleCrop().into(requireView().findViewById(myLocalImageViewId))
        }
    }
}
