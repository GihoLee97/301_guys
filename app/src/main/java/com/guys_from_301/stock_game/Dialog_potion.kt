package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.Item
import com.guys_from_301.stock_game.data.ItemDB
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_potion(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var tv_potion_count: TextView
    private lateinit var tv_potion_contents: TextView
    private lateinit var btn_potion: Button
    private lateinit var btn_potion_inactive: Button
    private lateinit var ib_close : ImageButton
    private lateinit var iv_potion_upper_inactive: ImageView
    private lateinit var iv_potion_bottom_inactive: ImageView
    var value1: Int = 0
    val value1reward: Int = 5000


    fun start(potion: Int){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_potion)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tv_potion_count = dlg.findViewById(R.id.tv_potion_count)
        tv_potion_contents = dlg.findViewById(R.id.tv_potion_contents)
        btn_potion = dlg.findViewById(R.id.btn_potion)
        btn_potion_inactive = dlg.findViewById(R.id.btn_potion_inactive)
        ib_close = dlg.findViewById(R.id.ib_cancel)
        iv_potion_upper_inactive = dlg.findViewById(R.id.iv_potion_upper_inactive)
        iv_potion_bottom_inactive = dlg.findViewById(R.id.iv_potion_bottom_inactive)

        tv_potion_count.text = "현재 물약 개수: "+potion+"개"
        if(potion>0){
            tv_potion_contents.text = "물약을 마시고 피로도를 낮춰보세요:)"
            iv_potion_bottom_inactive.visibility = View.INVISIBLE
            iv_potion_upper_inactive.visibility = View.INVISIBLE
            btn_potion_inactive.visibility = View.INVISIBLE
            btn_potion.visibility = View.VISIBLE
        }

        btn_potion.setOnClickListener{
            var profileDb: ProfileDB? = null
            var itemDb : ItemDB? = null
            profileDb = ProfileDB.getInstace(dlg.context)
            itemDb = ItemDB.getInstace(dlg.context)
            var value1 = profileDbManager!!.getValue1()!!.toInt()
            if (value1 >= value1reward) {
                value1 -= value1reward
            } else {
                value1 = 0
            }
            dbupdate()
            usepotion(getHash(profileDbManager!!.getLoginId()!!),
                    profileDbManager!!.getMoney()!!,
                    itemDb?.itemDao()?.getPotion()!!)
            dlg.dismiss()
        }

        ib_close.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    fun dbupdate() {
        var profileDb: ProfileDB? = null
        var itemDb: ItemDB? = null
        profileDb = ProfileDB.getInstace(dlg.context)
        itemDb = ItemDB.getInstace(dlg.context)
        var newItem = itemDb?.itemDao()?.getAll()!![0]
        if (!profileDbManager!!.isEmpty(dlg.context)) {
            profileDbManager!!.setValue1(value1)
        }
        newItem.potion = newItem.potion - 1
        itemDb?.itemDao()?.update(newItem)
    }

    fun usepotion(u_id: String, u_money : Int, u_potion : Int) {
        var funusepotion: RetrofitUsePotion? = null
        val url = "http://stockgame.dothome.co.kr/test/usepotion.php/"
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
        funusepotion= retrofit.create(RetrofitUsePotion::class.java)
        funusepotion.setasset(u_id, u_money, u_potion).enqueue(object : Callback<String> {
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