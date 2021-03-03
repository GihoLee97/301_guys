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
import com.guys_from_301.stock_game.data.ProfileDB
import com.kakao.sdk.user.UserApiClient
import org.w3c.dom.Text
import com.guys_from_301.stock_game.data.Profile


/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfile : Fragment() {


//    private val profileActivityViewModel = ProfileActivityViewModel(_MainActivity!!)
    private lateinit var tv_my_nick : TextView
    var loginMethod : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_profile, container, false)
        tv_my_nick = v.findViewById(R.id.tv_my_nick)
        tv_my_nick.text = profileDbManager!!.getNickname()
        v.findViewById<TextView>(R.id.tv_my_level).text = "레벨 " + profileDbManager!!.getLevel().toString()
        v.findViewById<ProgressBar>(R.id.pb_exp_bar).progress = profileDbManager!!.getExp()!!
        if(profileDbManager!!.getLogin() != 4){
            v.findViewById<ImageView>(R.id.iv_my_image).visibility=View.INVISIBLE
        }
        else{
            UserApiClient.instance.me { user, error ->
                if (error!=null)
                    Toast.makeText(_MainActivity,"사용자 정보 요청 실패(카카오)", Toast.LENGTH_SHORT)
                else if (user!=null) {
                    v.findViewById<ImageView>(R.id.iv_my_image).loadCircularImage(user?.kakaoAccount?.profile?.thumbnailImageUrl, 5F, Color.parseColor("#F4730B"))
//                    Glide.with(_MainActivity!!).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop().into(v.findViewById<ImageView>(R.id.iv_my_image))
                }
            }
        }
//        v.findViewById<ImageView>(R.id.iv_my_image).
        v.findViewById<TextView>(R.id.tv_nickname_change).setOnClickListener{
            val dlg = Dialog_nick(_MainActivity!!, false)
            dlg.start()
        }
        v.findViewById<LinearLayout>(R.id.ll_notice).setOnClickListener{
            val intent = Intent(_MainActivity, NoticeActivity::class.java)
            startActivity(intent)
        }
        v.findViewById<LinearLayout>(R.id.ll_version_info).setOnClickListener{
            //TODO: 버전정보
            Toast.makeText(_MainActivity, "버전정보 구현해야 함", Toast.LENGTH_LONG).show()
        }
        v.findViewById<LinearLayout>(R.id.ll_game_option).setOnClickListener{
            val intent = Intent(_MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }
        v.findViewById<LinearLayout>(R.id.ll_alarmSetting).setOnClickListener {
            pushAlarmManager.openSetting(activity as MainActivity)
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
            requireActivity().finish()
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
}