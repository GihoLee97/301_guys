package com.guys_from_301.stock_game

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RankingActivity : AppCompatActivity() {

    private lateinit var btn_goback : Button
    private lateinit var user1_nick: TextView; private lateinit var user1_money: TextView
    private lateinit var user2_nick: TextView; private lateinit var user2_money: TextView
    private lateinit var user3_nick: TextView; private lateinit var user3_money: TextView
    private lateinit var user4_nick: TextView; private lateinit var user4_money: TextView
    private lateinit var user5_nick: TextView; private lateinit var user5_money: TextView
    private lateinit var user6_nick: TextView; private lateinit var user6_money: TextView
    private lateinit var user7_nick: TextView; private lateinit var user7_money: TextView
    private lateinit var user8_nick: TextView; private lateinit var user8_money: TextView
    private lateinit var user9_nick: TextView; private lateinit var user9_money: TextView
    private lateinit var user10_nick: TextView; private lateinit var user10_money: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        btn_goback = findViewById(R.id.btn_goback)
        user1_nick = findViewById(R.id.user1_nickname); user1_money = findViewById(R.id.user1_money)
        user2_nick = findViewById(R.id.user2_nickname); user2_money = findViewById(R.id.user2_money)
        user3_nick = findViewById(R.id.user3_nickname); user3_money = findViewById(R.id.user3_money)
        user4_nick = findViewById(R.id.user4_nickname); user4_money = findViewById(R.id.user4_money)
        user5_nick = findViewById(R.id.user5_nickname); user5_money = findViewById(R.id.user5_money)
        user6_nick = findViewById(R.id.user6_nickname); user6_money = findViewById(R.id.user6_money)
        user7_nick = findViewById(R.id.user7_nickname); user7_money = findViewById(R.id.user7_money)
        user8_nick = findViewById(R.id.user8_nickname); user8_money = findViewById(R.id.user8_money)
        user9_nick = findViewById(R.id.user9_nickname); user9_money = findViewById(R.id.user9_money)
        user10_nick = findViewById(R.id.user10_nickname); user10_money = findViewById(R.id.user10_money)

        user1_nick.text = rank1_nick; user1_money.text = rank1_money
        user2_nick.text = rank2_nick; user2_money.text = rank2_money
        user3_nick.text = rank3_nick; user3_money.text = rank3_money
        user4_nick.text = rank4_nick; user4_money.text = rank4_money
        user5_nick.text = rank5_nick; user5_money.text = rank5_money
        user6_nick.text = rank6_nick; user6_money.text = rank6_money
        user7_nick.text = rank7_nick; user7_money.text = rank7_money
        user8_nick.text = rank8_nick; user8_money.text = rank8_money
        user9_nick.text = rank9_nick; user9_money.text = rank9_money
        user9_nick.text = rank9_nick; user9_money.text = rank9_money
        user10_nick.text = rank10_nick; user10_money.text = rank10_money


        btn_goback.setOnClickListener{
            onBackPressed()
        }
    }
}


