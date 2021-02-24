//package com.guys_from_301.stock_game
//
//import android.graphics.Color
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.guys_from_301.stock_game.data.ProfileDB
//import com.guys_from_301.stock_game.fragment.Fragment_ranking_kakao
//import com.guys_from_301.stock_game.fragment.Fragment_ranking_local
//import com.guys_from_301.stock_game.fragment.arrayimage
//import com.guys_from_301.stock_game.fragment.realkakaoplayer
//import com.guys_from_301.stock_game.retrofit.RetrofitRanking
//import com.kakao.sdk.user.UserApiClient
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class RankingActivity : AppCompatActivity() {
//
//    private lateinit var tv_local_ranking: TextView
//    private lateinit var tv_kakao_ranking: TextView
//    private lateinit var tv_my_level: TextView
//    private lateinit var tv_my_stack: TextView
//    private lateinit var tv_my_nick: TextView
//    private lateinit var tv_kakao_space : TextView
//    private lateinit var tv_local_space : TextView
//
//
//    //색깔
//    var coloron = "#F68A06"
//    var coloroff = "#FFFFFF"
//    var profileDb : ProfileDB? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_ranking)
//        friendsort()
//        profileDb = ProfileDB.getInstace(this)
//        if(profileDb?.profileDao()?.getLogin() != 4){
//            findViewById<ImageView>(R.id.iv_my_image).visibility=View.INVISIBLE
//        }
//        else{
//            UserApiClient.instance.me { user, error ->
//                if (error!=null)
//                    Toast.makeText(this,"사용자 정보 요청 실패(카카오)",Toast.LENGTH_SHORT)
//                else if (user!=null) {
//                    Glide.with(this).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop().into(findViewById<ImageView>(R.id.iv_my_image))
//                }
//            }
//        }
//        tv_local_ranking = findViewById(R.id.tv_localRanking)
//        tv_kakao_ranking = findViewById(R.id.tv_kakaoRanking)
//        tv_my_level =findViewById(R.id.tv_my_level)
//        tv_my_nick = findViewById(R.id.tv_my_nick)
//        tv_my_stack = findViewById(R.id.tv_my_stack)
//        tv_kakao_space = findViewById(R.id.tv_kakao_space)
//        tv_local_space = findViewById(R.id.tv_local_space)
//
//        tv_my_stack.text = profileDb?.profileDao()?.getMoney()!!.toString()+"   "
//        tv_my_nick.text = "   "+profileDb?.profileDao()?.getNickname()!!
//        tv_my_level.text = "   레벨 " + profileDb?.profileDao()?.getLevel()!!.toString()
//        tv_kakao_space.setBackgroundColor(Color.parseColor(coloroff))
//        tv_local_space.setBackgroundColor(Color.parseColor(coloroff))
//
//        if(profileDb?.profileDao()?.getLogin()!! != 4 ){
//            findViewById<TextView>(R.id.tv_kakaoRanking).visibility = View.GONE
//        }
//        tv_local_ranking.setOnClickListener{
//            tv_kakao_space.setBackgroundColor(Color.parseColor(coloroff))
//            tv_local_space.setBackgroundColor(Color.parseColor(coloron))
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fl_ranking, Fragment_ranking_local())
//                    .commit()
//        }
//
//        tv_kakao_ranking.setOnClickListener{
//            tv_kakao_space.setBackgroundColor(Color.parseColor(coloron))
//            tv_local_space.setBackgroundColor(Color.parseColor(coloroff))
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fl_ranking, Fragment_ranking_kakao())
//                    .commit()
//            if(realkakaoplayer == null || realkakaoplayer.size ==0) {
//                Toast.makeText(this, "친구를 초대해보세요!", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//    // 돈 많은 순서대로 정렬
//    fun friendsort(){
//        if(realkakaoplayer!=null){
//            realkakaoplayer.clear()
//        }
//        for (cnt in 1..friendlevel.size){
//            if(friendlevel[cnt-1] != -1){
//                var tmp : Dataclass_kakao = Dataclass_kakao("a", "b", 0, 0, "c")
//                friendlevel[cnt-1]
//                tmp?.NAME = friendname[cnt-1];
//                tmp?.NICKNAME = friendnick[cnt-1];
//                tmp?.MONEY = friendmoney[cnt-1];
//                tmp?.LEVEL = friendlevel[cnt-1]
//                tmp?.IMAGE = friendimage[cnt-1]
//                realkakaoplayer.add(tmp)
//            }
//        }
//        //sort
//        realkakaoplayer.sortByDescending { it.MONEY }
//    }
//
//}
//
//
