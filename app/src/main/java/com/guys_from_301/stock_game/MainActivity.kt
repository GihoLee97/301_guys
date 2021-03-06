package com.guys_from_301.stock_game

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.*
import com.guys_from_301.stock_game.retrofit.RetrofitFriend
import com.guys_from_301.stock_game.retrofit.RetrofitRanking
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

var accountID: String? = null
var _MainActivity : Context? = null
const val START_CASH = 10000F
const val START_MONTHLY = 1000F
const val START_SALARY_RAISE = 6F
const val START_GAME_LENGTH = 20
const val START_GAME_SPEED = 3
//푸쉬알람 ID
const val ONESIGNAL_APP_ID = "b096a9b0-2d91-48b5-886b-b3e8d6ba0116"
var rank1_nick :String = ""; var rank1_money :String = ""; var rank2_nick :String = ""; var rank2_money :String = ""
var rank3_nick :String = ""; var rank3_money :String = ""; var rank4_nick :String = ""; var rank4_money :String = ""
var rank5_nick :String = ""; var rank5_money :String = ""; var rank6_nick :String = ""; var rank6_money :String = ""
var rank7_nick :String = ""; var rank7_money :String = ""; var rank8_nick :String = ""; var rank8_money :String = ""
var rank9_nick :String = ""; var rank9_money :String = ""; var rank10_nick :String = ""; var rank10_money :String = ""
// ranker image
var ranker_image = mutableListOf<Int>()
var ranker_stack = mutableListOf<Int>()
//gameset update 여부
var updateGameSet: Boolean = false
var startGameSet: Boolean = false

var friend_all_array : Friends<Friend>? = null

var Notice_array : MutableList<DATACLASS_NOTICE>? = null

var my_name: String = ""; var my_image: String = ""
class MainActivity : AppCompatActivity() {
    // fragment 관련
    val manager = supportFragmentManager
    private var gameSetDb: GameSetDB? = null
    var itemDb: ItemDB? = null
    val mContext: Context = this
//    var profileDb : ProfileDB? = null
    var questDb: QuestDB? = null
    private var tradedayQuestList:List<Quest>? = null
    private var countGameQuestList:List<Quest>? = null
    private lateinit var btn_profile : Button
    private lateinit var btn_setting : Button
    private lateinit var btn_quest: Button
    private lateinit var btn_game : Button
    private lateinit var btn_market : Button
    private lateinit var btn_ranking: Button
    private lateinit var btn_friend: Button
    private lateinit var btn_sendKakaoMessageMyself: Button
    private lateinit var btn_sendKakaoMessageToOthers: Button
    private lateinit var btn_shareText: Button
    private lateinit var btn_pushAlarm: Button
    private lateinit var btn_captureViewAndShareWithKakao: Button
    private lateinit var btn_captureViewAndShareWithOthers: Button

    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("hongz","MainActivity 시작")
        itemDb = ItemDB?.getInstace(mContext!!)
//        profileDb = ProfileDB?.getInstace(mContext!!)
        questDb = QuestDB.getInstance(mContext!!)
        tradedayQuestList = questDb?.questDao()?.getQuestByTheme("누적 거래일")
        countGameQuestList = questDb?.questDao()?.getQuestByTheme("투자 경험")
        countGameQuestList = questDb?.questDao()?.getQuestByTheme("게임 플레이하기")
        _MainActivity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //gamesetviewmodel

        var t : BottomNavigationView = findViewById(R.id.main_bottom_navigation)
        NavigationUI.setupWithNavController(t, findNavController(R.id.nav_host))
        gameSetDb = GameSetDB.getInstace(this)
        // 광고 관련 코드
//        mAdView = findViewById(R.id.adView)
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)

        // 기존 알림 삭제
        cancelAlarm()

        //초기자산값 변수
        val initialgameset = gameSetDb?.gameSetDao()?.getSetWithId(accountID+0, accountID!!)


        //게임 저장시 gameset 업데이트
        if(updateGameSet){
//            val dialog = Dialog_loading(this@MainActivity)
//            dialog.show()
            var localDateTime = LocalDateTime.now()
            var newGameSet = gameSetDb?.gameSetDao()?.getSetWithId(setId, accountID!!)
            if (newGameSet != null) {
                newGameSet.endtime = localDateTime.toString()
                newGameSet.profitrate = profitrate
                gameSetDb?.gameSetDao()?.insert(newGameSet)
                Log.d("hongz", "MainActivity: gameset 업데이트")
            }
            updateGameSet = false
        }
        //새로운 빈 게임 생성
        var gamesetList = gameSetDb?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)
        var existence = false   //새 게임 존재 여부
        for (game in gameSetDb?.gameSetDao()?.getPick(accountID!!,accountID!!+1,accountID!!+2,accountID!!+3)!!) {
            var emptyCount : Int = 0
            if (game.endtime == "") {
                existence = true
                emptyCount++
            }
            if(emptyCount == 2) gameSetDb?.gameSetDao()?.delete(game)
        }
        if (gamesetList != null) {
            if (!existence && gamesetList.size < 3) {
                val newGameSet = GameSet()

                if (gamesetList.size == 1) {
                    if (gamesetList.last().id.last() == '1') newGameSet.id = initialgameset?.accountId+2
                    else if (gamesetList.last().id.last() == '2') newGameSet.id = initialgameset?.accountId+3
                    else if (gamesetList.last().id.last() == '3') newGameSet.id = initialgameset?.accountId+1
                    else newGameSet.id = "예외1"
                } else if (gamesetList.size == 2) {
                    if (gamesetList[0].id.last().toInt() + gamesetList[1].id.last().toInt() == '1'.toInt()+'2'.toInt()) newGameSet.id = initialgameset?.accountId+3
                    else if (gamesetList[0].id.last().toInt() + gamesetList[1].id.last().toInt() == '1'.toInt()+'3'.toInt()) newGameSet.id = initialgameset?.accountId+2
                    else if (gamesetList[0].id.last().toInt() + gamesetList[1].id.last().toInt() == '3'.toInt()+'2'.toInt()) newGameSet.id = initialgameset?.accountId+1
                    else newGameSet.id = "예외2"
                } else if (gamesetList.size == 0){
                    newGameSet.id = accountID+1
                }
                //예전거 누적
                if (initialgameset != null) {
                    val id = newGameSet.id
                    newGameSet.setcash = initialgameset.setcash
                    newGameSet.setmonthly = initialgameset.setmonthly
                    newGameSet.setsalaryraise = initialgameset.setsalaryraise
                    newGameSet.setgamespeed = initialgameset.setgamespeed
                    newGameSet.setgamelength = initialgameset.setgamelength
                    newGameSet.accountId = initialgameset.accountId
                    Log.d("hongz", "MainActivity: 새gameset 추가 [id]: "+id)
                }
                newGameSet.endtime = ""
                gameSetDb?.gameSetDao()?.insert(newGameSet)
            }
        }




        //TODO!!!!!!!!! 카카오 뒤짐....
        //내 정보
        UserApiClient.instance.me { user, error ->
            if (error!=null)
                Toast.makeText(_MainActivity,"사용자 정보 요청 실패(카카오)", Toast.LENGTH_SHORT)
            else if (user!=null) {
                my_image = user?.kakaoAccount?.profile?.thumbnailImageUrl!!
                my_name = user?.kakaoAccount?.profile?.nickname!!
            }
        }
        //친구 정보 코드
        TalkApiClient.instance.profile { profile, error ->
            if (error != null) {
                println("카카오톡 실패??"+error)
            }
            else if (profile != null) {
                println("카카오톡 성공"+profile.nickname+" "+profile.countryISO)
            }
        }
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                println("카카오톡 친구 목록 실패"+error)
            }
            else if (friends != null) {
                if(friend_all_array != null){
                    friend_all_array = null
                }
                friend_all_array = friends
                if(friendlevel != null){
                    friendid.clear()
                    friendmoney.clear()
                    friendlevel.clear()
                    friendnick.clear()
                    friendname.clear()
                    frienduuid.clear()
                    friendimage.clear()
                }
                val friendscount = friends.totalCount
                var count = 0
                while(count<friendscount){
                    friendid.add(getHash((friends.elements[count].id).toString()))
                    friendname.add(friends.elements[count].profileNickname)
                    frienduuid.add(friends.elements[count].uuid)
                    friendimage.add(friends.elements[count].profileThumbnailImage)
                    count++
                }
                friendInfo(friendid, friendscount)
            }
        }

        // 피로도 저감 코드
        CoroutineScope(Dispatchers.IO).launch {
            val job1 = launch {
                value1reduc()
            }
        }


//        btn_profile = findViewById(R.id.btn_profile)
//        btn_setting = findViewById(R.id.btn_setting)
//        btn_quest = findViewById(R.id.btn_quest)
//        btn_game =  findViewById(R.id.btn_game)
//        btn_market = findViewById(R.id.btn_market)
//        btn_ranking = findViewById(R.id.btn_ranking)
//        btn_friend = findViewById(R.id.btn_friend)
//        btn_sendKakaoMessageMyself = findViewById(R.id.btn_sendKakaoMessageMyself)
//        btn_sendKakaoMessageToOthers = findViewById(R.id.btn_sendKakaoMessageToOthers)
//        btn_shareText = findViewById(R.id.btn_shareText)
//        btn_pushAlarm = findViewById(R.id.btn_pushAlarm)
//        btn_captureViewAndShareWithKakao = findViewById(R.id.btn_captureViewAndShareWithKakao)
//        btn_captureViewAndShareWithOthers = findViewById(R.id.btn_captureViewAndShareWithOthers)
     //   btn_game.isEnabled = false // 로딩 미완료 상태일 때 게임 버튼 비활성화


//
//        //업적 달성하기 tmp
//        findViewById<Button>(R.id.btn_quest_tmp).setOnClickListener{
//            val dialog = Dialog_quest(this@MainActivity)
//            dialog.start()
//        }
//        //
//
//        // 카카오 로그인 시에만 친구 창 뜨게 하기
//        if(profileDb?.profileDao()?.getLogin()==4){
//            btn_friend.visibility = View.VISIBLE
//        }
//
//        btn_friend.setOnClickListener{
//            val intent = Intent(this,FriendActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        btn_profile.setOnClickListener{
//            val intent = Intent(this,ProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        btn_setting.setOnClickListener {
//            val intent = Intent(this,SettingActivity::class.java)
//            startActivity(intent)
//        }
//
//        btn_quest.setOnClickListener {
//            val intent = Intent(this, QuestActivity::class.java)
//            startActivity(intent)
//        }
//
//        btn_game.setOnClickListener{
//            var profileDb: ProfileDB? = null
//            profileDb = ProfileDB.getInstace(this)
//            var money = profileDb?.profileDao()?.getMoney()!!
//
//            if(money <= 0){
//                Toast.makeText(this, "현금이 없습니다.", Toast.LENGTH_LONG).show()
//            }
//            else{
//                val dialog = Dialog_loading(this@MainActivity)
//                dialog.show()
//                val intentgame = Intent(this, PickGameActivity::class.java)
//                val intent = Intent(this,GameNormalActivity::class.java)
//                if(gameSetDb?.gameSetDao()?.getAll()?.isEmpty() == true)    {
//                    val addRunnable = Runnable {
//                        val newGameSetDB = GameSet()
//                        setId = 1
//                        newGameSetDB.id = 1
//                        newGameSetDB.setcash = START_CASH
//                        newGameSetDB.setgamelength = START_GAME_LENGTH
//                        newGameSetDB.setgamespeed = START_GAME_SPEED
//                        newGameSetDB.setmonthly = START_MONTHLY
//                        newGameSetDB.setsalaryraise = START_SALARY_RAISE
//                        gameSetDb?.gameSetDao()?.insert(newGameSetDB)
//                    }
//                    val addThread = Thread(addRunnable)
//                    addThread.start()
//                    startActivity(intent)
//                    dialog.dismiss()
//                }
//                else {
////                    setCash = gameSetDb?.gameSetDao()?.getSetCash()!!
////                    setMonthly = gameSetDb?.gameSetDao()?.getSetMonthly()!!
////                    setSalaryraise = gameSetDb?.gameSetDao()?.getSetSalaryRaise()!!
////                    setGamespeed = gameSetDb?.gameSetDao()?.getSetGameSpeed()!!
//                    startActivity(intentgame)
//                    dialog.dismiss()
//                }
//            }
//        }
//
//        btn_market.setOnClickListener {
//            val intent = Intent(this,MarketActivity::class.java)
//            startActivity(intent)
//        }
//        btn_ranking.setOnClickListener{
//            val intent = Intent(this, RankingActivity::class.java)
//            startActivity(intent)
//        }
//        btn_sendKakaoMessageMyself.setOnClickListener {
//            kakaoMessageManager.sendMessageMyself()
//        }
//        btn_sendKakaoMessageToOthers.setOnClickListener {
//            kakaoMessageManager.sendMessage()
//        }
//        btn_shareText.setOnClickListener {
//            shareManager.shareText(this,"텍스트 공유하기 성공~")
//        }
//        btn_pushAlarm.setOnClickListener {
//            pushAlarmManager.generateAndPushAlarm(this)
//        }
//        btn_captureViewAndShareWithKakao.setOnClickListener {
//            val path = captureUtil.captureAndSaveViewWithKakao(findViewById(R.id.textView2))
//            shareManager.shareBinaryWithKakao(this,path)
//        }
//        btn_captureViewAndShareWithOthers.setOnClickListener {
//            val uri = captureUtil.captureAndSaveView(findViewById(R.id.textView2))
//            shareManager.shareBinaryWithOthers(this,uri.toString())
//        }
//
//        findViewById<TextView>(R.id.tv_value1).text = "피로도: "+profileDb?.profileDao()?.getValue1()!!
//        findViewById<ProgressBar>(R.id.progress_value1).progress = profileDb?.profileDao()?.getValue1()!!
//
//
//        while (true) {
//            if (loadcomp) {
//                btn_game.isEnabled = true
//                btn_game.text = "게임"
//                break
//            }
//            Thread.sleep(50)
//        }
//        ranking("a")
    }

    override fun onStart() {
        super.onStart()
        ranking("a")
    }
    // 두번 누르면 종료되는 코드
    var time3: Long = 0
    override fun onBackPressed() {
        // 서버에 올리는 코드(피로도)
        update(getHash(profileDbManager!!.getLoginId()!!).trim(),
                getHash(profileDbManager!!.getLoginPw()!!).trim(),
                profileDbManager!!.getMoney()!!,
                profileDbManager!!.getValue1()!!,
                profileDbManager!!.getNickname()!!,
                profileDbManager!!.getProfitRate()!!,
                profileDbManager!!.getRelativeProfit()!!,
                profileDbManager!!.getRoundCount()!!,
                profileDbManager!!.getHistory()!!,
                profileDbManager!!.getLevel()!!
        )
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            // 푸시알람설정
            setAlarm()
            // 이거 3줄 다 써야 안전하게 종료
            moveTaskToBack(true)
            finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 랭킹 받아오는 코드

    fun ranking(u_id: String) {
        var funranking: RetrofitRanking? = null
        val url = "http://stockgame.dothome.co.kr/test/ranking.php/"
        var gson: Gson = GsonBuilder()
                .setLenient()
                .create()
        //creating retrofit object
        var retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
        //creating our api
        funranking = retrofit.create(RetrofitRanking::class.java)
        funranking.funranking(u_id).enqueue(object : Callback<Array<String>> {
            override fun onFailure(call: Call<Array<String>>, t: Throwable) {
                //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Array<String>>, response: retrofit2.Response<Array<String>>) {
                if (response.isSuccessful && response.body() != null) {
                    rank1_nick = response.body()!![0]; rank1_money = response.body()!![10]
                    rank2_nick = response.body()!![1]; rank2_money = response.body()!![11]
                    rank3_nick = response.body()!![2]; rank3_money = response.body()!![12]
                    rank4_nick = response.body()!![3]; rank4_money = response.body()!![13]
                    rank5_nick = response.body()!![4]; rank5_money = response.body()!![14]
                    rank6_nick = response.body()!![5]; rank6_money = response.body()!![15]
                    rank7_nick = response.body()!![6]; rank7_money = response.body()!![16]
                    rank8_nick = response.body()!![7]; rank8_money = response.body()!![17]
                    rank9_nick = response.body()!![8]; rank9_money = response.body()!![18]
                    rank10_nick = response.body()!![9]; rank10_money = response.body()!![19]

                    if(ranker_image != null){
                        ranker_image.clear()
                    }
                    if(ranker_stack != null){
                        ranker_stack.clear()
                    }
                    for(i in 0..9){
                        ranker_image.add(response.body()!![20+i].toInt())
                        ranker_stack.add(response.body()!![30+i].toInt())
                    }
                    Log.d("sw---", "---stack"+ ranker_stack[0].toString())
                }
            }
        })
    }

    private var value1ForWrite = 0

    // 피로도 저감 코드
    suspend fun value1reduc() {
        while (true) {
            var lasttime = itemDb?.itemDao()?.getLasttime()!! // 마지막 피로도 저감 시간
            var nowtime = System.currentTimeMillis() // 현재 시간

            if ((nowtime - lasttime) >= 1000L) {
                var nowvalue1 = profileDbManager!!.getValue1()!!
                var value1temp = 1 * (((nowtime - lasttime)/1000L).toFloat()).roundToInt() // 초당 1 씩 저감

                if ((nowvalue1 - value1temp) <= 0 ) {
                    nowvalue1 = 0
                } else {
                    nowvalue1 -= value1temp
                }

//                // DB 피로도 업데이트
//                profileDbManager!!.setValue1(nowvalue1)
                value1ForWrite = nowvalue1

                // 피로도 저감 시간 저장
                val newItem = Item()
                newItem.id = itemDb?.itemDao()!!.getId()
                newItem.lasttime = nowtime
                newItem.potion = itemDb?.itemDao()?.getAll()?.get(0)?.potion ?: 0
                itemDb?.itemDao()?.update(newItem)
            }
            delay(200L) // 0.2초에 한 번씩 확인
        }
    }

    // 친구 정보 받아오는 코드
    fun friendInfo(u_id: MutableList<String>, u_number :Int) {
        var funfriend: RetrofitFriend? = null
        val url = "http://stockgame.dothome.co.kr/test/friendrank.php/"
        var gson: Gson = GsonBuilder()
                .setLenient()
                .create()
        //creating retrofit object
        var retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
        //creating our api
        funfriend = retrofit.create(RetrofitFriend::class.java)
        funfriend.funfriend(u_id, u_number).enqueue(object : Callback<MutableList<DATACLASS>> {
            override fun onFailure(call: Call<MutableList<DATACLASS>>, t: Throwable) {
                println("---실패: "+t.message)
            }

            override fun onResponse(call: Call<MutableList<DATACLASS>>, response: retrofit2.Response<MutableList<DATACLASS>>) {
                if (response.isSuccessful && response.body() != null) {
                    val data: MutableList<DATACLASS> = response.body()!!
                    println("---성공")
                    for(i in 0..u_number-1){
                        if(response.body()!![i]==null){
                            friendmoney.add(-1f)
                            friendlevel.add(-1)
                            friendnick.add("존재하지 않는 아이디")
                        }
                        else{
                            friendmoney.add(data[i].RELATIVEPROFITRATE)
                            friendlevel.add(data[i].LEVEL)
                            friendnick.add(data[i].NICKNAME)
                        }
                    }
                    friendid.add(getHash(profileDbManager!!.getLoginId()!!))
                    friendname.add(my_name)
                    friendnick.add(profileDbManager!!.getNickname()!!)
                    friendmoney.add(profileDbManager!!.getRelativeProfit()!!)
                    friendlevel.add(profileDbManager!!.getLevel()!!)
                    friendimage.add(my_image!!)
                }
            }
        })
    }

    //도전과제 스텍 보상 함수
    fun rewardByStack(reward: Int) {
        if (!profileDbManager!!.isEmpty(this)) {
            var temp = profileDbManager!!.getMoney()!!
            if (temp >= 2000000000) {
                profileDbManager!!.setMoney(temp)
            } else {
                profileDbManager!!.setMoney(temp + reward)
                temp = profileDbManager!!.getMoney()!!
                if (temp < 0) {
                    profileDbManager!!.setMoney(0)
                }
            }
        }
    }

    //도전과제 확인 함수(누적거래일, 완료한 게임수)
    fun checkQuestCumulativeTradingDay(tradeday:Int, questList: List<Quest>){
        for(i in 0 .. questList.size-1){
            if(questList?.get(i)?.achievement  == 0){
                when(i){
                    0-> if(tradeday>=10000){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(10000)
                        questAchieved.add(questList[i])
                    }
                    1-> if(tradeday>=20000){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(20000)
                        questAchieved.add(questList[i])
                    }
                    2-> if(tradeday>=30000){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(30000)
                        questAchieved.add(questList[i])
                    }
                    3-> if(tradeday>=50000){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(50000)
                        questAchieved.add(questList[i])
                    }
                    4-> if(tradeday>=100000){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(100000)
                        questAchieved.add(questList[i])
                    }

                }
            }
        }
    }

    fun checkQuestPlayedGame( countGame: Int, questList: List<Quest>){
        for(i in 0 .. questList.size-1){
            if(questList?.get(i)?.achievement  == 0){
                when(i){
                    0-> if(countGame>=1){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(1000)
                        questAchieved.add(questList[i])
                    }
                    1-> if(countGame>=5){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(5000)
                        questAchieved.add(questList[i])
                    }
                    2-> if(countGame>=10){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(10000)
                        questAchieved.add(questList[i])
                    }
                    3-> if(countGame>=50){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(50000)
                        questAchieved.add(questList[i])
                    }
                    4-> if(countGame>=100){
                        questList?.get(i)?.achievement = 1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questList?.get(i)?.let { questDb?.questDao()?.insert(it) }
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(100000)
                        questAchieved.add(questList[i])
                    }
                }
            }
        }
    }

    fun setAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)  // 1
        val pendingIntent = PendingIntent.getBroadcast(
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTime = (SystemClock.elapsedRealtime()
                + 3*24*60*60*1000)
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    fun cancelAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)  // 1
        val pendingIntent = PendingIntent.getBroadcast(
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }


    override fun onResume() {
        super.onResume()

        if(profileDbManager==null){
            profileDbManager = ProfileDbManager(this)
        }
        else{
            profileDbManager!!.refresh(this)
        }

        //누적거래일, 완료한 게임수 관련 도전과제 성공여부 확인
        profileDbManager!!.getHistory()?.let { tradedayQuestList?.let { it1 ->
            checkQuestCumulativeTradingDay(it,
                it1
            )
        } }
        profileDbManager!!.getRoundCount()?.let { countGameQuestList?.let { it1 ->
            checkQuestPlayedGame(it,
                it1
            )
        } }
        var textView1 = findViewById<TextView>(R.id.textView2)

        Log.d("hongz","MainActivity: 도전과제 다이얼로그 생성 전")
        if(questAchieved.isEmpty() == false){
            Log.d("hongz","MainActivity: 도전과제 다이얼로그 생성")
            for(i in 0..questAchieved.size-1){
                var quest = questAchieved[i]
                val dlgQuest = Dialog_new_quest(_MainActivity!!)
                dlgQuest.start(quest)
            }
            questAchieved = arrayListOf()
        }

        eventCount = 0
        Log.d("hongz","MainActivity: eventCount="+ eventCount)


    }

    override fun onStop() {
        super.onStop()
        // DB 피로도 업데이트
        profileDbManager!!.setValue1(value1ForWrite)
        profileDbManager!!.write2database()

        val CAPTURE_PATH = "/Tardis"
        val strFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+CAPTURE_PATH

        val file = File(strFolderPath)
        if(file.exists()) {
            file.deleteRecursively()
            Log.d("Giho","delete files")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}
