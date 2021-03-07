package com.guys_from_301.stock_game

import android.content.Context
import android.graphics.*
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.data.QuestDB
import com.guys_from_301.stock_game.kakaoMessageManager
import com.guys_from_301.stock_game.retrofit.RetrofitFriendCheck
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FriendInviteAdapter(val context: Context, var friends: Friends<Friend>, val itemClick: (Friend)->Unit): RecyclerView.Adapter<FriendInviteAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View, itemClick: (Friend) -> Unit): RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val iv_image = itemView.findViewById<ImageView>(R.id.iv_recyclerview_image)
        val tv_name = itemView.findViewById<TextView>(R.id.tv_recyclerview_name)
        val tv_invite = itemView.findViewById<TextView>(R.id.tv_recyclerview_invite)

        fun bind(friendUnit : Friend){
            iv_image.loadCircularImage(friendUnit.profileThumbnailImage, 5F, Color.parseColor("#F4730B"))
            tv_name.text = friendUnit.profileNickname
            tv_invite.setOnClickListener{
                friendcheck(getHash(friendUnit.id.toString()), friendUnit.uuid)
            }

            itemView.setOnClickListener{ itemClick(friendUnit) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_friend_invite, parent,
                false), itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentfriend = friends.elements[position]
        holder.apply {
            bind(currentfriend)
        }
    }

    override fun getItemCount(): Int {
        return friends.totalCount
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

    fun friendcheck(u_id: String, uuid : String) {
        var funfriendcheck: RetrofitFriendCheck? = null
        val url = "http://stockgame.dothome.co.kr/test/friendcheck.php/"
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
        funfriendcheck= retrofit.create(RetrofitFriendCheck::class.java)
        funfriendcheck.friendcheck(u_id).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("---서버업데이트실패: "+t.message)
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    println("---")
                    if(response.body()!! == "444"){
                        Toast.makeText(context, "이미 가입한 사용자입니다.", Toast.LENGTH_LONG).show()
                        // 친구 아이디가 있다.
                    }
                    else if(response.body()!! == "555"){
                        // 친구 아이디가 없다.
                        var questDb: QuestDB? = null
                        questDb = QuestDB.getInstance(context)
                        var friendquest = questDb?.questDao()?.getQuestByTheme("초대하기")?.get(0)
                        friendquest!!.achievement = friendquest!!.achievement+1
                        val addRunnable = kotlinx.coroutines.Runnable {
                            questDb?.questDao()?.insert(friendquest)
                        }
                        val addThread = Thread(addRunnable)
                        addThread.start()
                        rewardByStack(100000)
                        questAchieved.add(friendquest)

                        Toast.makeText(context, "초대를 보냈습니다.", Toast.LENGTH_LONG).show()
                        kakaoMessageManager.sendMessageonlyone(uuid, kakaoMessageManager.dummy)
                    }
                    else{
                        Toast.makeText(context, "앱을 종료 후 다시 실행시켜 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

    }
    //도전과제 스텍 보상 함수
    fun rewardByStack(reward: Int){
        if (!profileDbManager!!.isEmpty(context)) {
            profileDbManager!!.setMoney(profileDbManager!!.getMoney()!! + reward)
        }
    }

}