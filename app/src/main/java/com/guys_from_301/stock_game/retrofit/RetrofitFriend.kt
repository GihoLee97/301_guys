package com.guys_from_301.stock_game.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.guys_from_301.stock_game.DATACLASS
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitFriend  {
    @FormUrlEncoded
    @POST("/test/friendrank.php/")
    fun funfriend(
            @Field("u_id") u_id : String
    ): Call<FRIENDCLASS>
}

class FRIENDCLASS {

    @SerializedName("MONEY")
    @Expose
    var MONEY : Int

    @SerializedName("NICKNAME")
    @Expose
    var NICKNAME : String

    @SerializedName("LEVEL")
    @Expose
    var LEVEL : Int

    constructor(MONEY: Int, NICKNAME:String, LEVEL : Int) {
        this.MONEY = MONEY
        this.NICKNAME = NICKNAME
        this.LEVEL = LEVEL
    }
}