package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.Notice
import com.guys_from_301.stock_game.data.NoticeDB
import com.guys_from_301.stock_game.data.ProfileDB
import com.kakao.sdk.user.UserApiClient
import org.w3c.dom.Text
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.retrofit.RetrofitNotice
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentProfile : Fragment() {
//    private val profileActivityViewModel = ProfileActivityViewModel(_MainActivity!!)
    private lateinit var tv_my_nick : TextView
    private lateinit var googleAuth : FirebaseAuth
    var loginMethod : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getnotice("1")
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_profile, container, false)
        activity?.window?.statusBarColor = resources.getColor(R.color.white)

        tv_my_nick = v.findViewById(R.id.tv_my_nick)
        tv_my_nick.text = profileDbManager!!.getNickname()
        v.findViewById<TextView>(R.id.tv_my_level).text = "레벨 " + profileDbManager!!.getLevel().toString()
        v.findViewById<ProgressBar>(R.id.pb_exp_bar).progress = profileDbManager!!.getExp()!! / (profileDbManager!!.getLevel()!! * 10)
        if(profileDbManager!!.getLogin() == 4){ // kakao login
            UserApiClient.instance.me { user, error ->
                if (error!=null)
                    Toast.makeText(_MainActivity,"사용자 정보 요청 실패(카카오)", Toast.LENGTH_SHORT)
                else if (user!=null) {
                    v.findViewById<ImageView>(R.id.iv_my_image).loadCircularImage(user?.kakaoAccount?.profile?.thumbnailImageUrl, 5F, Color.parseColor("#F4730B"))
//                    Glide.with(_MainActivity!!).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop().into(v.findViewById<ImageView>(R.id.iv_my_image))
                }
            }
        }
        else if(profileDbManager!!.getLogin() == 2){ // google login
            googleAuth = FirebaseAuth.getInstance()
            val currUser = googleAuth.currentUser
            v.findViewById<ImageView>(R.id.iv_my_image).loadCircularImage(currUser?.photoUrl, 5F, Color.parseColor("#F4730B"))
        }
        else{ // general login
            v.findViewById<ImageView>(R.id.iv_my_image).loadCircularImage(profileImageId[profileDbManager!!.getImageNum()!!], 5F, Color.parseColor("#F4730B"))
        }
//        v.findViewById<ImageView>(R.id.iv_my_image).
        v.findViewById<TextView>(R.id.tv_profile_change).setOnClickListener{
//            val dlg = Dialog_nick(_MainActivity!!, false)
//            dlg.start()
            val intent = Intent(_MainActivity, ProfileSetActivity::class.java)
            startActivity(intent)
        }
        v.findViewById<LinearLayout>(R.id.ll_notice).setOnClickListener{
            val intent = Intent(_MainActivity, NoticeActivity::class.java)
            startActivity(intent)
        }
        v.findViewById<LinearLayout>(R.id.ll_alarmSetting).setOnClickListener {

            val dlg = Dialog_alarmcheck(_MainActivity!!)
            dlg.start()
            //Todo:
        }
        v.findViewById<LinearLayout>(R.id.ll_purchaseHistory).setOnClickListener {
            val intent = Intent(_MainActivity, PurchaseHistoryAcitivity::class.java)
            startActivity(intent)
        }
        v.findViewById<LinearLayout>(R.id.ll_sign_out).setOnClickListener{
            if(profileDbManager!!.getLogin() == 1){
                loginMethod = "GENERAL"
            }
            else if(profileDbManager!!.getLogin() == 2){
                loginMethod = "GOOGLE"
            }
            else if(profileDbManager!!.getLogin() == 4){
                loginMethod =  "KAKAO"
            }
            updatelogOutInFo2DB(loginMethod!!)
            val intent = Intent(_MainActivity,NewInitialActivity::class.java)
            startActivity(intent)
            activity?.finishAffinity()
//            requireActivity().finish()
        }
        v.findViewById<LinearLayout>(R.id.ll_withdraw).setOnClickListener{
            if(profileDbManager!!.getLogin() == 1){
                loginMethod = "GENERAL"
            }
            else if(profileDbManager!!.getLogin() == 2){
                loginMethod = "GOOGLE"
            }
            else if(profileDbManager!!.getLogin() == 4){
                loginMethod =  "KAKAO"
            }
            if(loginMethod == "GENERAL"){
                val dlg_delete = Dialog_DeleteAlert(_MainActivity!!)
                dlg_delete.start()
            }
            else{
                val dlg_delete = Dialog_DeleteKakaoGoogle(_MainActivity!!,loginMethod!!)
                dlg_delete.start()
            }
        }
        v.findViewById<LinearLayout>(R.id.ll_support).setOnClickListener {
            val intent = Intent(_MainActivity,SupportActivity::class.java)
            startActivity(intent)
        }
        return v
    }

    override fun onPause() {
//        profileActivityViewModel.write2database()
        println("---pause")
        super.onPause()
    }

//
    override fun onStart() {
//        profileActivityViewModel.refresh()
        super.onStart()
    }


    fun <T> ImageView.loadCircularImage(
            model: T,
            borderSize: Float = 0F,
            borderColor: Int = Color.WHITE
    ) {
        Glide.with(context)
                .asBitmap()
                .load(model)
                .apply(RequestOptions.circleCropTransform())
                .into(object : BitmapImageViewTarget(this) {
                    override fun setResource(resource: Bitmap?) {
                        setImageDrawable(
                                resource?.run {
                                    RoundedBitmapDrawableFactory.create(
                                            resources,
                                            if (borderSize > 0) {
                                                createBitmapWithBorder(borderSize, borderColor)
                                            } else {
                                                this
                                            }
                                    ).apply {
                                        isCircular = true
                                    }
                                }
                        )
                    }
                })
    }

    /**
     * Create a new bordered bitmap with the specified borderSize and borderColor
     *
     * @param borderSize - The border size in pixel
     * @param borderColor - The border color
     * @return A new bordered bitmap with the specified borderSize and borderColor
     */
    private fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int): Bitmap {
        val borderOffset = (borderSize * 2).toInt()
        val halfWidth = width / 2
        val halfHeight = height / 2
        val circleRadius = Math.min(halfWidth, halfHeight).toFloat()
        val newBitmap = Bitmap.createBitmap(
                width + borderOffset,
                height + borderOffset,
                Bitmap.Config.ARGB_8888
        )

        // Center coordinates of the image
        val centerX = halfWidth + borderSize
        val centerY = halfHeight + borderSize

        val paint = Paint()
        val canvas = Canvas(newBitmap).apply {
            // Set transparent initial area
            drawARGB(0, 0, 0, 0)
        }

        // Draw the transparent initial area
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, circleRadius, paint)

        // Draw the image
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, borderSize, borderSize, paint)

        // Draw the createBitmapWithBorder
        paint.xfermode = null
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = borderSize
        canvas.drawCircle(centerX, centerY, circleRadius, paint)
        return newBitmap
    }
    fun updatelogOutInFo2DB(method : String){
        var loginMethod = 0
        if(method=="GENERAL") loginMethod = 1
        else if(method=="GOOGLE") loginMethod = 2
        else if(method=="KAKAO") loginMethod = 4
        if(!profileDbManager!!.isEmpty(_MainActivity!!)) {
            profileDbManager!!.setLogin(profileDbManager!!.getLogin()!!-loginMethod)
        }
    }

    override fun onResume() {
        super.onResume()
        tv_my_nick.text = profileDbManager!!.getNickname()
    }

    // NOTICE
    fun getnotice(u_id: String) {
        var funnotice: RetrofitNotice? = null
        var noticedb : NoticeDB? = null
        noticedb = NoticeDB.getInstace(_MainActivity!!)
        val url = "http://stockgame.dothome.co.kr/test/notice_get.php/"
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
        funnotice= retrofit.create(RetrofitNotice::class.java)
        funnotice.getnotice(u_id).enqueue(object : Callback<MutableList<DATACLASS_NOTICE>> {
            override fun onFailure(call: Call<MutableList<DATACLASS_NOTICE>>, t: Throwable) {
                println("---서버통신실패: "+t.message)
            }
            override fun onResponse(call: Call<MutableList<DATACLASS_NOTICE>>,
                response: retrofit2.Response<MutableList<DATACLASS_NOTICE>>) {
                if (response.isSuccessful && response.body() != null) {
                    println("---서버통신성공")
                    var data = response.body()!!
                    if (noticedb?.noticeDao()?.getAll()!!.size >= 2) {
                    } else {
                        for (i in 0..data.size - 1) {
                            val newNotice = Notice()
                            newNotice.id = data[i].ID
                            newNotice.title = data[i].TITLE
                            newNotice.contents = data[i].CONTENT
                            newNotice.date = data[i].DATE
                            noticedb?.noticeDao()?.insert(newNotice)
                        }
                    }
                }
            }
        })

    }
}