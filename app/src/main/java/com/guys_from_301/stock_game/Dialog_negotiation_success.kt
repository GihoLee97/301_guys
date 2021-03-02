package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.GameSetDB
import com.guys_from_301.stock_game.data.Item
import com.guys_from_301.stock_game.data.ItemDB
import com.guys_from_301.stock_game.data.ProfileDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_negotiation_success(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var tv_before: TextView
    private lateinit var tv_after: TextView
    private lateinit var btn_close: Button
    private lateinit var profileDB: ProfileDB
    private lateinit var gamesetDB : GameSetDB
    private lateinit var itemDB : ItemDB

    fun start(theme:Int, step:Int){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_negotiation_success)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        profileDB = ProfileDB.getInstace(mContext!!)!!
        gamesetDB = GameSetDB.getInstace(mContext!!)!!
        itemDB = ItemDB.getInstace(mContext!!)!!

        tv_before = dlg.findViewById(R.id.tv_before)
        tv_after = dlg.findViewById(R.id.tv_after)
        btn_close = dlg.findViewById(R.id.btn_close)

        when(theme){
            1->{
                tv_before.text = "$"+dec.format(SET_CASH_STEP[step]).toString()
                tv_after.text = "$"+ dec.format(SET_CASH_STEP[step+1]).toString()
            }
            2->{
                tv_before.text = "$"+ dec.format(SET_MONTHLY_STEP[step]).toString()
                tv_after.text = "$"+ dec.format(SET_MONTHLY_STEP[step+1]).toString()
            }
            3->{
                tv_before.text = "    "+SET_SALARY_RAISE_STEP[step].toString() +"%"
                tv_after.text = SET_SALARY_RAISE_STEP[step+1].toString()+ "%  "
            }
        }
        println("---돈: " + profileDbManager.getMoney()!!)
        setasset(getHash(profileDbManager.getLoginId()!!),
                SET_CASH_STEP [gamesetDB?.gameSetDao()?.getSetCash()!!],
                SET_MONTHLY_STEP[gamesetDB?.gameSetDao()?.getSetMonthly()!!],
                SET_SALARY_RAISE_STEP[gamesetDB?.gameSetDao()?.getSetSalaryRaise()!!],
                profileDbManager.getMoney()!!,
                itemDB?.itemDao()?.getPotion()!!)
        btn_close.setOnClickListener{
            dlg.dismiss()
        }

        dlg.show()
    }
    fun setasset(u_id: String, u_setcash : Float, u_setmonthly : Float, u_setsalaryraise : Float, u_money : Int, u_potion : Int) {
        var funsetasset: RetrofitSetAsset? = null
        val url = "http://stockgame.dothome.co.kr/test/setasset.php/"
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
        funsetasset= retrofit.create(RetrofitSetAsset::class.java)
        funsetasset.setasset(u_id, u_setcash, u_setmonthly, u_setsalaryraise, u_money, u_potion).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()!! == "555"){
                        Toast.makeText(mContext, "서버에 올라감", Toast.LENGTH_LONG).show()
                    }
                    else if(response.body()!! == "666"){
                        Toast.makeText(mContext, "오류 발생", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(mContext, "앱을 종료 후 다시 실행시켜 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

}