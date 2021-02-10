package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import kotlin.experimental.and

class Dialog_level(context: Context) {
    var mContext: Context = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnOK : Button
    private lateinit var textexp : TextView
    private lateinit var textlevel : TextView

    val intent = Intent(mContext, MainActivity::class.java)

    private var profileDb: ProflieDB? = null
    fun start(){
        profileDb = ProflieDB.getInstace(mContext)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_level) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        btnOK = dlg.findViewById(R.id.btn_levelok)
        textexp = dlg.findViewById(R.id.exp)
        textlevel = dlg.findViewById(R.id.level)

        textexp.text = profileDb?.profileDao()?.getExp()!!.toString()
        textlevel.text = profileDb?.profileDao()?.getLevel()!!.toString()
        if(islevelup){
            Toast.makeText(mContext, "레벨 업!", Toast.LENGTH_LONG).show()
            islevelup = false
        }
        btnOK.setOnClickListener {
            dlg.dismiss()
            mContext.startActivity(intent)
        }
        dlg.show()
    }
}