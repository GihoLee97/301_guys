package com.guys_from_301.stock_game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Dataclass_kakao {

    @SerializedName("NAME")
    @Expose
    var NAME : String

    @SerializedName("NICKNAME")
    @Expose
    var NICKNAME : String

    @SerializedName("MONEY")
    @Expose
    var MONEY : Int

    @SerializedName("LEVEL")
    @Expose
    var LEVEL : Int

    @SerializedName("IMAGE")
    @Expose
    var IMAGE : String

    constructor(NAME :String, NICKNAME:String, MONEY:Int, LEVEL:Int, IMAGE :String) {
        this.NAME = NAME
        this.NICKNAME = NICKNAME
        this.MONEY = MONEY
        this.LEVEL = LEVEL
        this.IMAGE = IMAGE
    }
}