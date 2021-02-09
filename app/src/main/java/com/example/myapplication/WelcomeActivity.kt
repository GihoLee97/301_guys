package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.util.Log
import android.view.GestureDetector
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.invoke.ConstantCallSite

class WelcomeActivity : AppCompatActivity() {
    var mContext: Context = this
    private val welcomeViewModel: WelcomViewModel by viewModels()
    private val profileActivityViewModel = ProfileActivityViewModel(this)
    private lateinit var mGestureDetector : GestureDetectorCompat
    private var click = 0
    private var step = "Welcome"

    private lateinit var layout_welcome : ConstraintLayout
    private lateinit var layout_welcome1 : ConstraintLayout
    private lateinit var layout_welcome2 : ConstraintLayout
    private lateinit var layout_welcome3 : LinearLayout
    private lateinit var layout_welcome4 : LinearLayout
    private lateinit var editText_nicknameWelcome : EditText
    private lateinit var btn_nicknameOkWelcome : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        mGestureDetector = GestureDetectorCompat(this,GestureListener())

        layout_welcome = findViewById(R.id.Layout_welcome)
        layout_welcome1 = findViewById(R.id.Layout_welcome1)
        layout_welcome2 = findViewById(R.id.Layout_welcome2)
        layout_welcome3 = findViewById(R.id.Layout_welcome3)
        layout_welcome4 = findViewById(R.id.Layout_welcome4)
        editText_nicknameWelcome = findViewById(R.id.editText_NicknameWelcome)
        btn_nicknameOkWelcome = findViewById(R.id.btn_nicknameOkWelcome)


        layout_welcome.visibility = View.VISIBLE
        layout_welcome1.visibility = View.VISIBLE
        layout_welcome2.visibility = View.GONE
        layout_welcome3.visibility = View.GONE
        layout_welcome4.visibility = View.GONE

        val clickObserver = Observer<Int> { click ->
            // Update the UI, in this case, a TextView.
            if(step == "Welcome"){
                layout_welcome1.visibility = View.GONE
                layout_welcome2.visibility = View.VISIBLE
                step = "Introduction"
            } else if(step == "Introduction"){
                layout_welcome2.visibility = View.GONE
                layout_welcome3.visibility = View.VISIBLE
                step = "Nickname Setting"
            } else if(step == "Nickname Setting Complished") {
                layout_welcome3.visibility = View.GONE
                layout_welcome4.visibility = View.VISIBLE
                step = "Welcome complished"
            } else if(step == "Welcome complished"){
                //TODO
                Toast.makeText(this, "최초 접속 기념 지급!\n현금: 100만원\n아이템1: 5개", Toast.LENGTH_LONG).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }

        welcomeViewModel.currClick.observe(this,clickObserver)

        btn_nicknameOkWelcome.setOnClickListener {
            var profileDb: ProflieDB? = null
            profileDb = ProflieDB?.getInstace(this)
            var inputStr = editText_nicknameWelcome.text.toString()
            //TODO
            var login_id: String = profileDb?.profileDao()?.getLoginid()!!
            var login_pw: String = profileDb?.profileDao()?.getLoginpw()!!
            var nickname: String = inputStr.trim()
            var history: String = profileDb?.profileDao()?.getHistory()!!
            var level: Int = profileDb?.profileDao()?.getLevel()!!
            var money :Int = 1000000
            var profit: Int = profileDb?.profileDao()?.getProfit()!!
            var funnickcheck: RetrofitNickcheck? = null
            val url = "http://stockgame.dothome.co.kr/test/nickcheck.php/"

            if (inputStr.trim() == "" || inputStr.toString().trim() == null) {
                Toast.makeText(mContext, "닉네임을 입력하세요!", Toast.LENGTH_LONG).show()
            }
            else {
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
                funnickcheck = retrofit.create(RetrofitNickcheck::class.java)
                funnickcheck.nickcheck(getHash(login_id), nickname).enqueue(object :
                    Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        println("---fail")
                    }
                    override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                        if (response.isSuccessful && response.body() != null) {
                            val okcode = response.body()!!
                            if (okcode == "444") {
                                Toast.makeText(mContext, "사용중인 닉네임입니다.", Toast.LENGTH_LONG).show()
                            }
                            if (okcode == "555") {
                                Toast.makeText(mContext, "닉네임이 확정되었습니다.", Toast.LENGTH_LONG).show()
                                var profileDb: ProflieDB? = null
                                profileDb = ProflieDB?.getInstace(mContext!!)
                                val newProfile = Profile()
                                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                                newProfile.nickname = inputStr.trim()
                                newProfile.money = money
                                newProfile.history = profileDb?.profileDao()?.getHistory()!!
                                newProfile.level = profileDb?.profileDao()?.getLevel()!!
                                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                                newProfile.profit = profileDb?.profileDao()?.getProfit()!!
                                newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                                newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                                profileDb?.profileDao()?.update(newProfile)
                                update(getHash(login_id).toString().trim(), getHash(login_pw).toString().trim(), money, 5, 4, 4, nickname, profit, history, level)

                                //
                                profileActivityViewModel.setnWriteNickname(inputStr)
                                step = "Nickname Setting Complished"
                                click += 1
                                welcomeViewModel.click(click)

                            }
                            if(okcode =="666"){
                                // 입력한 닉네임이 현재 닉네임과 일치하는 경우
                            }
                        }
                    }
                })

            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?) : Boolean{
        mGestureDetector.onTouchEvent(event)
        val action : Int = MotionEventCompat.getActionMasked(event)
        val DEBUG_TAG = "Giho"
        when(action){
            MotionEvent.ACTION_DOWN -> {
                Log.d(DEBUG_TAG, "Action was DOWN")
                click()
                true
            }
            else -> super.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private fun click(){
        Log.d("Giho",click.toString())
        welcomeViewModel.click(click)
        click+=1
    }

    private class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.d("Giho","SingleTapUp")
            return super.onSingleTapUp(e)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            Log.d("Giho","Down")
            return super.onDown(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.d("Giho","Fling")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d("Giho","DoubleTap")
            return super.onDoubleTap(e)
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.d("Giho","onScroll")
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onContextClick(e: MotionEvent?): Boolean {
            Log.d("Giho","SingleTapUp")
            return super.onContextClick(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.d("Giho","SingleTapConfirmed")
            return super.onSingleTapConfirmed(e)
        }

        override fun onShowPress(e: MotionEvent?) {
            Log.d("Giho","ShowPress")
            super.onShowPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            Log.d("Giho","DoubleTapEvent")
            return super.onDoubleTapEvent(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            Log.d("Giho","LongPress")
            super.onLongPress(e)
        }
    }

    override fun onRestart() {
        profileActivityViewModel.refresh()
        super.onRestart()
    }

    override fun onStop() {
        profileActivityViewModel.write2database()
        super.onStop()
    }
}

