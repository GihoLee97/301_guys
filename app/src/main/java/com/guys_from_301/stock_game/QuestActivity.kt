package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guys_from_301.stock_game.data.ProfileDB
import com.guys_from_301.stock_game.data.Quest
import com.guys_from_301.stock_game.data.QuestDB
import kotlin.math.exp

class QuestActivity : AppCompatActivity() {

    private var questDb: QuestDB? = null
    private var profileDb: ProfileDB? = null
    private var questList = listOf<Quest>()
    private var achievementList = listOf<Int>()
    private var questInGameProfitRateList = listOf<Quest>()
    private var questRelativeProfitRateList = listOf<Quest>()
    private var questCumulativeList = listOf<Quest>()
    private var questPlayedGameList = listOf<Quest>()
    private var questInviteList = listOf<Quest>()
    private lateinit var tv_level : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

        questDb = QuestDB.getInstance(this)
        profileDb = ProfileDB.getInstace(this)
        tv_level = findViewById(R.id.tv_level)
        tv_level.text = "레벨 "+ profileDbManager!!.getLevel()!!
        var mAdapter = MyQuestAdapter(this, questList, achievementList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val qRecyclerView = findViewById<RecyclerView>(R.id.qRecyclerView)
        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
        var profitAchievement = 0
        var relativeProfitAchievement = 0
        var surplusAchievement = 0
        var exprienceAchievement = 0
        var invitationAchievemnet = 0
        findViewById<ImageButton>(R.id.ib_go_back).setOnClickListener{
            onBackPressed()
        }
        qRecyclerView.layoutManager = gridLayoutManager
        val r = Runnable {
            try{
                //성공한 도전과제 받아오기
                questInGameProfitRateList = questDb?.questDao()?.getQuestToAdapterByTheme("수익률")!!
                questRelativeProfitRateList = questDb?.questDao()?.getQuestToAdapterByTheme("시장대비 수익률")!!
                questCumulativeList = questDb?.questDao()?.getQuestToAdapterByTheme("연속 흑자")!!
                questPlayedGameList = questDb?.questDao()?.getQuestToAdapterByTheme("투자 경험")!!
                questInviteList = questDb?.questDao()?.getQuestToAdapterByTheme("초대하기")!!
                profitAchievement = 100 - questInGameProfitRateList.size * 10
                relativeProfitAchievement = 100 - questRelativeProfitRateList.size * 20
                surplusAchievement = 100 - questCumulativeList.size * 20
                exprienceAchievement = 100 - questPlayedGameList.size * 20
                invitationAchievemnet = 100 - questInviteList.size * 100
                if (questInGameProfitRateList.isEmpty()) {
                    questInGameProfitRateList = listOf(Quest(0, "수익률", "투자왕","모든 도전과제 달성", 1))
                    profitAchievement = 100
                }
                if (questRelativeProfitRateList.isEmpty()) {
                    questRelativeProfitRateList = listOf(Quest(0, "시장대비 수익률", "시장을 이기는 투자자","모든 도전과제 달성", 1))
                    relativeProfitAchievement = 100
                }
                if (questCumulativeList.isEmpty()) {
                    questCumulativeList = listOf(Quest(0, "연속 흑자", "거래왕","모든 도전과제 달성", 1))
                    surplusAchievement = 100
                }
                if (questPlayedGameList.isEmpty()) {
                    questPlayedGameList = listOf(Quest(0, "투자 경험", "노련한 투자자","모든 도전과제 달성", 1))
                    exprienceAchievement = 100
                }
                if (questInviteList.isEmpty()) {
                    questInviteList = listOf(Quest(0, "초대하기", "인맥왕","모든 도전과제 달성", 1))
                    invitationAchievemnet = 100
                }

                questList = listOf(questInGameProfitRateList.first(), questRelativeProfitRateList.first(), questCumulativeList.first(), questPlayedGameList.first(), questInviteList.first())
                achievementList = listOf(profitAchievement,relativeProfitAchievement,surplusAchievement,exprienceAchievement,invitationAchievemnet)

                mAdapter = MyQuestAdapter(this, questList, achievementList)
                mAdapter.notifyDataSetChanged()

                qRecyclerView.adapter = mAdapter
                val manager = GridLayoutManager(applicationContext, 2)
                qRecyclerView.setHasFixedSize(true)
                manager.reverseLayout = true
                manager.stackFromEnd = true
                qRecyclerView.layoutManager = manager
            }catch (e: Exception){
                Log.d("hongz", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}