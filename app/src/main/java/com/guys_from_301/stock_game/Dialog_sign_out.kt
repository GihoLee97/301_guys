package com.guys_from_301.stock_game

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSetDB
import com.guys_from_301.stock_game.data.QuestDB
import com.guys_from_301.stock_game.retrofit.RetrofitDelete
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_sign_out(context: Context, method: String) {
    var mContext: Context = context
    var loginMethod : String = method
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btn_cancel: Button
    private lateinit var btn_sign_out: Button
    private var gameNormalDb: GameNormalDB? = null
    private var gameSetDb: GameSetDB? = null
    private var questDb: QuestDB? = null

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_sign_out) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_cancel = dlg.findViewById(R.id.btn_cancel)
        btn_sign_out = dlg.findViewById(R.id.btn_sign_out)

        gameNormalDb = GameNormalDB.getInstace(dlg.context)
        gameSetDb = GameSetDB.getInstace(dlg.context)
        questDb = QuestDB.getInstance(dlg.context)


        btn_cancel.setOnClickListener {
            dlg.dismiss()
        }

        btn_sign_out.setOnClickListener {
            updatelogOutInFo2DB(loginMethod)
            gameSetDb?.gameSetDao()?.deleteSignOut()
            gameNormalDb?.gameNormalDao()?.deleteSignOut()
            questDb?.questDao()?.deleteSignOut()
            val intent = Intent(mContext,NewInitialActivity::class.java)
            mContext?.startActivity(intent)
            (mContext as Activity).finishAffinity()
        }

        dlg.show()
    }



    private fun updatelogOutInFo2DB(method : String){
        var loginMethod = 0
        if(method=="GENERAL") loginMethod = 1
        else if(method=="GOOGLE") loginMethod = 2
        else if(method=="KAKAO") loginMethod = 4
        if(!profileDbManager!!.isEmpty(_MainActivity!!)) {
            profileDbManager!!.setLogin(profileDbManager!!.getLogin()!!-loginMethod)
        }
    }
}