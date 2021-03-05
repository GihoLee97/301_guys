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
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB
import com.guys_from_301.stock_game.data.ItemDB
import com.guys_from_301.stock_game.data.ProfileDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_negotiation_unsuccess(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btn_close: Button
    private lateinit var tv_fail_message: TextView
    private lateinit var profileDB: ProfileDB
    private lateinit var gamesetDB : GameSetDB
    private lateinit var itemDB : ItemDB

    fun start(theme: Int, potion: Int){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_negotiation_unsuccess)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        profileDB = ProfileDB.getInstace(mContext!!)!!
        gamesetDB = GameSetDB.getInstace(mContext!!)!!
        itemDB = ItemDB.getInstace(mContext!!)!!


        btn_close = dlg.findViewById(R.id.btn_close)
        tv_fail_message = dlg.findViewById(R.id.tv_negotiation_fail_message)

        when(theme){
            1->tv_fail_message.text = "협상에 실패하여 초기자산이 $"+ dec.format(SET_CASH_STEP[0])+"가 되었습니다"
            2->tv_fail_message.text = "협상에 실패하여 월급이 $"+ dec.format(SET_MONTHLY_STEP[0])+"가 되었습니다"
            3->tv_fail_message.text = "협상에 실패하여 연봉 인상률이 "+ SET_SALARY_RAISE_STEP[0]+"%가 되었습니다"
            4->tv_fail_message.text = "현재 스택이 부족합니다"
            5->tv_fail_message.text = "더 이상 협상을 진행할 수 없습니다"
            6->tv_fail_message.text = "피로도 물약 구매 성공 ! 현재 개수 " + potion +"개"
        }
        setasset(getHash(profileDbManager!!.getLoginId()!!),
            SET_CASH_STEP [gamesetDB?.gameSetDao()?.getSetCashWithId(accountID!!)!!],
            SET_MONTHLY_STEP[gamesetDB?.gameSetDao()?.getSetMonthlyWithId(accountID!!)!!],
            SET_SALARY_RAISE_STEP[gamesetDB?.gameSetDao()?.getSetSalaryRaiseWithId(accountID!!)!!],
            profileDbManager!!.getMoney()!!,
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