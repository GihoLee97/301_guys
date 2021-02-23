package com.guys_from_301.stock_game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.ProfileDB
import com.guys_from_301.stock_game.fragment.Fragment_ranking_kakao
import com.guys_from_301.stock_game.fragment.Fragment_ranking_local
import com.guys_from_301.stock_game.fragment.realkakaoplayer
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RankingActivity : AppCompatActivity() {

    private lateinit var tv_local_ranking: TextView
    private lateinit var tv_kakao_ranking:TextView

    var profileDb : ProfileDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        friendsort()
        profileDb = ProfileDB.getInstace(this)
        tv_local_ranking = findViewById(R.id.tv_localRanking)
        tv_kakao_ranking = findViewById(R.id.tv_kakaoRanking)
        if(profileDb?.profileDao()?.getLogin()!! != 4 ){
            findViewById<TextView>(R.id.tv_kakaoRanking).visibility = View.GONE
        }
        tv_local_ranking.setOnClickListener{
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_ranking, Fragment_ranking_local())
                    .commit()
        }

        tv_kakao_ranking.setOnClickListener{
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_ranking, Fragment_ranking_kakao())
                    .commit()
            if(realkakaoplayer == null || realkakaoplayer.size ==0) {
                Toast.makeText(this, "친구를 초대해보세요!", Toast.LENGTH_LONG).show()
            }
        }
    }
    // 돈 많은 순서대로 정렬
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


