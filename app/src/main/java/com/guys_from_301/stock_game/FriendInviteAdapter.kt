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
import com.guys_from_301.stock_game.kakaoMessageManager
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends


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
                kakaoMessageManager.sendMessageonlyone(friendUnit.uuid, kakaoMessageManager.dummy)
                Toast.makeText(context, "초대하기 보냄", Toast.LENGTH_LONG).show()
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
}